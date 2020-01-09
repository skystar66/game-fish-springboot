package com.xl.game.bydr.script.impl;

import com.xl.game.bydr.manager.RoomManager;
import com.xl.game.bydr.script.IRoomScript;
import com.xl.game.bydr.server.BydrServer;
import com.xl.game.bydr.struts.role.Role;
import com.xl.game.bydr.struts.room.ClassicsRoom;
import com.xl.game.bydr.struts.room.Room;
import com.xl.game.bydr.thread.RoomExecutor;
import com.xl.game.message.bydr.BydrRoomMessage;
import com.xl.game.thread.ThreadType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.*;


/**
 * 进入房间脚本
 *
 * @author xuliang
 * @QQ 359135103 2017年9月14日 上午10:09:16
 */
@Component(value = "enterRoom")
@Slf4j
public class EnterRoomScript implements IRoomScript {


    private List<Integer> seats = new ArrayList<>(Arrays.asList(1, 2, 3, 4)); // 座位列表


    @Autowired
    BydrServer bydrServer;

    @Override
    public void enterRoom(Role role, Room room) {


        room.getRoomThread().execute(() -> {
            if (room.getRoles().containsValue(role)) {
                log.debug("角色{}已经在房间中", role.getNick());
                return;
            }
            //分配座位号
            int seatNum = randomSeat(room);
            room.getRoles().put(seatNum, role);
            role.setSeatNo(seatNum);
            role.setRoomId(room.getId());
            BydrRoomMessage.EnterRoomResponse.Builder builder =
                    BydrRoomMessage.EnterRoomResponse.newBuilder();
            builder.setResult(0);
            builder.setRoomInfo(RoomManager.getInstance().buildRoomInfo(room));
            room.getRoles().values().forEach(r -> {
                builder.addRoleInfo(RoomManager.getInstance().buildRoomRoleInfo(r));
            });
            role.sendMsg(builder.build());
        });

    }

    /**
     * 查找匹配房间
     */

    @Override
    public void enterRoom(Role role, BydrRoomMessage.RoomType roomType, int rank) {

        List<Room> roomList = RoomManager.getInstance().getRooms(roomType);
        Room room = null;
        Optional<Room> roomOption = roomList.stream().filter(r -> r.getRank() == rank && r.getRoles().size() < 4).findAny();
        if (roomOption.isPresent()) {
            room = roomOption.get();
        } else {
            switch (roomType) {
                case CLASSICS://经典场
                    room = new ClassicsRoom();
                    room.setRank(rank);
                    break;
                case ARENA://竞技场
                    break;
                default:
                    break;
            }

            RoomManager.getInstance().addRoom(room);
            //todo 稍后添加
            RoomExecutor roomExecutor = bydrServer.getExecutor(ThreadType.ROOM);
            if (roomExecutor != null) {
                roomExecutor.addRoom(room);
            }
        }
        enterRoom(role, room);
    }


    /**
     * 随机一个空闲座位号
     *
     * @param room
     * @return
     * @note 并未真正随机
     */

    public int randomSeat(Room room) {
        if (room.getRoles().size() < 1) {
            return 1;
        }
        Set<Integer> seatSet = room.getRoles().keySet();
        for (int i = 1; i <= seats.size(); i++) {
            if (!seatSet.contains(i)) {
                return i;
            }
        }
        log.warn("房间{}_{}未找到空位", room.getType());
        return 0;
    }

}
