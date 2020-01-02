package com.xl.game.config;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "mongo")
public class MongoClientConfig {


    /**数据库名字*/
    private String dbName = "lztb_hall";
    /**数据库连接地址*/
    private String url="mongodb://127.0.0.1:27017/?replicaSet=rs_lztb";



}
