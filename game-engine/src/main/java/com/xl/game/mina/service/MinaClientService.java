package com.xl.game.mina.service;

import com.xl.game.config.MailConfig;
import com.xl.game.config.MinaClientConfig;
import com.xl.game.config.ThreadPoolExecutorTcpConfig;
import com.xl.game.server.ITcpClientService;
import com.xl.game.server.Service;
import com.xl.game.thread.ServerThread;
import com.xl.game.thread.ThreadType;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public abstract class MinaClientService extends Service implements ITcpClientService {


    private MinaClientConfig minaClientConfig;

    private MailConfig mailConfig;

    /**
     * 连接会话
     */

    private final PriorityBlockingQueue<IoSession> sessions = new PriorityBlockingQueue<>(128,
            (IoSession o1, IoSession o2) -> {
                int res = o1.getScheduledWriteMessages() - o2.getScheduledWriteMessages();
                if (res == 0) {
                    res = (int) (o1.getWrittenBytes() - o2.getWrittenBytes());
                }
                return res;
            });


    /**
     * 无线程池
     *
     * @param minaClientConfig
     */
    public MinaClientService(MinaClientConfig minaClientConfig) {
        this.minaClientConfig = minaClientConfig;
    }


    /**
     * 有线程池
     *
     * @param minaClientConfig
     */
    public MinaClientService(ThreadPoolExecutorTcpConfig threadPoolExecutorConfig, MinaClientConfig minaClientConfig,
                             MailConfig mailConfig) {

        // IO默认线程池 客户端的请求,默认使用其执行
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorConfig.newThreadPoolExecutor();
        this.serverThreads.put(ThreadType.IO, threadPoolExecutor);
        //全局sync线程
        ServerThread serverThreadSync = new ServerThread(new ThreadGroup("全局同步线程"),
                "全局同步线程:" + getClass().getSimpleName(), threadPoolExecutorConfig.getHeart(),
                threadPoolExecutorConfig.getCommandSize(), mailConfig);
        serverThreadSync.start();
        serverThreads.put(ThreadType.SYNC, serverThreadSync);
        this.minaClientConfig = minaClientConfig;
    }


    /**
     * 连接建立
     */
    public void onIoSessionConnect(IoSession session) {
        sessions.add(session);
        log.info("添加链接，【{}】个链接",sessions.size());

    }


    /**
     * 连接关闭移除
     */
    public void onIoSessionClosed(IoSession session) {
        log.info("移除连接");
        sessions.remove(session);
    }


    public boolean isSessionEmpty() {
        return sessions.isEmpty();
    }


    /**
     * 发送消息
     *
     * @param obj
     * @return
     */
    @Override
    public boolean sendMsg(Object object) {

        IoSession session = getMostIdleIoSession();

        if (null != session) {
            session.write(object);
            return true;
        }

        return false;
    }


    /**
     * 获取连接列表中最空闲的有效的连接
     *
     * @return
     */

    public IoSession getMostIdleIoSession() {
        IoSession session = null;
        while (session == null && !sessions.isEmpty()) {
            session = sessions.peek();
            if (session != null && session.isConnected()) {
                break;
            }else {
                log.info("执行poll========");
                sessions.poll();
            }
        }
        return session;

    }


}
