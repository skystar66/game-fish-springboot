package com.xl.game.hall.script;


import com.xl.game.model.constants.Reason;
import com.xl.game.model.struct.Item;
import com.xl.game.model.struct.Role;

import java.util.function.Consumer;

/**
 * 道具
 * @author JiangZhiYong
 * @QQ 359135103
 * 2017年9月18日 下午4:18:44
 */
public interface IPacketScript {

    /**
     * 使用道具
     * @author JiangZhiYong
     * @QQ 359135103
     * 2017年9月18日 下午4:20:16
     * @param id 道具Id
     * @param num 数量
     * @param itemConsumer
     */
    default void useItem(Role role, long id, int num, Reason reason, Consumer<Item> itemConsumer) {

    }

    /**
     * 添加道具
     * @author JiangZhiYong
     * @QQ 359135103
     * 2017年9月18日 下午4:23:47
     * @param configId
     * @param num 数量
     * @param reason
     * @param itemConsumer
     */
    default Item addItem(Role role,int configId,int num,Reason reason,Consumer<Item> itemConsumer) {
        return null;
    }




}
