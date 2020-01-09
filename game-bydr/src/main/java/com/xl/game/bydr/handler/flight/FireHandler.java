package com.xl.game.bydr.handler.flight;

import com.xl.game.bydr.manager.RoomManager;
import com.xl.game.bydr.struts.role.Role;
import com.xl.game.bydr.struts.room.Room;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.bydr.BydrFightMessage;
import com.xl.game.model.constants.Reason;
import com.xl.game.thread.ThreadType;
import lombok.extern.slf4j.Slf4j;


/**
 * 开炮
 *
 * @author xuliang
 * @date 2017-04-21 QQ:359135103
 */
@HandlerEntity(mid = Mid.MID.FireReq_VALUE, msg = BydrFightMessage.FireRequest.class, thread = ThreadType.ROOM)
@Slf4j
public class FireHandler extends TcpHandler {


    @Override
    public void run() {


        log.info("开炮=====");


        BydrFightMessage.FireRequest req = getMsg();
        // LOGGER.info(req.toString());
        Role role = getPerson();

        Room room = RoomManager.getInstance().getRoom(role.getRoomId());
//		if (room == null) {
//			LOGGER.warn("{}所在房间{}未找到", role.getNick(), role.getRoomId());
//			sendServerMsg(ServerMsgId.login_state_lost);
//			return;
//		}
//		ConfigRoom cRoom = ConfigManager.getInstance().getConfigRoom(room.getType());
//
//		if (req.getGold() < cRoom.getMinGold() || req.getGold() > cRoom.getMaxGold()
//				|| req.getGold() % cRoom.getMinGold() != 0) {
//			//sendServerMsg("请求金币非法");
//			LOGGER.warn("房间{} {}_{}请求金币{}非法", room.getType(), role.getNick(), getRemoteAddr(), req.getGold());
//			return;
//		}

        if (role.getGold() < req.getGold()) {
//			sendServerMsg(ServerMsgId.not_enough_gold);
            return;
        }

        role.getFireGolds().add(req.getGold());
        role.changeGold(-req.getGold(), Reason
                .RoleFire);
        role.addBetGold(req.getGold());
        role.setFireTime(System.currentTimeMillis());
        room.addAllFireCount();
        role.addFireCount();


        BydrFightMessage.FireResponse.Builder builder = BydrFightMessage.FireResponse.newBuilder();
        builder.setRid(role.getId());
        builder.setGold(req.getGold());
        builder.setAngleX(req.getAngleX());
        builder.setAngleY(req.getAngleY());
        builder.setTargetFishId(req.getTargetFishId());
        BydrFightMessage.FireResponse response = builder.build();
        room.getRoles().values().forEach(roomRole -> {
            roomRole.sendMsg(response);
        });

    }
}
