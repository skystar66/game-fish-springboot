package com.xl.game.mina.handler;

import com.google.protobuf.Message;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.IHandler;
import com.xl.game.handler.TcpHandler;
import com.xl.game.manager.tcp.TcpHandlerMsgManager;
import com.xl.game.server.Service;
import com.xl.game.util.IntUtil;
import com.xl.game.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;

import java.util.concurrent.Executor;

@Slf4j
public class ClientProtocolHandler extends DefaultProtocolHandler {


    protected Service service;

    public ClientProtocolHandler(int messageHeaderLength) {
        super(messageHeaderLength);
    }


    @Override
    public void messageReceived(IoSession ioSession, Object obj) throws Exception {

        byte[] bytes = (byte[]) obj;

        try {
            if (bytes.length < messageHeaderLength) {
                log.error("messageReceived:消息长度{}小于等于消息头长度{}",
                        bytes.length, messageHeaderLength);
                return;
            }

            int mid = IntUtil.bigEndianByteToInt(bytes, 0, 4); // 消息ID
            log.info("消息id：{}", mid);

            if (TcpHandlerMsgManager.getInstance().tcpMsgIsRegister(mid)) {

                Class<? extends IHandler> handlerClass = TcpHandlerMsgManager.getInstance().getTcpHandler(mid);

                HandlerEntity handlerEntity = TcpHandlerMsgManager.getInstance().getTcpHandlerEntity(mid);
                if (handlerClass != null) {
                    //获取消息
                    Message message = MsgUtil.buildMessage(handlerEntity.msg(), bytes, messageHeaderLength,
                            bytes.length - messageHeaderLength);
                    log.info("消息：{}", message.toString());

                    TcpHandler tcpHandler = (TcpHandler) handlerClass.newInstance();
                    if (tcpHandler != null) {
                        messageHandler(ioSession, message, tcpHandler, handlerEntity);
                    }
                }
            }
            log.info("非网关消息");
            forward(ioSession, mid, bytes);
        } catch (Exception ex) {
            log.error("messageReceived errMsg: {}", ex);
        }


    }

    @Override
    protected void forward(IoSession session, int msgID, byte[] bytes) {
        log.warn("消息[{}]未实现", msgID);
    }

    @Override
    public Service getService() {
        return service;
    }

    public void messageHandler(IoSession session, Message message, TcpHandler tcpHandler, HandlerEntity handlerEntity) {
        tcpHandler.setMessage(message);
        tcpHandler.setSession(session);
        tcpHandler.setCreateTime(System.currentTimeMillis());
        Executor executor = getService().getExecutor(handlerEntity.thread());
        if (null != executor) {
            executor.execute(tcpHandler);
        } else {
            tcpHandler.run();
        }
    }

    public void setService(Service service) {
        this.service = service;
    }


}
