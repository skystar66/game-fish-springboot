/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xl.game.model.mongo.dao;

import com.xl.game.model.struct.Role;
import com.xl.game.mongo.manager.AbsMongoManager;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * 角色
 *
 * @author JiangZhiYong
 * @QQ 359135103
 */
public class RoleDao extends BasicDAO<Role, Long> {

	private static volatile RoleDao roleDao;

	private RoleDao(AbsMongoManager mongoManager) {
		super(Role.class, mongoManager.getMongoClient(), mongoManager.getMorphia(),
				mongoManager.getMongoConfig().getDbName());
	}

	public static RoleDao getDB(AbsMongoManager mongoManager) {
		if (roleDao == null) {
			synchronized (RoleDao.class) {
				if (roleDao == null) {
					roleDao = new RoleDao(mongoManager);
				}
			}
		}
		return roleDao;
	}

	public static Role getRoleByUserId(long userId) {
		Role role = roleDao.createQuery().filter("userId", userId).get();
		return role;
	}

	public static void saveRole(Role role) {
		roleDao.save(role);
	}

}
