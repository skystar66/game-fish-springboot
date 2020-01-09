package com.xl.game.model.redis.key;

/**
 * 捕鱼达人redis数据key枚举
 *
 * @author xuliang
 * @mail 359135103@qq.com
 */
public enum BydrKey {

	/** 角色基本信息 */
	Team_Map("Bydr:Team:Map"),
	/**角色信息*/
	Role_Map("Bydr:Role_%d:Map")
	;

	private final String key;

	BydrKey(String key) {
		this.key = key;
	}

	public String getKey(Object... objects) {
		return String.format(key, objects);
	}
}
