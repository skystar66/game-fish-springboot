package com.xl.game.bydr.script;


import com.xl.game.bydr.struts.role.Role;
import com.xl.game.bydr.struts.room.Room;
import com.xl.game.message.bydr.BydrRoomMessage;

import java.time.LocalTime;

/**
 * 房间脚本
 *
 * @author xuliang
 * @date 2020-01-8 QQ:359135103
 */
public interface IRoomScript {





    /**
     * 进入房间
     *
     * @param role
     * @param room
     * @return
     */
    default void enterRoom(Role role, Room room) {

    }

    /**
     * 进入房间
     *
     * @author JiangZhiYong
     * @QQ 359135103 2017年9月14日 下午2:42:25
     * @param role
     * @param roomType
     *            房间类型
     * @param rank
     *            级别
     */
    default void enterRoom(Role role, BydrRoomMessage.RoomType roomType, int rank) {

    }

    /**
     * 退出房间
     *
     * @param role
     * @param room
     */
    default void quitRoom(Role role, Room room) {

    }

    /**
     * 跑马灯
     * <p>
     * 没有辛运星
     * </p>
     *
     *
     * @param role
     * @param baseGold
     * @param accumulateGold
     */
    default void sendPmd(Role role, int totalGold, int accumulateGold, int multiple, String fishName) {

    }

    /**
     * 销毁房间
     *
     * @author xuliang
     * @QQ 359135103 2017年9月14日 上午9:47:12
     * @param room
     */
    default void destoryRoom(Room iRoom) {

    }

    /**
     * 每秒执行
     * @author xuliang
     * @QQ 359135103
     * 2017年9月26日 下午2:07:00
     * @param localTime
     */
    default void secondHandler(Room room,LocalTime localTime) {

    }

    /**
     * 每分钟执行
     * @author xuliang
     * @QQ 359135103
     * 2017年9月26日 下午1:51:45
     * @param localTime
     */
    default void minuteHandler(Room room,LocalTime localTime) {

    }

    /**
     * 每小时执行
     * @author xuliang
     * @QQ 359135103
     * 2017年9月26日 下午1:51:59
     * @param localTime
     */
    default void hourHandler(Room room,LocalTime localTime) {

    }



}
