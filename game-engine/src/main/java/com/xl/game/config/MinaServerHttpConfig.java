package com.xl.game.config;

import com.xl.game.engine.ServerType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mina-http")
public class MinaServerHttpConfig extends BaseServerConfig {


    // mina地址
    private String ip;

    // mina端口
    private int port = 8500;


    // http服务器地址
    private String url;

    // mina服务器线程池大小
    private int orderedThreadPoolExecutorSize = 300;

    // 是否重用地址
    private boolean reuseAddress = true;

    // Tcp没有延迟
    private boolean tcpNoDelay = true;

    // 读取空闲时间检测
    private int readerIdleTime = 120;

    // 写入空闲时间检测
    private int writerIdleTime = 120;

    private int soLinger;

    // 服务器类型
    private ServerType type = ServerType.GATE;

    //http服务器端口
    private int httpPort;

    //网络带宽：负载均衡时做判断依据。以1M支撑64人并发计算
    private int netSpeed = 64 * 5;

    //限制每秒会话可接受的消息数，超过则关闭
    private int maxCountPerSecond = 30;


}
