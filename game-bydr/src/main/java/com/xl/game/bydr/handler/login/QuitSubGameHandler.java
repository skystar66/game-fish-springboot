package com.xl.game.bydr.handler.login;

import com.xl.game.bydr.manager.RoleManager;
import com.xl.game.bydr.manager.RoomManager;
import com.xl.game.bydr.struts.role.Role;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.hall.HallLoginMessage;
import com.xl.game.model.constants.Reason;
import lombok.extern.slf4j.Slf4j;


/**
 * 退出子游戏 TODO 数据持久化等处理
 *
 * @author xuliang
 * @QQ 359135103 2017年7月26日 下午5:34:06
 */
@HandlerEntity(mid = Mid.MID.QuitSubGameReq_VALUE, msg = HallLoginMessage.QuitSubGameRequest.class)
@Slf4j
public class QuitSubGameHandler extends TcpHandler {


    @Override
    public void run() {
        HallLoginMessage.QuitSubGameRequest req = getMsg();
        log.info("{}退出捕鱼", getRid());
        Role role = RoleManager.getInstance().getRole(getRid());
        if (role == null) {
            return;
        }
        // 退出房间
        if (role.getRoomId() > 0) {
            RoomManager.getInstance().quitRoom(role, role.getRoomId());
        }
        RoleManager.getInstance().quit(role, Reason.UserQuit);
        HallLoginMessage.QuitSubGameResponse.Builder builder = HallLoginMessage.QuitSubGameResponse.newBuilder();
        builder.setResult(0);
        sendIdMsg(builder.build());
    }
}
