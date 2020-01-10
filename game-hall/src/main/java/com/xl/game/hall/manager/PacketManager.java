package com.xl.game.hall.manager;


import com.xl.game.hall.script.IPacketScript;
import com.xl.game.message.hall.HallPacketMessage;
import com.xl.game.model.constants.Reason;
import com.xl.game.model.struct.Item;
import com.xl.game.model.struct.Role;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * 背包
 *
 * @author xuliang
 * @QQ 359135103 2020年1月5日 下午2:49:17
 */
@Slf4j
public class PacketManager {

    private static volatile PacketManager packetManager;

    private IPacketScript useItem;
    private IPacketScript addItem;


    private PacketManager() {
        this.useItem = (IPacketScript) SpringUtil.getBean("userItem");
        this.addItem = (IPacketScript) SpringUtil.getBean("addItem");
    }

    public static PacketManager getInstance() {
        if (packetManager == null) {
            synchronized (PacketManager.class) {
                if (packetManager == null) {
                    packetManager = new PacketManager();
                }
            }
        }
        return packetManager;
    }

    /**
     * 使用道具
     *
     * @param id
     * @param num
     * @param reason
     * @param itemConsumer
     * @author XULIANG
     * @QQ 359135103 2017年9月18日 下午4:25:54
     */

    public void useItem(Role role, long id, int num, Reason reason,
                        Consumer<Item> itemConsumer) {
        useItem.useItem(role, id, num, reason, itemConsumer);
    }

    /**
     * 添加道具
     *
     * @param configId
     * @param num          数量
     * @param reason
     * @param itemConsumer
     * @author JiangZhiYong
     * @QQ 359135103 2017年9月18日 下午4:23:47
     */
    public Item addItem(Role role, int configId, int num, Reason reason, Consumer<Item> itemConsumer) {
        return addItem.addItem(role, configId, num, reason, itemConsumer);
    }


    /**
     * 构建
     *
     * @param item
     * @return
     * @author JiangZhiYong
     * @QQ 359135103 2017年9月18日 下午4:07:49
     */
    public HallPacketMessage.PacketItem buildPacketItem(Item item) {
        HallPacketMessage.PacketItem.Builder builder = HallPacketMessage.PacketItem.newBuilder();
        builder.setId(item.getId());
        builder.setConfigId(item.getConfigId());
        builder.setNum(item.getNum());
        return builder.build();
    }

    /**
     * 获取物品
     *
     * @param rid
     * @param itemId
     * @return
     * @author JiangZhiYong
     * @QQ 359135103 2017年9月18日 下午5:10:36
     */
    public Item getItem(Role role, long itemId) {
        return role.getItem(itemId);
    }


}
