package com.xl.game.gate.handler.role;

import com.xl.game.gate.manager.UserSessionManager;
import com.xl.game.gate.session.UserSession;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.hall.HallLoginMessage;
import lombok.extern.slf4j.Slf4j;


/**
 * 退出子游戏返回
 *
 * @author xuliang
 * @QQ 359135103 2017年10月20日 下午2:06:00
 */
@HandlerEntity(mid = Mid.MID.QuitSubGameRes_VALUE,
        msg = HallLoginMessage.QuitSubGameResponse.class)
@Slf4j
public class QuitSubGameResHandler extends TcpHandler {


    @Override
    public void run() {


        log.info("退出子游戏处理器。。。。。。");

        HallLoginMessage.QuitSubGameResponse res = getMsg();
        UserSession userSession=UserSessionManager.getInstance().getUserSessionbyRoleId(rid);
        if(userSession==null) {
//			LOGGER.warn("角色{}会话已遗失",rid);
            return ;
        }

//		LOGGER.info("角色{}退出：{}", userSession.getRoleId(), userSession.getServerType().toString());
        if (userSession.getClientSession() != null) {
            userSession.sendToClient(res);
            userSession.removeGame();
        }
    }
}
