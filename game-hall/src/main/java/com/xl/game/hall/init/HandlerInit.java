package com.xl.game.hall.init;

import com.xl.game.hall.handler.chat.ChatHandler;
import com.xl.game.hall.handler.guild.*;
import com.xl.game.hall.handler.http.GmHandler;
import com.xl.game.hall.handler.login.LoginHandler;
import com.xl.game.hall.handler.login.QuitHandler;
import com.xl.game.hall.handler.mail.MailListHandler;
import com.xl.game.hall.handler.mail.ModifyMailHandler;
import com.xl.game.hall.handler.packet.PacketItemsHandler;
import com.xl.game.hall.handler.server.ServerListHandler;
import com.xl.game.hall.handler.server.ServerRegisterHandler;
import com.xl.game.manager.http.HttpHandlerMsgManager;
import com.xl.game.manager.tcp.TcpHandlerMsgManager;

public class HandlerInit implements Runnable {


    @Override
    public void run() {


        TcpHandlerMsgManager.getInstance().addHandler(ChatHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(ApplyGuildHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(CreateGuildHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(GuildApprovalHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(GuildInfoHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(GuildListHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(GmHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(LoginHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(QuitHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(MailListHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(ModifyMailHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(PacketItemsHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(ServerListHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(ServerRegisterHandler.class);


    }
}
