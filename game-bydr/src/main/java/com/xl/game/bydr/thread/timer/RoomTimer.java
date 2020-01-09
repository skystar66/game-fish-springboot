package com.xl.game.bydr.thread.timer;

import com.xl.game.bydr.script.IFishScript;
import com.xl.game.bydr.script.IRoomScript;
import com.xl.game.bydr.struts.room.Room;
import com.xl.game.bydr.thread.RoomThread;
import com.xl.game.thread.timer.TimerEvent;
import com.xl.game.util.SpringUtil;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 房间定时器
 *
 * @author xuliang
 * @date 2017-04-25 QQ:359135103
 */
public class RoomTimer extends TimerEvent {

    final List<Room> rooms = Collections.synchronizedList(new ArrayList<>());
    private final RoomThread roomThread; // 依赖的房间线程

    private int hour = -1;
    private int sec = -1;
    private int min = -1;

    private IFishScript fishScript;
    private IRoomScript roomRoleUpdateScript;
    private IRoomScript roomFishDieScript;


    public RoomTimer(Room room, RoomThread roomThread) {
        super(Long.MAX_VALUE, -1);
        rooms.add(room);
        this.roomThread = roomThread;

        if (fishScript == null) {
            this.fishScript = SpringUtil.getBean(IFishScript.class);
        }

        if (roomRoleUpdateScript == null) {
            this.roomRoleUpdateScript = (IRoomScript) SpringUtil.getBean("roomRoleUpdateScript");
        }

        if (roomFishDieScript == null) {
            this.roomFishDieScript = (IRoomScript) SpringUtil.getBean("roomFishDieScript");
        }
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }
    public void removeRoom(Room room) {
        rooms.remove(room);
    }

    @Override
    public void run() {

        LocalTime localTime = LocalTime.now();
        int _hour = localTime.getHour();
        int _sec = localTime.getSecond();
        int _min = localTime.getMinute();

        if (null != rooms && !rooms.isEmpty()) {
            rooms.forEach(room -> {
                //鱼群刷新
                roomThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        fishScript.fishRefresh(room);
                    }
                });

                //每秒执行
                if (sec != _sec) {
                    sec = _sec;
                    roomThread.execute(() -> {
                        roomFishDieScript.secondHandler(room, localTime);
                        roomRoleUpdateScript.secondHandler(room, localTime);
                    });
                }
                //每分钟执行
                if (min != _min) {
                    min = _min;
                    room.getRoomThread().execute(() -> {
                        roomRoleUpdateScript.secondHandler(room, localTime);
                    });
                }

                //每小时执行
                if (hour != _hour) {
                    hour = _hour;
                    room.getRoomThread().execute(() -> {
                        roomRoleUpdateScript.secondHandler(room, localTime);
                    });
                }
            });
        }
    }
}
