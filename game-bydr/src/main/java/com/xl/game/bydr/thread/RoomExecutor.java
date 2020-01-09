package com.xl.game.bydr.thread;

import com.xl.game.bydr.manager.RoleManager;
import com.xl.game.bydr.struts.role.Role;
import com.xl.game.bydr.struts.room.Room;
import com.xl.game.bydr.thread.timer.RoomTimer;
import com.xl.game.config.MailConfig;
import com.xl.game.handler.TcpHandler;
import com.xl.game.thread.ExecutorPool;
import lombok.extern.slf4j.Slf4j;

import java.util.*;


/**
 * 逻辑执行线程池，将玩家的逻辑操作分配到同一个线程中执行，
 * 避免并发数据异常
 *
 * @author xuliang
 * @date 2020-01-8 QQ:359135103
 */
@Slf4j
public class RoomExecutor implements ExecutorPool {

    private static final ThreadGroup threadGroup
            = new ThreadGroup("房间线程组");


    private MailConfig mailConfig;

    public RoomExecutor(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }

    // key:房间ID
    private final Map<Long, RoomThread> roomThreads = new HashMap<>();

    /**
     * 房间线程
     */
    final List<RoomThread> threads = Collections.synchronizedList(new ArrayList<>());


    @Override
    public void stop() {

        threads.forEach(thread -> thread.stop(true));
    }

    public RoomThread getRoomThread(long roomId) {
        return roomThreads.get(roomId);
    }

    @Override
    public void execute(Runnable command) {

        if (command instanceof TcpHandler) {
            TcpHandler handler = (TcpHandler) command;
            Role role = RoleManager.getInstance().getRole(handler.getRid());
            if (role == null) {
                log.warn("角色{}不在线", handler.getRid());
                return;
            }
            handler.setPerson(role);

            RoomThread roomThread = roomThreads.get(role.getRoomId());
            if (roomThread == null) {
                log.warn("房间{}已经销毁", role.getRoomId());
                return;
            }
            roomThread.execute(handler);
        }
    }

    /**
     * 添加逻辑线程
     *
     * @return
     */

    public RoomThread addRoom(Room room) {

        if (roomThreads.containsKey(room.getId())) {
            log.warn("房间{}已在线程中", room.getId());
            return roomThreads.get(room.getId());
        }

        RoomThread roomThread = null;
        Optional<RoomThread> optRoomThread = threads.stream().filter(thread ->
                //todo 稍后添加
                thread.getRooms().size() < 4

        ).findAny();

        if (optRoomThread.isPresent()) {
            roomThread = optRoomThread.get();
            //将当前房间 添加到该线程中
            roomThread.getRooms().add(room);
            roomThread.getRoomTimer().addRoom(room);
        } else {
            roomThread = new RoomThread(threadGroup, room, mailConfig);
            threads.add(roomThread);
            roomThread.start();

            RoomTimer roomTimer = new RoomTimer(room, roomThread);
            roomThread.setRoomTimer(roomTimer);
            roomThread.addTimerEvent(roomTimer);
        }
        room.setRoomThread(roomThread);
        roomThreads.put(room.getId(), roomThread);
        return roomThread;

    }


    /***
     * 移除线程
     *
     * @param room
     * @return
     */


    public RoomThread removeRoom(Room room) {
        RoomThread roomThread = roomThreads.remove(room.getId());
        roomThread.getRooms().remove(room);
        roomThread.getRoomTimer().removeRoom(room);
        return roomThread;
    }


}
