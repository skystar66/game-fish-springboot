package com.xl.game.gate.server.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.xl.game.gate.script.IUserScript;
import com.xl.game.gate.session.UserSession;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.hall.HallLoginMessage;
import com.xl.game.mina.handler.DefaultProtocolHandler;
import com.xl.game.model.constants.Reason;
import com.xl.game.server.Service;
import com.xl.game.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

@Slf4j
public class GateWebSocketUserServerHandler extends DefaultProtocolHandler {


    private Service service;

    private IUserScript userScript;


    public GateWebSocketUserServerHandler(int messageHeaderLength, Service service,
                                          IUserScript userScript) {
        super(messageHeaderLength);
        this.service = service;
        this.userScript = userScript;
    }

    @Override
    protected void forward(IoSession session, int msgID, byte[] bytes) {

        // 转发到大厅服
        if (msgID < Config.HALL_MAX_MID) {
            forwardToHall(session, msgID, bytes);
            return;
        }
        // 转发到游戏服
        Object attribute = session.getAttribute(Config.USER_SESSION);
        if (attribute != null) {
            UserSession userSession = (UserSession) attribute;
            if (userSession.getRoleId() > 0) {
                if (userSession.sendToGame(bytes)) {
                    return;
                } else {
                    log.warn("角色[{}]没有连接游戏服务器,消息{}发送失败", userSession.getRoleId(), msgID);
                    return;
                }
            }

        }


        try {
            HallLoginMessage.LoginRequest loginRequest = HallLoginMessage.LoginRequest.newBuilder().mergeFrom(bytes, 0, bytes.length).build();
            log.info(loginRequest.toString());
        } catch (InvalidProtocolBufferException e) {
            log.error(e.getMessage(), e);
        }


    }


    /**
     * 消息转发到大厅服务器
     *
     * @param session
     * @param msgID
     * @param bytes
     * @author JiangZhiYong
     * @QQ 359135103 2017年7月21日 上午10:14:44
     */
    private void forwardToHall(IoSession session, int msgID, byte[] bytes) {
        Object attribute = session.getAttribute(Config.USER_SESSION);
        if (attribute != null) {
            UserSession userSession = (UserSession) attribute;
            if (userSession.getRoleId() > 0) {
                if (!userSession.sendToHall(bytes)) {
                    log.warn("角色[{}]没有连接大厅服务器", userSession.getRoleId());
                    return;
                } else {
                    return;
                }
            }
        }
        log.warn("[{}]消息未找到对应的处理方式", msgID);
    }


    @Override
    public Service getService() {
        return service;
    }


    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        UserSession userSession = new UserSession(session);
        session.setAttribute(Config.USER_SESSION, userSession);
    }

    @Override
    public void sessionClosed(IoSession ioSession) {
        super.sessionClosed(ioSession);
        userScript.quit(ioSession, Reason.SessionClosed);
    }

    @Override
    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {
        super.sessionIdle(ioSession, idleStatus);

        userScript.quit(ioSession, Reason.SessionClosed);

    }
}
