package com.xl.game.cluster.server.tcp;

import com.xl.game.cluster.manager.ServerManager;
import com.xl.game.config.MailConfig;
import com.xl.game.config.MinaServerTcpConfig;
import com.xl.game.config.ThreadPoolExecutorTcpConfig;
import com.xl.game.engine.ServerInfo;
import com.xl.game.mina.TcpServer;
import com.xl.game.mina.handler.DefaultProtocolHandler;
import com.xl.game.script.ITimerEventScript;
import com.xl.game.server.Service;
import com.xl.game.thread.ServerThread;
import com.xl.game.thread.ThreadType;
import com.xl.game.thread.timer.events.ServerHeartTimer;
import com.xl.game.util.IntUtil;
import com.xl.game.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * TCP连接
 *
 * @author xl
 * @date 2019-12-06 QQ:2755055412
 */
@Component
@Slf4j
public class ClusterTcpServer extends Service {


    @Autowired
    ThreadPoolExecutorTcpConfig threadPoolExecutorTcpConfig;
    @Autowired
    MailConfig mailConfig;
    @Autowired
    MinaServerTcpConfig minaServerConfig;

    public static final String SERVER_INFO = "serverInfo"; // 服务器信息

    @Autowired
    ITimerEventScript iTimerEventScript;

    private TcpServer tcpServer;

    @PostConstruct
    public void init() {
        // IO默认线程池 客户端的请求,默认使用其执行
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorTcpConfig.newThreadPoolExecutor();
        this.serverThreads.put(ThreadType.IO, threadPoolExecutor);
        //全局sync线程
        ServerThread serverThreadSync = new ServerThread(new ThreadGroup("全局同步线程"),
                "全局同步线程:" + getClass().getSimpleName(), threadPoolExecutorTcpConfig.getHeart(),
                threadPoolExecutorTcpConfig.getCommandSize(), mailConfig);
        serverThreadSync.start();
        serverThreads.put(ThreadType.SYNC, serverThreadSync);

        //初始化tcpServer 服务线程
        tcpServer = new TcpServer(minaServerConfig, new ClusterTcpServerHandler(this));
    }

    @Override
    protected void running() {
        log.debug(" run ... ");
        tcpServer.run();
        //心跳检测
        ServerThread syncThread = getExecutor(ThreadType.SYNC);
        syncThread.addTimerEvent(new ServerHeartTimer(iTimerEventScript));
    }

    /**
     * 消息处理器
     */
    public class ClusterTcpServerHandler extends DefaultProtocolHandler {

        private final Service service;

        public ClusterTcpServerHandler(Service service) {
            super(4); // 消息ID+消息内容
            this.service = service;
        }


        @Override
        public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) {
            MsgUtil.close(ioSession, "链接空闲:" + ioSession.toString() + " " + idleStatus.toString()); // 客户端长时间不发送请求，将断开链接LoginTcpServer->minaServerConfig->readerIdleTime
            // 60
            // 1分钟
        }

        @Override
        public void sessionClosed(IoSession ioSession) {
            super.sessionClosed(ioSession);
            ServerInfo serverInfo = (ServerInfo) ioSession.getAttribute(SERVER_INFO);
            if (serverInfo != null) {
                log.warn("服务器:" + serverInfo.getName() + "断开链接");
                ServerManager.getInstance().removeServer(serverInfo);
            } else {
                log.error("未知游戏服务器断开链接");
            }
        }


        @Override
        protected void forward(IoSession session, int msgID, byte[] bytes) {
            log.warn("无法找到消息处理器：msgID{},bytes{}", msgID, IntUtil.BytesToStr(bytes));
        }

        @Override
        public Service getService() {
            return service;
        }
    }


}
