package com.xl.game.mina.handler;

import com.xl.game.mina.service.MinaClientService;
import com.xl.game.server.Service;
import com.xl.game.util.IntUtil;
import org.apache.mina.core.session.IoSession;

public class DefaultClientProtocolHandler extends DefaultProtocolHandler {


    public DefaultClientProtocolHandler(int messageHeaderLength, MinaClientService service) {
        super(messageHeaderLength);
        this.service = service;
    }



    public DefaultClientProtocolHandler(MinaClientService service) {
        super(4);
        this.service = service;
    }

    private final MinaClientService service;



    @Override
    public void sessionOpened(IoSession session) throws Exception{
        log.info("打开链接");
        super.sessionOpened(session);
        getService().onIoSessionConnect(session);
    }

    @Override
    protected void forward(IoSession session, int msgID, byte[] bytes) {
        log.warn("无法找到消息处理器：msgID{},bytes{}", msgID, IntUtil.BytesToStr(bytes));
    }

    @Override
    public MinaClientService getService() {
        return service;
    }

    @Override
    public void sessionClosed(IoSession session) {
        super.sessionClosed(session);
        getService().onIoSessionClosed(session);
    }

}
