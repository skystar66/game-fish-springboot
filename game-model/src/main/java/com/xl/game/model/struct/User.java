/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xl.game.model.struct;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * 用戶
 * 
 * @author JiangZhiYong
 * @QQ 359135103
 */
@Entity(value = "user", noClassnameStored = true)
@Data
public class User {

	/**
	 * 用户ID
	 */
	@Id
	@JSONField
	private long id;

	/** 账号 */
	@JSONField
	private String accunt;

	/** 密码 */
	@JSONField
	private String password;

	/** 注册IP地址 */
	private String ip;


	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
