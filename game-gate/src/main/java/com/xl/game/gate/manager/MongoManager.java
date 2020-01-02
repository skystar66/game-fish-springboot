package com.xl.game.gate.manager;

import com.xl.game.config.MongoClientConfig;
import com.xl.game.model.mongo.dao.HallInfoDao;
import com.xl.game.model.mongo.dao.RoleDao;
import com.xl.game.model.mongo.dao.UserDao;
import com.xl.game.mongo.manager.AbsMongoManager;

public class MongoManager extends AbsMongoManager {


    private static final MongoManager INSTANCE_MANAGER = new MongoManager();

    private MongoManager() {

    }

    public static MongoManager getInstance() {
        return INSTANCE_MANAGER;
    }

    @Override
    protected void initDao() {

        HallInfoDao.getDB(INSTANCE_MANAGER);
        UserDao.getDB(INSTANCE_MANAGER);
        RoleDao.getDB(INSTANCE_MANAGER);

    }
}
