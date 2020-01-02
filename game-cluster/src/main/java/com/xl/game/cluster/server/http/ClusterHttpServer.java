package com.xl.game.cluster.server.http;

import com.xl.game.config.*;
import com.xl.game.handler.HttpServerIoHandler;
import com.xl.game.mina.HttpServer;
import com.xl.game.server.Service;
import com.xl.game.thread.ServerThread;
import com.xl.game.thread.ThreadType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Slf4j
public class ClusterHttpServer extends Service {

    @Autowired
    MinaServerHttpConfig minaServerConfig;


    private HttpServer httpServer;


    @Autowired
    ThreadPoolExecutorHttpConfig threadPoolExecutorConfig;


    @Autowired
    MailConfig mailConfig;

    @Autowired
    JedisClusterConfig jedisClusterConfig;

    @PostConstruct
    public void init() {
        // IO默认线程池 客户端的请求,默认使用其执行
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorConfig.newThreadPoolExecutor();
        this.serverThreads.put(ThreadType.IO, threadPoolExecutor);
        //全局sync线程
        ServerThread serverThreadSync = new ServerThread(new ThreadGroup("全局同步线程"),
                "全局同步线程:" + getClass().getSimpleName(), threadPoolExecutorConfig.getHeart(),
                threadPoolExecutorConfig.getCommandSize(), mailConfig);
        serverThreadSync.start();
        serverThreads.put(ThreadType.SYNC, serverThreadSync);

        httpServer = new HttpServer(minaServerConfig, new ClusterHttpServerHandler(this));
    }


    @Override
    protected void running() {
        log.debug(" http server run ... ");
        httpServer.run();

    }


    /**
     * 消息处理器
     */

    public class ClusterHttpServerHandler extends HttpServerIoHandler {

        private final Service service;

        public ClusterHttpServerHandler(Service service) {
            this.service = service;
        }

        @Override
        protected Service getSevice() {
            return service;
        }
    }


}
