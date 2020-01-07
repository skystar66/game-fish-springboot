package com.xl.game.hall.server;

import com.xl.game.config.MailConfig;
import com.xl.game.config.MinaClientConfig;
import com.xl.game.config.ThreadPoolExecutorTcpConfig;
import com.xl.game.engine.ServerInfo;
import com.xl.game.message.IDMessage;
import com.xl.game.message.ServerMessage;
import com.xl.game.mina.service.MinaClientService;
import com.xl.game.mina.service.MutilMinaTcpClientService;
import com.xl.game.model.constants.NetPort;
import com.xl.game.model.script.IGameServerCheckScript;
import com.xl.game.server.ServerState;
import com.xl.game.thread.ServerThread;
import com.xl.game.thread.ThreadType;
import com.xl.game.thread.timer.events.ServerHeartTimer;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;


/**
 * 捕鱼达人连接大厅 Tcp客户端
 *
 * @author JiangZhiYong
 * @QQ 359135103 2017年6月28日 下午4:12:57
 */
@Slf4j
public class Hall2GateClient extends MutilMinaTcpClientService {


    private IGameServerCheckScript iGameServerCheckScript;


    public Hall2GateClient(MinaClientConfig minaClientConfig) {
        super(minaClientConfig);
    }

    public Hall2GateClient(ThreadPoolExecutorTcpConfig threadPoolExecutorConfig, MinaClientConfig minaClientConfig, MailConfig mailConfig


    ,IGameServerCheckScript iGameServerCheckScript) {
        super(threadPoolExecutorConfig, minaClientConfig, mailConfig);
        this.iGameServerCheckScript=iGameServerCheckScript;
    }

    @Override
    protected void running() {

//        ServerThread syncThread = getExecutor(ThreadType.SYNC);
//        syncThread.addTimerEvent(new ServerHeartTimer());

    }


    /**
     * 更新大厅服务器信息
     *
     * @param info
     */
    public void updateHallServerInfo(ServerMessage.ServerInfo info) {

        //获取网关服务器
        ServerInfo serverInfo = gateServerMap.get(info.getId());

        if (serverInfo == null) {
            serverInfo = getServerInfo(info);
            addTcpClient(serverInfo, NetPort.GATE_GAME_PORT, new MutilConHallHandler(serverInfo, this, iGameServerCheckScript));
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
        gateServerMap.put(info.getId(), serverInfo);
    }

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
        return serverInfo;
    }


    public class MutilConHallHandler extends MutilTcpProtocolHandler {


        private IGameServerCheckScript iGameServerCheckScript;

        public MutilConHallHandler(ServerInfo serverInfo, MinaClientService service,
                                   IGameServerCheckScript iGameServerCheckScript) {
            super(serverInfo, service);
            this.iGameServerCheckScript = iGameServerCheckScript;
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            super.sessionOpened(session);
            // 向网关服注册session TODO
            ServerMessage.ServerRegisterRequest.Builder builder = ServerMessage.ServerRegisterRequest.newBuilder();
            ServerMessage.ServerInfo.Builder info = ServerMessage.ServerInfo.newBuilder();
            info.setId(getMinaClientConfig().getId());
            info.setIp("");
            info.setMaxUserCount(1000);
            info.setOnline(1);
            info.setName(getMinaClientConfig().getName());
            info.setState(ServerState.NORMAL.getState());
            info.setType(getMinaClientConfig().getType().getType());
            info.setWwwip("");
            iGameServerCheckScript.buildServerInfo(info);
            builder.setServerInfo(info);
            session.write(new IDMessage(session, builder.build(), 0));

        }
    }


}



