package com.xl.game.hall.handler.packet;

import com.xl.game.hall.manager.PacketManager;
import com.xl.game.hall.manager.RoleManager;
import com.xl.game.hall.script.IPacketScript;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.hall.HallPacketMessage;
import com.xl.game.model.struct.Role;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 背包物品列表
 *
 * @author xuliang
 * @QQ 359135103 2017年9月18日 下午3:26:33
 */
@HandlerEntity(mid = Mid.MID.PacketItemsReq_VALUE,
        msg = HallPacketMessage.PacketItemsRequest.class)
@Slf4j
public class PacketItemsHandler extends TcpHandler {


    private IPacketScript packetScript;

    @Override
    public void run() {
        Role role = RoleManager.getInstance().getRole(rid);
        if (role == null) {
            log.warn("角色{}未登陆", rid);
            return;
        }

        if (packetScript == null) {
            packetScript = SpringUtil.getBean(IPacketScript.class);
        }

        HallPacketMessage.PacketItemsResponse.Builder builder
                = HallPacketMessage.PacketItemsResponse.newBuilder();
        role.getItems().forEach((key, value) -> {
            builder.addItems(PacketManager.getInstance(packetScript).buildPacketItem(value));
        });
        sendIdMsg(builder.build());


    }
}
