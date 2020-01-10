package com.xl.game.bydr.init;

import com.xl.game.bydr.handler.flight.*;
import com.xl.game.bydr.handler.login.BydrLoginHandler;
import com.xl.game.bydr.handler.login.QuitSubGameHandler;
import com.xl.game.bydr.handler.room.EnterRoomHandler;
import com.xl.game.bydr.handler.room.QuitRoomHandler;
import com.xl.game.bydr.handler.server.ChangeRoleServerResHandler;
import com.xl.game.bydr.handler.server.ServerListHandler;
import com.xl.game.bydr.handler.server.ServerRegisterHandler;
import com.xl.game.manager.http.HttpHandlerMsgManager;
import com.xl.game.manager.tcp.TcpHandlerMsgManager;
import com.xl.game.model.server.ExitServerHandler;
import com.xl.game.model.server.JvmInfoHandler;
import com.xl.game.model.server.ThreadInfoHandler;

public class HandlerInit implements Runnable {


    @Override
    public void run() {


        //初始化httphandler
        HttpHandlerMsgManager.getInstance().addHandler(ExitServerHandler.class);
        HttpHandlerMsgManager.getInstance().addHandler(JvmInfoHandler.class);
        HttpHandlerMsgManager.getInstance().addHandler(ThreadInfoHandler.class);


        TcpHandlerMsgManager.getInstance().addHandler(ApplyAthleticsHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(FireHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(FireResultHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(GunLeveUpHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(UseSkillHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(BydrLoginHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(QuitSubGameHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(EnterRoomHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(QuitRoomHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(ChangeRoleServerResHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(ServerListHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(ServerRegisterHandler.class);


    }
}
