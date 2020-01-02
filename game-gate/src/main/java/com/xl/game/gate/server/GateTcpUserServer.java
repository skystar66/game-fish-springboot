package com.xl.game.gate.server;


import com.xl.game.config.MailConfig;
import com.xl.game.config.MinaServerTcpConfig;
import com.xl.game.config.ThreadPoolExecutorTcpConfig;
import com.xl.game.gate.config.MinaServerTcpUserConfig;
import com.xl.game.gate.manager.UserSessionManager;
import com.xl.game.gate.script.IUserScript;
import com.xl.game.gate.server.handler.GateTcpUserServerHandler;
import com.xl.game.mina.TcpServer;
import com.xl.game.script.ITimerEventScript;
import com.xl.game.server.Service;
import com.xl.game.thread.ServerThread;
import com.xl.game.thread.ThreadType;
import com.xl.game.thread.timer.events.ServerHeartTimer;
import com.xl.game.util.CommonBeanUtils;
import com.xl.game.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.firewall.BlacklistFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 网关 用户 TCP服务器
 *
 * @author xuliang
 * @date 2019-12-30 QQ:359135103
 */
@Component
@Slf4j
public class GateTcpUserServer extends Service {


    @Autowired
    MinaServerTcpUserConfig minaServerTcpUserConfig;

    @Autowired
    ThreadPoolExecutorTcpConfig threadPoolExecutorTcpConfig;

    @Autowired
    MailConfig mailConfig;

    @Autowired
    IUserScript iUserScript;

    private TcpServer tcpServer;

    private GateTcpUserServerHandler gateTcpUserServerHandler;

    @Autowired
    ITimerEventScript iTimerEventScript;


    private static final Map<String, IoFilter> filters = new HashMap<>();
    private static final BlacklistFilter blacklistFilter = new BlacklistFilter(); // IP黑名单过滤器


    @PostConstruct
    public void init() {
        filters.put("Blacklist", blacklistFilter);

        // IO默认线程池 客户端的请求,默认使用其执行
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorTcpConfig.newThreadPoolExecutor();
        this.serverThreads.put(ThreadType.IO, threadPoolExecutor);
        //全局sync线程
        ServerThread serverThreadSync = new ServerThread(new ThreadGroup("全局同步线程"),
                "全局同步线程:" + getClass().getSimpleName(), threadPoolExecutorTcpConfig.getHeart(),
                threadPoolExecutorTcpConfig.getCommandSize(), mailConfig);
        serverThreadSync.start();
        serverThreads.put(ThreadType.SYNC, serverThreadSync);

        gateTcpUserServerHandler = new GateTcpUserServerHandler(8, iUserScript);
        gateTcpUserServerHandler.setService(this);
        MinaServerTcpConfig minaServerTcpConfig = new MinaServerTcpConfig();
        BeanUtils.copyProperties(minaServerTcpUserConfig,minaServerTcpConfig);
        //初始化tcpServer 服务线程
        tcpServer = new TcpServer(minaServerTcpConfig, gateTcpUserServerHandler, filters);

        Config.SERVER_ID = minaServerTcpUserConfig.getId();

    }

    @Override
    protected void running() {
        log.info("start gate tcp server.......");
        tcpServer.run();
//        log.info("end gate user tcp port : {}",minaServerTcpUserConfig.getPort());
        log.warn("已开始监听网关用户 TCP端口：{}", minaServerTcpUserConfig.getPort());

        // 添加定时器 ,如果心跳配置为0，则没有定时器
        ServerThread syncThread = getExecutor(ThreadType.SYNC);
        if (syncThread != null) {
            syncThread.addTimerEvent(new ServerHeartTimer(iTimerEventScript));
        }
    }


    public static BlacklistFilter getBlacklistFilter() {
        return blacklistFilter;
    }

    @Override
    public void onShutDown() {
        UserSessionManager.getInstance().onShutdown();
        super.onShutDown();
    }
}
