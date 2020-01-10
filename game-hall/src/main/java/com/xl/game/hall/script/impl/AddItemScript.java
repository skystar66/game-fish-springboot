package com.xl.game.hall.script.impl;

import com.xl.game.hall.script.IPacketScript;
import com.xl.game.model.constants.Reason;
import com.xl.game.model.struct.Item;
import com.xl.game.model.struct.Role;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * 添加道具
 * @author JiangZhiYong
 * @QQ 359135103
 * 2017年9月18日 下午4:58:18
 */
@Slf4j
@Component(value = "addItem")
public class AddItemScript implements IPacketScript {

	@Override
	public Item addItem(Role role, int configId, int num, Reason reason, Consumer<Item> itemConsumer) {
		// TODO 具体逻辑,叠加方式等？
		Item item=new Item();
		item.setConfigId(configId);
		item.setNum(item.getNum()+num);
		item.saveToRedis(role.getId());
		return item;
	}

	
}
