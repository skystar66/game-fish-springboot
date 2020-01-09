package com.xl.game.bydr.handler.server;

import com.xl.game.bydr.manager.RoleManager;
import com.xl.game.bydr.struts.role.Role;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.ServerMessage;
import com.xl.game.model.constants.Reason;
import lombok.extern.slf4j.Slf4j;


/**
 * 改变服务器结果 - 跨服返回结果
 *
 * @author xuliang
 * @QQ 359135103 2020年1月7日 下午4:11:03
 */
@HandlerEntity(mid = Mid.MID.ChangeRoleServerRes_VALUE, msg = ServerMessage.ChangeRoleServerResponse.class)
@Slf4j
public class ChangeRoleServerResHandler extends TcpHandler {


    @Override
    public void run() {

        ServerMessage.ChangeRoleServerResponse res = getMsg();
        Role role = RoleManager.getInstance().getRole(rid);
        if (res.getResult() == 0 && role != null) {
            RoleManager.getInstance().quit(role, Reason.CrossServer);
        }
        log.info("角色{}跨服退出{}", rid, res.getResult());
    }
}
