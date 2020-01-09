package com.xl.game.bydr.handler.login;


import com.xl.game.bydr.manager.RoleManager;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.hall.HallLoginMessage;
import com.xl.game.model.constants.Reason;
import lombok.extern.slf4j.Slf4j;

/**
 * 捕鱼达人登录
 * TODO 此次全用的是session写逻辑，用netty需要使用channel
 *
 * @author xuliang
 * @QQ 359135103 2020年1月8日 下午3:17:01
 */
@HandlerEntity(mid = Mid.MID.LoginSubGameReq_VALUE, msg = HallLoginMessage.LoginSubGameRequest.class)
@Slf4j
public class BydrLoginHandler extends TcpHandler {


    @Override
    public void run() {

        HallLoginMessage.LoginSubGameRequest req = getMsg();

        switch (req.getType()) {
            case 0:
                //登录
                RoleManager.getInstance().login(this.rid, Reason.UserLogin, role -> {
                    role.setIoSession(getSession());
                    role.setChannel(getChannel());
                });
                break;
            case 1:
                //重连
                RoleManager.getInstance().login(this.rid, Reason.UserReconnect, role -> {
                    role.setIoSession(getSession());
                    role.setChannel(getChannel());
                });
                break;
            case 2:
                //跨服
                RoleManager.getInstance().login(req.getRid(), Reason.CrossServer, role -> {
                    role.setIoSession(getSession());
                    role.setChannel(getChannel());
                });
                break;
            default:
                break;

        }

        if (req.getType() == 2) {
            //跨服不返消息

            log.debug("角色[{}]跨服登录", req.getRid());
            return;
        }
        HallLoginMessage.LoginSubGameResponse.Builder builder = HallLoginMessage.LoginSubGameResponse.newBuilder();
        builder.setResult(1);
        sendIdMsg(builder.build());
    }
}
