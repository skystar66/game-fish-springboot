package com.xl.game.cluster.init;


import com.xl.game.cluster.handler.http.*;
import com.xl.game.cluster.handler.tcp.ServerRegisterHandler;
import com.xl.game.manager.http.HttpHandlerMsgManager;
import com.xl.game.manager.tcp.TcpHandlerMsgManager;
import com.xl.game.model.server.ExitServerHandler;
import com.xl.game.model.server.JvmInfoHandler;
import com.xl.game.model.server.ThreadInfoHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * handler初始化
 *
 * @author xuliang
 * @date 2019-04-05 QQ:2755055412
 */
@Slf4j
public class HandlerInit implements Runnable {


    @Override
    public void run() {
        //初始化httphandler
        HttpHandlerMsgManager.getInstance().addHandler(ExitServerHandler.class);
        HttpHandlerMsgManager.getInstance().addHandler(JvmInfoHandler.class);
        HttpHandlerMsgManager.getInstance().addHandler(ThreadInfoHandler.class);
        HttpHandlerMsgManager.getInstance().addHandler(GetGateIpHandler.class);
        HttpHandlerMsgManager.getInstance().addHandler(GetHallIpHandler.class);
        HttpHandlerMsgManager.getInstance().addHandler(ServerListHandler.class);
        HttpHandlerMsgManager.getInstance().addHandler(SetServerStateHandler.class);

        //初始化tcp handler
        TcpHandlerMsgManager.getInstance().addHandler(com.xl.game.cluster.handler.tcp.ServerListHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(ServerRegisterHandler.class);

        log.info("http 处理器 tcp 处理器 初始化完成！");
    }
}
