package com.xl.game.bydr.script.impl;

import com.xl.game.bydr.script.IRoomScript;
import com.xl.game.bydr.struts.room.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * 定时清理房间死亡的鱼
 *
 * @author JiangZhiYong
 * @QQ 359135103 2017年9月14日 上午10:08:12
 */
@Component(value = "roomFishDieScript")
@Slf4j
public class RoomFishDieScript implements IRoomScript {

    @Override
    public void secondHandler(Room room, LocalTime localTime) {

        if (localTime.getSecond() % 3 != 0) {    //每隔三秒清理一次
            return;
        }
        if (room.getFishMap().size() > 1000) {
            room.getFishMap().clear();
        }


    }
}
