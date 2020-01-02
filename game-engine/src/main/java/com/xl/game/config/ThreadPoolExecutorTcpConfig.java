package com.xl.game.config;

import com.xl.game.thread.IoThreadFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 线程池配置
 *
 * @author xuliang
 * @date 2019-12-06 QQ:2755055412
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "threadpool-tcp")
public class ThreadPoolExecutorTcpConfig {


    // 线程池名称
    private String name;

    // 核心线程池值
    private int corePoolSize = 20;

    // 线程池最大值
    private int maxPoolSize = 200;

    // 线程池保持活跃时间(秒)
    private long keepAliveTime = 30L;

    // 心跳间隔（大于0开启定时监测线程）
    private int heart;

    // 队列大小
    private int commandSize = 100000;

    public ThreadPoolExecutor newThreadPoolExecutor() throws RuntimeException {
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(commandSize), new IoThreadFactory());
    }


}
