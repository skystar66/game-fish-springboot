package com.xl.game.hall.script.impl;

import com.xl.game.hall.manager.PacketManager;
import com.xl.game.hall.script.IPacketScript;
import com.xl.game.message.hall.HallPacketMessage;
import com.xl.game.model.constants.Reason;
import com.xl.game.model.struct.Item;
import com.xl.game.model.struct.Role;
import com.xl.game.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * 使用道具脚本
 *
 * @author JiangZhiYong
 * @QQ 359135103
 * 2017年9月18日 下午4:55:43
 */
public class UseItemScript implements IPacketScript {
    private static final Logger LOGGER = LoggerFactory.getLogger(UseItemScript.class);


    private IPacketScript packetScript;

    @Override
    public void useItem(Role role, long itemId, int num, Reason reason, Consumer<Item> itemConsumer) {

        if (packetScript == null) {
            packetScript = SpringUtil.getBean(IPacketScript.class);
        }

        Item item = PacketManager.getInstance(packetScript).getItem(role, itemId);
        if (item == null) {
            return;
        }
        if (item.getNum() < num) {
            LOGGER.warn("道具数量{}，请求{}", item.getNum(), num);
            return;
        }

        item.setNum(item.getNum() - num);
        item.saveToRedis(role.getId());
        HallPacketMessage.UseItemResponse.Builder builder = HallPacketMessage.UseItemResponse.newBuilder();
        builder.setResult(0);
        role.sendMsg(builder.build());
    }

}
