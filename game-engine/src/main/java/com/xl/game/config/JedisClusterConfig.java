package com.xl.game.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;

/**
 *redis集群配置文件
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "redis")
public class JedisClusterConfig {


    private List<String> nodes;
    private int poolMaxTotal=500;
    private int poolMaxIdle=5;
    private int connectionTimeout=2000;
    private int soTimeout=2000;
    private int maxRedirections=6;
    private int timeBetweenEvictionRunsMillis=30000;
    private int minEvictableIdleTimeMillis=1800000;
    private int softMinEvictableIdleTimeMillis=1800000;
    private int maxWaitMillis=60000;
    private boolean testOnBorrow=true;
    private boolean testWhileIdle;
    private boolean testOnReturn;

}
