package com.xl.game.mina.handler;

import com.google.protobuf.Message;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.IHandler;
import com.xl.game.handler.TcpHandler;
import com.xl.game.manager.tcp.TcpHandlerMsgManager;
import com.xl.game.server.Service;
import com.xl.game.util.MsgUtil;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * 默认消息处理器
 * <p>
 * 消息头+消息内容 <br>
 * 消息头可能有消息长度、消息ID、用户ID
 * </p>
 *
 * @author xuliang
 * @date 2017-03-30 QQ:359135103
 */
public abstract class DefaultProtocolHandler implements IoHandler {

    protected static final Logger log = LoggerFactory.getLogger(DefaultProtocolHandler.class);
    protected final int messageHeaderLength; // 消息头长度

    /**
     * @param messageHeaderLength 消息头长度
     */
    public DefaultProtocolHandler(int messageHeaderLength) {
        this.messageHeaderLength = messageHeaderLength;
    }

    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        log.info("已创建连接");
        
    }

    @Override
    public void sessionOpened(IoSession ioSession) throws Exception {
        log.info("已打开连接");
    }

    @Override
    public void sessionClosed(IoSession ioSession) {
        log.info("已关闭连接");
    }

    @Override
    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {
        log.info("连接{}处于空闲{}", ioSession, idleStatus);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
        log.error("连接{}异常：{}", session, throwable);
        session.closeNow();
    }

    @Override
    public void messageReceived(IoSession ioSession, Object obj) throws Exception {
        byte[] bytes = (byte[]) obj;
        try {
            if (bytes.length < messageHeaderLength) {
                log.error("messageReceived:消息长度{}小于等于消息头长度{}", bytes.length, messageHeaderLength);
                return;
            }
            int offset = messageHeaderLength > 4 ? 8 : 0;
            int msgID = MsgUtil.getMessageID(bytes, offset); //消息id
            log.info("消息id：{}", msgID);
            //校验消息id 是否已经注册
            if (TcpHandlerMsgManager.getInstance().getTcpHandlerMap().containsKey(msgID)) {
                //获取注册的handler
                Class<? extends IHandler> tcpHandler = TcpHandlerMsgManager.getInstance().getTcpHandler(msgID);
                //获取声明handlerEntry 注解的handler
                HandlerEntity handlerEntity = TcpHandlerMsgManager.getInstance().getTcpHandlerEntity(msgID);
                if (tcpHandler != null) {
                    Message message = MsgUtil.buildMessage(handlerEntity.msg(), bytes, messageHeaderLength,
                            bytes.length - messageHeaderLength);
                    TcpHandler handler = (TcpHandler) tcpHandler.newInstance();
                    if (null != handler) {
                        if (offset > 0) {
                            //发玩家id
                            long rid = MsgUtil.getMessageRID(bytes, 0);
                            handler.setRid(rid);
                        }
                        messageHandler(ioSession, handlerEntity, message, handler);
                        return;
                    }
                }
            }
            //消息转发
            forward(ioSession, msgID, bytes);
        } catch (Exception e) {
            log.error("messageReceived", e);
            int msgid = MsgUtil.getMessageID(bytes, 0);
            log.warn("尝试按0移位处理,id：{}", msgid);
        }
    }

    @Override
    public void messageSent(IoSession ioSession, Object o) throws Exception {

    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
        log.info("连接{}inputClosed已关闭", session);
        session.closeNow();
    }


    /**
     * 消息处理
     */
    public void messageHandler(IoSession ioSession, HandlerEntity handlerEntity, Message message, TcpHandler handler) {

        handler.setMessage(message);
        handler.setSession(ioSession);
        handler.setCreateTime(System.currentTimeMillis());
        Executor executor = getService().getExecutor(handlerEntity.thread());
        if (null == executor) {
            handler.run();
            return;
        }
        executor.execute(handler);
    }


    /**
     * 转发消息
     *
     * @param session
     * @param msgID
     * @param bytes
     */
    protected abstract void forward(IoSession session, int msgID, byte[] bytes);

    public abstract Service getService();


}
