package com.xl.game.gate.script.impl;

import com.xl.game.gate.manager.UserSessionManager;
import com.xl.game.gate.script.IUserScript;
import com.xl.game.gate.session.UserSession;
import com.xl.game.message.hall.HallLoginMessage;
import com.xl.game.model.constants.Reason;
import com.xl.game.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;


/**
 * 角色退出
 * <p>TODO 需要向大厅服、游戏服广播,断线重连和玩家真实退出的不同处理</p>
 *
 * @author xuliang
 * @QQ 359135103
 * 2019年12月30日 下午4:51:31
 */
@Slf4j
@Service
public class UserQuitScript implements IUserScript {


    @Override
    public void quit(IoSession session, Reason reason) {
        Object attribute = session.getAttribute(Config.USER_SESSION);
        if (attribute == null) {
            log.warn("session 为空 ：{}", reason.toString());
            return;
        }

        UserSession userSession = (UserSession) attribute;

        //是否连接游戏服
        if (userSession.getGameSession() != null) {
            HallLoginMessage.QuitSubGameRequest.Builder builder = HallLoginMessage.QuitSubGameRequest.newBuilder();
            builder.setRid(userSession.getRoleId());
            userSession.sendToGame(builder.build());
            userSession.removeGame();
        }

        //是否连接大厅服
        if (userSession.getHallSession() != null) {

            HallLoginMessage.QuitRequest.Builder builder=HallLoginMessage.QuitRequest.newBuilder();
            builder.setRid(userSession.getRoleId());
            userSession.sendToHall(builder.build());
            userSession.removeHall();
        }

        if(Reason.SessionIdle==reason||Reason.GmTickRole==reason||Reason.ServerClose==reason){
            session.closeOnFlush();
            UserSessionManager.getInstance().quit(userSession, reason);
        }
    }
}
