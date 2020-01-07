package com.xl.game.mq;

import com.xl.game.config.MQConfig;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * MQ 消费者，监听器
 * 
 * @author JiangZhiYong
 * @QQ 359135103 2017年7月28日 上午10:37:44
 */
public class MQConsumer extends MQService implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(MQConsumer.class);

	private final String queueName; // 队列名称
	private boolean connected; // 是否连接

	IMQScript imqScript;

	public MQConsumer(MQConfig mqConfig,IMQScript imqScript) {
		super(mqConfig);
		this.imqScript=imqScript;
        queueName = mqConfig.getQueueName();
	}


	@Override
	public void run() {
		MessageConsumer consumer = null;
		while (true) {
			try {
				if (!connected) { // 连接
					Connection conn = getConnection();
					if (conn == null) {
						LOGGER.error("启动MQ失败，获取连接失败");
                        connected = false;
						Thread.sleep(3000);
						break;
					}
					conn.start();
					Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
					consumer = session.createConsumer(new ActiveMQQueue(queueName));
                    connected = true;
				} else if (consumer != null) { // 接收消息
					Message msg = consumer.receive();
					if (msg == null) {
						continue;
					}
					if (msg instanceof TextMessage) {
						String body = ((TextMessage) msg).getText();
						if (body == null) {
							continue;
						}
						imqScript.onMessage(body);
					} else {
						LOGGER.warn("不支持的消息：{}", msg.getClass().getName());
					}
				}
			} catch (Exception e) {
				LOGGER.error("消息接收", e);
                closeConnection();
                connected = false;
			}
		}
	}
	
	public void stop(){
        closeConnection();
	}

}
