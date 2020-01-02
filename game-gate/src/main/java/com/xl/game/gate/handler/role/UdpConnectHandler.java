package com.xl.game.gate.handler.role;


import com.xl.game.gate.manager.UserSessionManager;
import com.xl.game.gate.session.UserSession;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.system.SystemMessage;
import com.xl.game.util.Config;
import com.xl.game.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 请求进行udp连接
 *
 * @author xuliang
 * @QQ 359135103 2017年9月1日 下午3:55:27
 */
@HandlerEntity(mid = Mid.MID.UdpConnectReq_VALUE, msg = SystemMessage.UdpConnectRequest.class)
@Slf4j
public class UdpConnectHandler extends TcpHandler {


    @Override
    public void run() {

        log.info("请求进行udp连接处理器。。。。。。");


        SystemMessage.UdpConnectRequest req = getMsg();
        UserSession userSession = UserSessionManager.getInstance().getUserSessionbyRoleId(req.getRid());
        UserSession userSession2 = UserSessionManager.getInstance().getUserSessionBySessionId(req.getSessionId());
        SystemMessage.UdpConnectResponse.Builder builder = SystemMessage.UdpConnectResponse.newBuilder();
        builder.setResult(0);
        if (userSession == null) {
            builder.setResult(1); //角色未登录
        } else if (!userSession.equals(userSession2)) {
            builder.setResult(2); //会话id与用户session不匹配
        }
        // 地址不正确
        if (!MsgUtil.getIp(session).equals(MsgUtil.getIp(userSession.getClientSession()))) {
            builder.setResult(3);
        }
        userSession.setClientUdpSession(getSession());
        session.setAttribute(Config.USER_SESSION, userSession);
        session.write(builder.build());
    }
}
