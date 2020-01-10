package com.xl.game.hall.manager;

import com.xl.game.model.mongo.dao.HallInfoDao;
import com.xl.game.model.mongo.dao.MailDao;
import com.xl.game.model.mongo.dao.RoleDao;
import com.xl.game.model.mongo.dao.UserDao;
import com.xl.game.mongo.manager.AbsMongoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mongodb
 * 
 * @author xuliang
 * @QQ 359135103 2017年6月28日 下午3:33:14
 */
public class MongoManager extends AbsMongoManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoManager.class);
	private static final MongoManager INSTANCE_MANAGER = new MongoManager();

	public static MongoManager getInstance() {
		return INSTANCE_MANAGER;
	}

	@Override
	protected void initDao() {
		HallInfoDao.getDB(INSTANCE_MANAGER);
		UserDao.getDB(INSTANCE_MANAGER);
		RoleDao.getDB(INSTANCE_MANAGER);
		MailDao.getDB(INSTANCE_MANAGER);
	}

}
