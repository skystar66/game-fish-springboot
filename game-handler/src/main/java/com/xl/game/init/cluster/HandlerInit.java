package com.xl.game.init.cluster;


import com.xl.game.http.cluster.GetGateIpHandler;
import com.xl.game.http.cluster.GetHallIpHandler;
import com.xl.game.http.cluster.ServerListHandler;
import com.xl.game.http.cluster.SetServerStateHandler;
import com.xl.game.manager.http.HttpHandlerMsgManager;
import com.xl.game.manager.tcp.TcpHandlerMsgManager;
import com.xl.game.model.server.ExitServerHandler;
import com.xl.game.model.server.JvmInfoHandler;
import com.xl.game.model.server.ThreadInfoHandler;
import com.xl.game.tcp.cluster.ServerRegisterHandler;
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
        TcpHandlerMsgManager.getInstance().addHandler(ServerListHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(ServerRegisterHandler.class);

        log.info("http 处理器 tcp 处理器 初始化完成！");
    }
}
