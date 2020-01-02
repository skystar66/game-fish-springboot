package com.xl.game.config;

import com.xl.game.engine.ServerType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "mina-client")
@Data
@Configuration
public class MinaClientConfig extends BaseServerConfig{


    // 客户端线程池大小
    private int orderedThreadPoolExecutorSize = 150;

    private int soLinger;

    /***
     * 客户端创建的最大连接数
     */
    private int maxConnectCount = 1;

    //连接配置
    private MinaClienConnToConfig connTo;

    // 当前服务器的类型,如当前服务器是gameserver.那么对应ServerType.GameServer = 10
    private ServerType type = ServerType.GATE;

    // 其他配置,如配置服务器允许开启的地图
    private String info;

}
