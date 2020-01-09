package com.xl.game.bydr.script;


import com.xl.game.bydr.struts.room.Room;

/**
 * 鱼脚本
 *
 * @author xuliang
 * @date 2017-04-24 QQ:359135103
 */
public interface IFishScript {


    /**
     * 刷新鱼
     *
     * @param room
     */
    default void fishRefresh(Room room) {

    }

    /**
     * 鱼死亡
     *
     * @param room
     */
    default void fishDie(Room room) {

    }


}
