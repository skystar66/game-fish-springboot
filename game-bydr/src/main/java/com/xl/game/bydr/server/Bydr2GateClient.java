package com.xl.game.bydr.server;


import com.xl.game.bydr.thread.RoomExecutor;
import com.xl.game.config.MailConfig;
import com.xl.game.config.MinaClientConfig;
import com.xl.game.config.ThreadPoolExecutorTcpConfig;
import com.xl.game.engine.ServerInfo;
import com.xl.game.message.IDMessage;
import com.xl.game.message.ServerMessage;
import com.xl.game.mina.service.MinaClientService;
import com.xl.game.mina.service.MutilMinaTcpClientService;
import com.xl.game.model.script.IGameServerCheckScript;
import com.xl.game.server.ServerState;
import com.xl.game.thread.ThreadType;
import com.xl.game.util.SysUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;

/**
 * 捕鱼达人通过网关连接大厅 Tcp客户端
 *
 * @author xuliang
 * @QQ 359135103 2020年1月7日 下午4:12:57
 */
@Slf4j
public class Bydr2GateClient extends MutilMinaTcpClientService {


    private IGameServerCheckScript iGameServerCheckScript;

    private MailConfig mailConfig;

    public Bydr2GateClient(MinaClientConfig minaClientConfig,IGameServerCheckScript iGameServerCheckScript) {
        super(minaClientConfig);
        this.iGameServerCheckScript=iGameServerCheckScript;
    }

    public Bydr2GateClient(ThreadPoolExecutorTcpConfig threadPoolExecutorConfig, MinaClientConfig minaClientConfig, MailConfig mailConfig
            ,IGameServerCheckScript iGameServerCheckScript) {
        super(threadPoolExecutorConfig, minaClientConfig, mailConfig);
        this.mailConfig=mailConfig;
        this.iGameServerCheckScript=iGameServerCheckScript;
    }


    @Override
    protected void running() {

        // 全局同步线程
//        ServerThread syncThread = getExecutor(ThreadType.SYNC);
//        syncThread.addTimerEvent(new ServerHeartTimer());

        //todo  添加线程池

        // 添加房间线程池
        RoomExecutor roomExecutor = new RoomExecutor(mailConfig);
        getServerThreads().put(ThreadType.ROOM, roomExecutor);
    }



    /**
     * 消息处理器
     *
     * @author xuliang
     * @QQ 359135103 2020年1月7日 下午6:29:34
     */

    public class MutilConHallHandler extends MutilTcpProtocolHandler {


        public MutilConHallHandler(ServerInfo serverInfo, MinaClientService service) {
            super(serverInfo, service);
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            super.sessionOpened(session);
            //todo  向网关注册
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
            info.setTotalMemory(SysUtil.totalMemory());
            info.setFreeMemory(SysUtil.freeMemory());
            iGameServerCheckScript.buildServerInfo(info);
            builder.setServerInfo(info);
            session.write(new IDMessage(session, builder.build(), 0));
        }
    }


}
