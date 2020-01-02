package com.xl.game.model.mongo.dao;

import com.xl.game.model.struct.Guild;
import com.xl.game.mongo.manager.AbsMongoManager;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * 公会
 * @author JiangZhiYong
 * @QQ 359135103
 * 2017年9月22日 上午10:38:16
 */
public class GuildDao extends BasicDAO<Guild, Long>{
	private static volatile GuildDao guildDao;

	public GuildDao(AbsMongoManager mongoManager) {
		super(Guild.class, mongoManager.getMongoClient(), mongoManager.getMorphia(), mongoManager.getMongoConfig().getDbName());
	}
	
	public static GuildDao getDB(AbsMongoManager mongoManager) {
		if(guildDao==null) {
			synchronized (GuildDao.class) {
				if(guildDao==null) {
					guildDao=new GuildDao(mongoManager);
				}
			}
		}
		return guildDao;
	}
	
	/**
	 * 存储
	 * @author JiangZhiYong
	 * @QQ 359135103
	 * 2017年9月22日 上午10:45:52
	 * @param guild
	 */
	public static void saveGuild(Guild guild) {
		guildDao.save(guild);
	}

}
