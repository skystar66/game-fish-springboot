package com.xl.game.gate.handler.chat;


import com.xl.game.gate.manager.UserSessionManager;
import com.xl.game.gate.session.UserSession;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.hall.HallChatMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * 聊天消息返回
 *
 * @author xuliang
 * @QQ 359135103
 * 2019年12月30日 下午3:49:36
 */
@HandlerEntity(mid = Mid.MID.ChatRes_VALUE, msg = HallChatMessage.ChatResponse.class)
@Slf4j
public class ChatResHandler extends TcpHandler {
    @Override
    public void run() {
        log.info("网关返回聊天消息");
        HallChatMessage.ChatResponse res = getMsg();
        switch (res.getChatType()) {
            case PRIVATE: //在当前网关转发
                UserSession userSession = UserSessionManager.getInstance().getUserSessionbyRoleId(this.rid);
                if (userSession != null) {
                    userSession.sendToClient(res);
                }
                break;
            case PMD:
                UserSessionManager.getInstance().broadcast(res);
                break;
            case GUILD:
                break;
            case WORLD:
                break;
        }


    }
}
