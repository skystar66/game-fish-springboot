package com.xl.game.bydr.handler.flight;

import com.xl.game.bydr.manager.RoomManager;
import com.xl.game.bydr.struts.fish.Fish;
import com.xl.game.bydr.struts.role.Role;
import com.xl.game.bydr.struts.room.Room;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.bydr.BydrFightMessage;
import com.xl.game.model.constants.Reason;
import com.xl.game.thread.ThreadType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * 使用技能
 *
 * @author xuliang
 * @date 2017-04-21 QQ:359135103
 */
@HandlerEntity(mid = Mid.MID.FireResultReq_VALUE,
        msg = BydrFightMessage.FireResultRequest.class,
        thread = ThreadType.ROOM)
@Slf4j
public class FireResultHandler extends TcpHandler {

    @Override
    public void run() {

        log.info("使用技能=====");

        BydrFightMessage.FireResultRequest req = getMsg();
        // LOGGER.info(req.toString());
        Role role = getPerson();


        Room room = RoomManager.getInstance().getRoom(role.getRoomId());

        if (room == null) {
            log.info("{}所在房间{}未找到", role.getNick(), role.getRoomId());
            // sendServerMsg("您已退出房间");
            return;
        }
        if (role.getFireGolds().size() < 1 || !role.getFireGolds().contains(req.getFireGold())) {
            // sendServerMsg("请发射炮弹");
            log.info("{}  未发射炮弹{}，请求结算{}", role.getNick(), req.getFireGold(), role.getFireGolds().toString());
            return;
        }


        if (req.getTargetFishIdList() == null) {
            return;
        }
        BydrFightMessage.FireResultResponse.Builder builder = BydrFightMessage.FireResultResponse.newBuilder();
        builder.addAllDieFishId(req.getTargetFishIdList());

        room.addFireResultCount();


        role.getFireGolds().remove((Integer) req.getFireGold());
        if (builder.getDieFishIdCount() > 0) {
            getAward(role, room, builder, req.getFireGold());
            builder.setGold(role.getGold());
            builder.setSpecialFishId(req.getSpecialFishId());
            builder.setRid(role.getId());
            builder.setAccumulateGold(0);
            BydrFightMessage.FireResultResponse response = builder.build();
            room.getRoles().values().forEach(roomRole -> {
                role.sendMsg(response);
            });
        }


    }


    /**
     * 领取奖励
     *
     * @param role
     * @param room
     * @param dieFishs
     * @return -1 获得累积奖
     */
    private int getAward(Role role, Room room, BydrFightMessage.FireResultResponse.Builder builder, int fireGold) {


        List<Long> dieFishs = builder.getDieFishIdList();
        int result = 0;
        if (dieFishs.size() < 1) {
            return 0;
        }
        for (int i = 0; i < dieFishs.size(); i++) {
            Fish fish = room.getFishMap().get(dieFishs.get(i));
            if (fish == null) {
                // LOGGER.debug("房间{}—{}未找到，结算奖励失败", room.getType(),
                // room.getNum());
                continue;
            }
            if (i == 0) { // 统计打死鱼
                role.addFishDiesCount(fish.getConfigId());
            }

            fishDie(room, fish.getId());
        }
        builder.setMultiple(0);

        role.changeGold(fireGold * 2, Reason.RoleFire);

        return result;


    }


    @SuppressWarnings({"unchecked"})
    private void fishDie(Room room, long fishId) {
        Fish fish = room.getFishMap().remove(fishId);
        if (fish == null) {
            return;
        }
    }


}
