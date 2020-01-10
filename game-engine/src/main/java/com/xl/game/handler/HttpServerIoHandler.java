package com.xl.game.handler;

import com.xl.game.manager.http.HttpHandlerMsgManager;
import com.xl.game.server.Service;
import com.xl.game.thread.ThreadType;
import com.xl.game.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.http.HttpRequestImpl;

import java.util.concurrent.Executor;

@Slf4j
public abstract class HttpServerIoHandler implements IoHandler {


    @Override
    public void sessionOpened(IoSession ioSession) throws Exception {

    }

    @Override
    public void messageReceived(IoSession ioSession, Object message) throws Exception {

        if (!(message instanceof HttpRequestImpl)) {
            log.error("请求消息非法！:{}", message.toString());
            return;
        }
        long begin = System.currentTimeMillis();
        HttpRequestImpl httpRequest = (HttpRequestImpl) message;
        Class<? extends IHandler> handlerClass = HttpHandlerMsgManager.getInstance()
                .getHttpHandler(httpRequest.getRequestPath());
        HandlerEntity handlerEntity = HttpHandlerMsgManager.getInstance()
                .getHandlerEntity(httpRequest.getRequestPath());
        try {
            log.info("mina 服务器获取消息 msg : {}", message.toString());
            IHandler handler = handlerClass.newInstance();
            handler.setMessage(httpRequest); //创建消息
            handler.setSession(ioSession);
            handler.setCreateTime(System.currentTimeMillis());
            Service service = getSevice();
            Executor executor = service.getExecutor(handlerEntity.thread());
            if (null != executor) {
                executor.execute(handler);
            } else {
                handler.run();
            }

        } catch (Exception ex) {
            log.error("errorMsg : {}", ex);
        }

        long cost = System.currentTimeMillis() - begin;
        if (cost > 30L) {
            log.error(String.format("\t messageReceived %s msgID[%s] builder[%s] cost %d ms",
                    Thread.currentThread().toString(), httpRequest.getRequestPath(), httpRequest.toString(), cost));
        }
    }


    @Override
    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {

        ioSession.closeOnFlush();
    }


    @Override
    public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {
        ioSession.closeOnFlush();
        log.debug("exceptionCaught", throwable);
    }


    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        log.info("http请求建立");
    }


    @Override
    public void sessionClosed(IoSession ioSession) throws Exception {
        log.info("session 关闭");
    }

    @Override
    public void messageSent(IoSession session, Object o) throws Exception {
        if (!session.isClosing()) {
            MsgUtil.close(session, "http isClosing");
        }
    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
        log.error("http inputClosed " + session);
        MsgUtil.close(session, "http inputClosed");
    }

    /**
     * 对应的服务
     *
     * @return
     */
    protected abstract Service getSevice();


}
