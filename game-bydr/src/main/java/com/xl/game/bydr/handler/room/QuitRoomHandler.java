package com.xl.game.bydr.handler.room;

import com.xl.game.bydr.manager.RoleManager;
import com.xl.game.bydr.manager.RoomManager;
import com.xl.game.bydr.struts.role.Role;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.bydr.BydrRoomMessage;
import lombok.extern.slf4j.Slf4j;


/**
 * 退出房间
 *
 * @author xuliang
 * @QQ 359135103 2017年7月5日 下午5:35:36
 */
@HandlerEntity(mid=Mid.MID.QuitRoomReq_VALUE,msg=BydrRoomMessage.QuitRoomRequest.class)
@Slf4j
public class QuitRoomHandler extends TcpHandler {


    @Override
    public void run() {

        BydrRoomMessage.QuitRoomRequest request = getMsg();

        Role role = RoleManager.getInstance().getRole(this.rid);
        if (role == null) {
            log.warn("角色{}未登陆", rid);
            return;
        }
        RoomManager.getInstance().quitRoom(role,role.getRoomId());

    }
}
