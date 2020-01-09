package com.xl.game.bydr.manager;


import com.xl.game.bydr.script.IRoomScript;
import com.xl.game.bydr.struts.fish.Fish;
import com.xl.game.bydr.struts.role.Role;
import com.xl.game.bydr.struts.room.ClassicsRoom;
import com.xl.game.bydr.struts.room.Room;
import com.xl.game.message.bydr.BydrRoomMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 房间管理类
 *
 * @author xuliang
 * @QQ 359135103 2017年9月14日 下午2:27:59
 */
@Slf4j
public class RoomManager {


    private static final Logger LOGGER = LoggerFactory.getLogger(RoomManager.class);
    private static volatile RoomManager roomManager;
    private final Map<Long, Room> rooms = new ConcurrentHashMap<Long, Room>();
    private final Map<BydrRoomMessage.RoomType, List<Room>> roomTypes = new ConcurrentHashMap<>();

    private IRoomScript enterRoom;
    private IRoomScript quitRoom;

    private RoomManager() {

    }

    public static RoomManager getInstance() {
        if (roomManager == null) {
            synchronized (RoomManager.class) {
                if (roomManager == null) {
                    roomManager = new RoomManager();
                }
            }
        }
        return roomManager;
    }


    public Map<Long, Room> getRooms() {
        return rooms;
    }

    public Map<BydrRoomMessage.RoomType, List<Room>> getRoomTypes() {
        return roomTypes;
    }

    /**
     * 进入房间
     *
     * @param role
     * @param roomType
     * @param rank
     * @author xuliang
     * @QQ 359135103 2017年9月14日 下午2:32:40
     */

    public void enterRoom(Role role, BydrRoomMessage.RoomType roomType, int rank) {

        enterRoom.enterRoom(role, roomType, rank);
    }


    /**
     * 添加房间
     *
     * @param room
     * @author xuliang
     * @QQ 359135103 2017年9月14日 下午2:45:19
     */

    public void addRoom(Room room) {
        rooms.put(room.getId(), room);
        if (room instanceof ClassicsRoom) {
            //经典场
            if (!roomTypes.containsValue(BydrRoomMessage.RoomType.CLASSICS)) {
                roomTypes.put(BydrRoomMessage.RoomType.CLASSICS, new ArrayList<Room>());
            }
            //将房间加入到经典场集合中
            roomTypes.get(BydrRoomMessage.RoomType.CLASSICS).add(room);
        }


    }

    /**
     * 退出房间
     *
     * @param role
     * @param roomId
     * @author xuliang
     * @QQ 359135103 2017年9月26日 下午4:08:55
     */

    public void quitRoom(Role role, long roomId) {
        Room room = getRoom(roomId);
        if (room == null) {
            return;
        }
        Thread currentThread = Thread.currentThread();
        if (currentThread.equals(room.getRoomThread())) {
            //如果当前线程是当前推出房间执行线程，则直接在该房间线程池内 执行
            quitRoom.quitRoom(role, room);
        } else {
            room.getRoomThread().execute(() -> {
                quitRoom.quitRoom(role, room);
            });
        }
    }


    /**
     * 获取房间
     *
     * @param roomId
     * @return
     * @author JiangZhiYong
     * @QQ 359135103 2017年9月14日 下午2:56:29
     */
    public Room getRoom(long roomId) {
        return rooms.get(roomId);
    }


    /**
     * 获取房间列表
     *
     * @param roomType
     * @return
     * @author xuliang
     * @QQ 359135103 2017年9月14日 下午3:11:11
     */

    public List<Room> getRooms(BydrRoomMessage.RoomType roomType) {
        if (roomTypes.containsKey(roomType)) {
            return roomTypes.get(roomType);
        }
        List<Room> list = new ArrayList<>();
        roomTypes.put(roomType, list);
        return list;

    }

    /**
     * 广播鱼进入房间
     *
     * @param room
     * @param fishs
     * @author xuliang
     * @QQ 359135103 2017年9月25日 下午4:13:23
     */


    public void broadcastFishEnter(Room room, Fish... fishs) {


    }


    /**
     * 构建房间信息
     *
     * @param room
     * @return
     * @author xuliang
     * @QQ 359135103
     * 2017年10月20日 上午10:51:45
     */

    public BydrRoomMessage.RoomInfo buildRoomInfo(Room room) {

        BydrRoomMessage.RoomInfo.Builder builder = BydrRoomMessage.RoomInfo.newBuilder();
        builder.setId(room.getId());
        builder.setType(room.getType());
        builder.setState(room.getState());
        return builder.build();
    }

    /**
     * 构建房间信息
     *
     * @param role
     * @return
     * @author xuliang
     * @QQ 359135103 2017年9月25日 下午5:59:28
     */

    public BydrRoomMessage.RoomRoleInfo buildRoomRoleInfo(Role role) {
        BydrRoomMessage.RoomRoleInfo.Builder builder = BydrRoomMessage.RoomRoleInfo.newBuilder();
        builder.setDiamond(role.getGem());
        builder.setGold(role.getGold());
        builder.setIcon(role.getHead() == null ? "" : role.getHead());
        builder.setNick(role.getNick() == null ? "" : role.getNick());
        builder.setLevel(role.getLevel());
        builder.setRid(role.getId());
        builder.setVip(role.isIs_vip());
        return builder.build();
    }


}
