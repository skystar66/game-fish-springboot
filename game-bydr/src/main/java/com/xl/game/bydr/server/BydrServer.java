package com.xl.game.bydr.server;

import com.xl.game.bydr.config.MinaClusterClientConfig;
import com.xl.game.bydr.config.MinaGateClientConfig;
import com.xl.game.bydr.init.HandlerInit;
import com.xl.game.config.MailConfig;
import com.xl.game.config.MinaClientConfig;
import com.xl.game.config.ThreadPoolExecutorTcpConfig;
import com.xl.game.engine.ServerInfo;
import com.xl.game.message.ServerMessage;
import com.xl.game.model.constants.NetPort;
import com.xl.game.model.redis.channel.BydrChannel;
import com.xl.game.model.script.IGameServerCheckScript;
import com.xl.game.model.timer.GameServerCheckTimer;
import com.xl.game.redis.listener.JedisPubListener;
import com.xl.game.redis.manager.JedisManager;
import com.xl.game.server.IMutilTcpClientService;
import com.xl.game.server.ITcpClientService;
import com.xl.game.server.Service;
import com.xl.game.thread.ThreadType;
import com.xl.game.util.Config;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Component
public class BydrServer {


    /**
     * 连接网关 （接收网关转发过来的消息）
     */
    private IMutilTcpClientService bydr2GateClient;
    /**
     * 连接集群服 （获取各服务器信息）
     */
    private ITcpClientService bydr2ClusterClient;

    /**
     * 游戏前端消息服务 （消息直接从手机前端发来,如果没有直接注释掉，不经过大厅网关转发,暂时用engine封装类）
     */
    @Autowired
    private BydrGameTcpServer bydrTcpServer;

    /**
     * http服务器
     */
    @Autowired
    private BydrHttpServer gameHttpServer;

    /**
     * 服务器状态监测
     */
    private GameServerCheckTimer gameServerCheckTimer;

    /**
     * redis订阅发布
     */
    private JedisPubListener bydrPubListener;

    @Autowired
    ThreadPoolExecutorTcpConfig threadPoolExecutorTcpConfig;


    @Autowired
    MinaGateClientConfig minaGateClientConfig;

    @Autowired
    MinaClusterClientConfig minaClusterClientConfig;


    @Autowired
    MailConfig mailConfig;

    @Autowired
    IGameServerCheckScript gameServerCheckScript;

    @Autowired
    JedisManager jedisManager;


    public void start() {


        //初始化handler
        new Thread(new HandlerInit()).start();

        MinaClientConfig minaClientConfig_gate = new MinaClientConfig();
        BeanUtils.copyProperties(minaGateClientConfig, minaClientConfig_gate);
        //连接网关
        bydr2GateClient = new Bydr2GateClient(threadPoolExecutorTcpConfig,
                minaClientConfig_gate, mailConfig,gameServerCheckScript);

        new Thread(bydr2GateClient).start();

        MinaClientConfig minaClientConfig_cluster = new MinaClientConfig();

        BeanUtils.copyProperties(minaClusterClientConfig, minaClientConfig_cluster);

        //注册集群
        bydr2ClusterClient = new Bydr2ClusterClient(minaClientConfig_cluster);
        new Thread(bydr2ClusterClient).start();

        // 状态监控
        gameServerCheckTimer = new GameServerCheckTimer(bydr2ClusterClient, bydr2GateClient,
                minaClientConfig_gate, gameServerCheckScript);
        gameServerCheckTimer.start();


        // 订阅发布
        bydrPubListener = new JedisPubListener(jedisManager, BydrChannel.getChannels());

        new Thread(bydrPubListener).start();


        new Thread(bydrTcpServer).start();
        new Thread(gameHttpServer).start();




        //设置配置相关常量
        Config.SERVER_ID = minaClientConfig_gate.getId();
        Config.SERVER_NAME = minaClientConfig_gate.getName();
    }


    public IMutilTcpClientService getBydr2GateClient() {
        return bydr2GateClient;
    }


    /**
     * 更新可用网关服务器信息
     *
     * @param info
     */
    public void updateGateServerInfo(ServerMessage.ServerInfo info) {
        ServerInfo serverInfo = getBydr2GateClient().getServers().get(info.getId());
        if (serverInfo == null) {
            serverInfo = getServerInfo(info);
            if (getBydr2GateClient() instanceof Bydr2GateClient) {
                Bydr2GateClient service = (Bydr2GateClient) getBydr2GateClient();
                // TODO 暂时，网关服，有多个tcp端口
                service.addTcpClient(serverInfo, NetPort.GATE_GAME_PORT, service.new MutilConHallHandler(serverInfo, service));
            } else {
                getBydr2GateClient().addTcpClient(serverInfo, NetPort.GATE_GAME_PORT);
            }
        } else {
            serverInfo.setIp(info.getIp());
            serverInfo.setId(info.getId());
            serverInfo.setPort(info.getPort());
            serverInfo.setState(info.getState());
            serverInfo.setOnline(info.getOnline());
            serverInfo.setMaxUserCount(info.getMaxUserCount());
            serverInfo.setName(info.getName());
            serverInfo.setHttpPort(info.getHttpport());
            serverInfo.setWwwip(info.getWwwip());
        }
        getBydr2GateClient().getServers().put(info.getId(), serverInfo);


    }

    /**
     * 消息转换
     *
     * @param info
     * @return
     * @author JiangZhiYong
     * @QQ 359135103
     * 2017年8月29日 下午2:21:52
     */
    private ServerInfo getServerInfo(ServerMessage.ServerInfo info) {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setIp(info.getIp());
        serverInfo.setId(info.getId());
        serverInfo.setPort(info.getPort());
        serverInfo.setState(info.getState());
        serverInfo.setOnline(info.getOnline());
        serverInfo.setMaxUserCount(info.getMaxUserCount());
        serverInfo.setName(info.getName());
        serverInfo.setHttpPort(info.getHttpport());
        serverInfo.setWwwip(info.getWwwip());
        serverInfo.setFreeMemory(info.getFreeMemory());
        serverInfo.setTotalMemory(info.getTotalMemory());
        serverInfo.setVersion(info.getVersion());
        return serverInfo;
    }

    /**
     * 获取线程 在连接网关服的service中获取
     * @author xuliang
     * @QQ 359135103
     * 2020年1月9日 下午4:33:48
     * @param threadType
     * @return
     */

    public  <T extends Executor> T getExecutor(ThreadType threadType) {
        return (T) ((Service)bydr2GateClient).getExecutor(threadType);
    }


    public BydrHttpServer getGameHttpServer() {
        return gameHttpServer;
    }
}
