package com.xl.game.bydr.thread;

import com.xl.game.bydr.struts.room.Room;
import com.xl.game.bydr.thread.timer.RoomTimer;
import com.xl.game.config.MailConfig;
import com.xl.game.thread.ServerThread;
import com.xl.game.thread.ThreadType;
import com.xl.game.thread.timer.TimerEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 房间逻辑线程
 * <p>
 * 一个线程处理多个房间
 * </p>
 *
 * @author xuliang
 * @date 2017-03-24 QQ:359135103
 */
public class RoomThread extends ServerThread {


    private static final AtomicInteger threadNum = new AtomicInteger(0);
    private final List<Room> rooms = new ArrayList<>();
    private RoomTimer roomTimer;


    public RoomThread(ThreadGroup threadGroup, Room room, MailConfig mailConfig) {
        super(threadGroup, ThreadType.ROOM + "-" + threadNum.getAndIncrement(), 500, 10000, mailConfig); // TODO
        rooms.add(room);
    }


    public RoomTimer getRoomTimer() {
        return roomTimer;
    }

    public void setRoomTimer(RoomTimer roomFishTimer) {
        roomTimer = roomFishTimer;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Room getRoom(long roomId) {
        Optional<Room> findAny = rooms.stream().filter(r -> r.getId() == roomId).findAny();
        return findAny.orElse(null);
    }


}



