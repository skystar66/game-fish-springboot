package com.xl.game.gate.server.handler;


import com.google.protobuf.Message;
import com.xl.game.gate.script.IUserScript;
import com.xl.game.gate.session.UserSession;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.mina.handler.ClientProtocolHandler;
import com.xl.game.model.constants.Reason;
import com.xl.game.server.Service;
import com.xl.game.util.Config;
import com.xl.game.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.ssl.SslFilter;
import org.springframework.stereotype.Component;

/**
 * 大厅tcp消息处理器
 *
 * @author xulaing
 * @date 2019-12-30 QQ:359135103
 */
@Slf4j
public class GateTcpUserServerHandler extends ClientProtocolHandler {


    private IUserScript iUserScript;


    public GateTcpUserServerHandler(int messageHeaderLength, IUserScript iUserScript) {
        super(messageHeaderLength);
        this.iUserScript = iUserScript;
    }


    @Override
    public void messageReceived(IoSession ioSession, Object obj) throws Exception {
        super.messageReceived(ioSession, obj);
    }

    @Override
    protected void forward(IoSession session, int msgID, byte[] bytes) {
        if (msgID < Config.HALL_MAX_MID) {
            //转发到大厅服
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
     * @author xuliang
     * @QQ 359135103 2019年12月30日 上午10:14:44
     */
    private void forwardToHall(IoSession session, int msgID, byte[] bytes) {
        Object attribute = session.getAttribute(Config.USER_SESSION);
        if (attribute != null) {
            UserSession userSession = (UserSession) attribute;
            if (userSession.getRoleId() > 0) {
                if (!userSession.sendToHall(MsgUtil.clientToGame(msgID, bytes))) {
                    log.warn("角色[{}]没有连接大厅服务器", userSession.getRoleId());
                    return;
                } else {
                    return;
                }
            }
        }
    }


    @Override
    public Service getService() {
        return super.getService();
    }

    @Override
    public void messageHandler(IoSession session, Message message, TcpHandler tcpHandler, HandlerEntity handlerEntity) {
        super.messageHandler(session, message, tcpHandler, handlerEntity);
    }

    @Override
    public void setService(Service service) {
        super.setService(service);
    }

    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        super.sessionCreated(ioSession);

        //session认证
//        if (Config.USE_SSL) {
//            try {
//                SslFilter sslFilter = new SslFilter(GateSslContextFactory.getInstance(true));
////				sslFilter.setNeedClientAuth(true);
//                session.getFilterChain().addFirst("SSL", sslFilter);
//            } catch (Exception e) {
//                LOGGER.error("创建ssl", e);
//                throw new RuntimeException(e);
//            }
//        }


    }

    @Override
    public void sessionOpened(IoSession ioSession) throws Exception {
        super.sessionOpened(ioSession);

        UserSession userSession = new UserSession(ioSession);
        ioSession.setAttribute(Config.USER_SESSION, userSession);

    }

    @Override
    public void sessionClosed(IoSession ioSession) {
        super.sessionClosed(ioSession);
        iUserScript.quit(ioSession, Reason.SessionClosed);
    }

    @Override
    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {
        super.sessionIdle(ioSession, idleStatus);
        iUserScript.quit(ioSession, Reason.SessionIdle);

    }
}
