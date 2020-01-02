package com.xl.game.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * mina 基本配置
 *
 * @author xuliang
 * @date 2019-12-06
 * QQ:2755055412
 */
@Data
public class BaseServerConfig {


    // 这3个是必须设置的
    // 服务器标识
    private int id;

    // 服务器名称
    private String name = "无";

    // 服务器渠道
    private String channel;

    // 服务器版本
    private String version = "0.0.1";

    // 接收数据缓冲大小
    private int receiveBufferSize = 1048576;

    // 发送数据缓冲大小
    private int sendBufferSize = 1048576;

    // 接收数据最大字节数
    private int maxReadSize = 1048576;

    // 发送数据缓冲消息数
    private int maxScheduledWriteMessages = 1024;

}
