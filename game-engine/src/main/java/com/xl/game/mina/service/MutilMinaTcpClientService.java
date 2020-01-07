package com.xl.game.mina.service;


import com.xl.game.config.MailConfig;
import com.xl.game.config.MinaClienConnToConfig;
import com.xl.game.config.MinaClientConfig;
import com.xl.game.config.ThreadPoolExecutorTcpConfig;
import com.xl.game.engine.ServerInfo;
import com.xl.game.engine.ServerType;
import com.xl.game.message.IDMessage;
import com.xl.game.mina.MinaMultiTcpClient;
import com.xl.game.mina.handler.DefaultClientProtocolHandler;
import com.xl.game.server.IMutilTcpClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;

import java.util.HashMap;
import java.util.Map;

/**
 * 多tcp连接客户端
 * <p>
 * 一般用于子游戏服务器和网关服，所有玩家共享连接
 * </p>
 *
 * @author xuliang
 * @QQ 359135103 2017年6月30日 下午3:23:32
 */
@Slf4j
public class MutilMinaTcpClientService extends MinaClientService implements IMutilTcpClientService {


    protected final MinaMultiTcpClient multiTcpClient = new MinaMultiTcpClient();

    /**
     * 网关服务器
     */
    protected final Map<Integer, ServerInfo> gateServerMap = new HashMap<>();

    public MutilMinaTcpClientService(MinaClientConfig minaClientConfig) {
        super(minaClientConfig);
    }

    public MutilMinaTcpClientService(ThreadPoolExecutorTcpConfig threadPoolExecutorConfig, MinaClientConfig minaClientConfig, MailConfig mailConfig) {
        super(threadPoolExecutorConfig, minaClientConfig, mailConfig);
    }

    /**
     * 移除客户端
     *
     * @param serverId
     */
    @Override
    public void removeTcpClient(int serverId) {
        multiTcpClient.removeTcpClient(serverId);
        gateServerMap.remove(serverId);
    }

    /**
     * 添加连接大厅服务器
     *
     * @param serverInfo
     */
    @Override
    public void addTcpClient(ServerInfo serverInfo, int port) {
        addTcpClient(serverInfo, port, new MutilTcpProtocolHandler(serverInfo, this));
    }

    /**
     * 添加连接大厅服务器
     *
     * @param serverInfo
     */
    public void addTcpClient(ServerInfo serverInfo, int port, MutilTcpProtocolHandler ioHandler) {

        if (multiTcpClient.containsKey(serverInfo.getId())) {
            return;
        }
        MinaClientConfig minaClientConfig = createMinaClientConfig(serverInfo, port);
        multiTcpClient.addTcpClient(this, minaClientConfig, ioHandler);
    }


    /**
     * 创建连接网关配置文件
     *
     * @param serverInfo
     * @param port
     * @return
     */
    private MinaClientConfig createMinaClientConfig(ServerInfo serverInfo, int port) {
        MinaClientConfig conf = new MinaClientConfig();
        conf.setType(ServerType.GATE);
        conf.setId(serverInfo.getId());
        conf.setMaxConnectCount(getMinaClientConfig().getMaxConnectCount());
        conf.setOrderedThreadPoolExecutorSize(getMinaClientConfig().getOrderedThreadPoolExecutorSize());
        MinaClienConnToConfig con = new MinaClienConnToConfig();
        con.setHost(serverInfo.getIp());
        con.setPort(port);
        conf.setConnTo(con);
        return conf;

    }


    @Override
    public Map<Integer, ServerInfo> getServers() {
        return gateServerMap;
    }

    /**
     * 发送消息
     *
     * @param serverId
     *            目标服务器ID
     * @param msg
     * @return
     */
    @Override
    public boolean sendMsg(Integer serverId, Object msg) {
        if (multiTcpClient == null) {
            return false;
        }
        IDMessage idm = new IDMessage(null, msg, 0);
        return multiTcpClient.sendMsg(serverId, idm);
    }


    /**
     * 广播所有服务器消息：注意，这里并不是向每个session广播，
     * 因为有可能有多个连接到同一个服务器
     *
     * @param obj
     * @return
     */

    @Override
    public boolean broadcastMsg(Object obj) {
        if (multiTcpClient == null) {
            return false;
        }


        IDMessage idm = new IDMessage(null, obj, 0);
        gateServerMap.values().forEach(server -> {
            server.sendMsg(idm);
        });
        return true;
    }

    /**
     * 广播所有服务器消息：注意，这里并不是向每个session广播，
     * 因为有可能有多个连接到同一个服务器
     *
     * @param obj
     * @return
     */
    public boolean broadcastMsg(Object obj,long rid) {
        if (multiTcpClient == null) {
            return false;
        }
        IDMessage idm = new IDMessage(null, obj, rid);
        gateServerMap.values().forEach(server -> {
            server.sendMsg(idm);
        });
        return true;
    }

    /**
     * 监测连接状态
     */
    @Override
    public void checkStatus() {

        multiTcpClient.tcpClients.values().forEach(minaTcpClient -> {
            minaTcpClient.checkStatus();
        });

    }

    @Override
    protected void running() {

    }

    public MinaMultiTcpClient getMultiTcpClient() {
        return multiTcpClient;
    }

    public class MutilTcpProtocolHandler extends DefaultClientProtocolHandler {

        private final ServerInfo serverInfo;

        public MutilTcpProtocolHandler(ServerInfo serverInfo, MinaClientService service) {
            super(12, service);
            this.serverInfo = serverInfo;
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            super.sessionOpened(session);
            serverInfo.onIoSessionConnect(session);
        }

    }
}
