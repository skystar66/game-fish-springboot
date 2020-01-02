package com.xl.game.gate.server.handler;

import com.google.protobuf.Message;
import com.xl.game.gate.session.UserSession;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.mina.handler.ClientProtocolHandler;
import com.xl.game.server.Service;
import com.xl.game.util.Config;
import com.xl.game.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;


/**
 * udp消息处理器
 *
 * @author xuliang
 * @QQ 359135103
 * 2017年9月1日 下午2:06:36
 */

@Slf4j
public class GateUdpUserServerHandler extends ClientProtocolHandler {


    public GateUdpUserServerHandler(int messageHeaderLength) {
        super(messageHeaderLength);
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
                if (userSession.sendToGame(MsgUtil.clientToGame(msgID, bytes))) {
                    return;
                } else {
                    log.warn("角色[{}]没有连接游戏服务器,消息{}发送失败", userSession.getRoleId(), msgID);
                    return;
                }
            }
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
                if (!userSession.sendToHall(MsgUtil.clientToGame(msgID, bytes))) {
                    log.warn("角色[{}]没有连接大厅服务器", userSession.getRoleId());
                    return;
                }

            }
        }
        log.warn("[{}]消息未找到对应的处理方式", msgID);
    }


    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        super.sessionCreated(ioSession);
    }

    @Override
    public void sessionOpened(IoSession ioSession) throws Exception {
        super.sessionOpened(ioSession);
    }

    @Override
    public void sessionClosed(IoSession ioSession) {
        super.sessionClosed(ioSession);
    }

    @Override
    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {
        super.sessionIdle(ioSession, idleStatus);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
        super.exceptionCaught(session, throwable);
    }

    @Override
    public void messageSent(IoSession ioSession, Object o) throws Exception {
        super.messageSent(ioSession, o);
    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
        super.inputClosed(session);
    }

    @Override
    public void messageHandler(IoSession ioSession, HandlerEntity handlerEntity, Message message, TcpHandler handler) {
        super.messageHandler(ioSession, handlerEntity, message, handler);
    }

    @Override
    public Service getService() {
        return super.getService();
    }
}
