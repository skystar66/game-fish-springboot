package com.xl.game.config;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * mq配置
 *
 * @author xulaing
 * @date 2017-04-18
 * QQ:359135103
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "mq")
public class MQConfig {
	//用户名
	private String user="jzy";
	
	//密码
	private String password="111";
	
	//ip地址
	private String host="127.0.0.1";
	
	//端口
	private String port="61616";
	
	//协议类型
	private String protocol="tcp";
	
	//队列名称
	private String queueName="hall";

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	/**
	 * 连接地址
	 * @author JiangZhiYong
	 * @QQ 359135103
	 * 2017年7月28日 上午11:53:17
	 * @return
	 */
	public  String getMqConnectionUrl() {
		return protocol + "://" + getHost() + ":" + getPort();
	}
}
