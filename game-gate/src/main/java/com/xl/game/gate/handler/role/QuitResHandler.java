package com.xl.game.gate.handler.role;


import com.xl.game.gate.manager.UserSessionManager;
import com.xl.game.gate.session.UserSession;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.hall.HallLoginMessage;
import com.xl.game.model.constants.Reason;
import lombok.extern.slf4j.Slf4j;

/**
 * 退出游戏返回<br>
 *
 * @author xuliang
 * @note 在请求消息时已经移除了角色的连接会话信息, 所有返回消息会话是游戏内部会话，
 * 不能从session中获取属性,不能关闭
 * @QQ 359135103 2017年10月20日 下午2:18:08
 */
@HandlerEntity(mid = Mid.MID.QuitRes_VALUE, msg = HallLoginMessage.QuitResponse.class)
@Slf4j
public class QuitResHandler extends TcpHandler {


    @Override
    public void run() {

        log.info("退出游戏返回处理器。。。。。。");


        HallLoginMessage.QuitResponse res = getMsg();
        UserSession userSession = UserSessionManager.getInstance().getUserSessionbyRoleId(rid);

        if (userSession == null) {
            log.warn("角色{}会话已遗失", rid);
            return;
        }

        if (userSession.getClientSession() != null) {
            userSession.sendToClient(res);
            userSession.getClientSession().closeOnFlush();
            log.info("{}退出游戏", userSession.getRoleId());
        }

        //返回结果再移除，防止一些消息得不到转发失败
        UserSessionManager.getInstance().quit(userSession, Reason.UserQuit);

    }
}
