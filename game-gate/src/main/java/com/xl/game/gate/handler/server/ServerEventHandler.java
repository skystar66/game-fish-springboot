package com.xl.game.gate.handler.server;

import com.xl.game.gate.manager.UserSessionManager;
import com.xl.game.gate.script.IUserScript;
import com.xl.game.gate.session.UserSession;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.ServerMessage;
import com.xl.game.model.constants.Reason;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 事件消息
 *
 * @author xuliang
 * @QQ 359135103
 * 2017年10月17日 下午5:05:36
 */
@HandlerEntity(mid = Mid.MID.ServerEventReq_VALUE, msg = ServerMessage.ServerEventRequest.class)
@Slf4j
public class ServerEventHandler extends TcpHandler {


    private IUserScript userScript;

    @Override
    public void run() {
        log.info("进入事件消息处理器。。。。。。");
        ServerMessage.ServerEventRequest request = getMsg();
        switch (request.getType()) {
            case 1:
                //gm踢玩家下线
                tickRole(request);
                break;
        }
        log.info("处理事件{}", request.toString());
    }


    /**
     * @param request
     * @author xuliang
     * @QQ 359135103
     * 2017年10月17日 下午5:08:15
     */
    public void tickRole(ServerMessage.ServerEventRequest request) {


        UserSession userSession = UserSessionManager.getInstance().getUserSessionbyRoleId(request.getId());
        if (userSession == null) {
            return;
        }

        if (userScript == null) {
            userScript = SpringUtil.getBean(IUserScript.class);
        }
        userScript.quit(userSession.getClientSession(), Reason.GmTickRole);

    }


}
