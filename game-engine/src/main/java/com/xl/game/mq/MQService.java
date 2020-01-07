package com.xl.game.mq;

import com.xl.game.config.MQConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;

@Slf4j
public abstract class MQService {


    protected ActiveMQConnectionFactory activeMQConnectionFactory; // 连接工厂
    protected Connection connection; // 连接
    protected MQConfig mqConfig; // 配置

    public MQService(MQConfig mqConfig) {
        this.mqConfig = mqConfig;
        activeMQConnectionFactory = new ActiveMQConnectionFactory(mqConfig.getMqConnectionUrl());
    }


    /**
     * 获取连接
     *
     * @return
     * @author JiangZhiYong
     * @QQ 359135103
     * 2017年7月28日 下午1:38:28
     */
    public final Connection getConnection() {
        try {
            if (connection == null) {
                connection = activeMQConnectionFactory.createConnection(mqConfig.getUser(), mqConfig.getPassword());
            }
        } catch (Exception e) {
            log.error("MQ Connection", e);
            connection = null;
        }
        return connection;
    }

    /**
     * 关闭连接
     *
     * @author JiangZhiYong
     * @QQ 359135103
     * 2017年7月28日 下午1:38:20
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                log.error("closeConnection", e);
            }
            connection = null;
        }
    }


}
