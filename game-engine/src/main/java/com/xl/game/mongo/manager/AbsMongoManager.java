package com.xl.game.mongo.manager;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.xl.game.config.MongoClientConfig;
import com.xl.game.util.SpringUtil;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mongodb 管理类
 *
 * @author JiangZhiYong
 * @date 2017-04-14 QQ:359135103
 */
public abstract class AbsMongoManager {

    static Logger log = LoggerFactory.getLogger(AbsMongoManager.class);

    private MongoClient mongoClient;
    private Morphia morphia;
    public MongoClientConfig mongoConfig;

    /**
     * 创建mongodb连接
     *
     * @param configPath
     */
    public void createConnect(MongoClientConfig mongoConfig) {

        this.mongoConfig = mongoConfig;
        MongoClientURI uri = new MongoClientURI(mongoConfig.getUrl());
        mongoClient = new MongoClient(uri);
        morphia = new Morphia();
        morphia.mapPackage("");

        initDao();
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public Morphia getMorphia() {
        return morphia;
    }

    public MongoClientConfig getMongoConfig() {
        return mongoConfig;
    }

    /**
     * 初始化dao
     */
    protected abstract void initDao();

}
