package com.xl.game.gate.server;

import com.xl.game.config.MinaServerHttpConfig;
import com.xl.game.gate.handler.chat.ChatResHandler;
import com.xl.game.gate.handler.role.*;
import com.xl.game.gate.handler.server.*;
import com.xl.game.handler.HttpServerIoHandler;
import com.xl.game.manager.http.HttpHandlerMsgManager;
import com.xl.game.manager.tcp.TcpHandlerMsgManager;
import com.xl.game.mina.HttpServer;
import com.xl.game.mina.service.GameHttpSevice;
import com.xl.game.model.server.ExitServerHandler;
import com.xl.game.model.server.JvmInfoHandler;
import com.xl.game.model.server.ThreadInfoHandler;
import com.xl.game.server.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class GateHttpServer extends Service {


    @Autowired
    private MinaServerHttpConfig minaServerConfig;


    private HttpServer httpServer;

    @PostConstruct
    public void init() {
        httpServer = new HttpServer(minaServerConfig, new GameHttpServerHandler(this));
    }


    @Override
    protected void running() {

        log.info(" start game http server run ... ");

        httpServer.run();

        //初始化httphandler
        HttpHandlerMsgManager.getInstance().addHandler(ExitServerHandler.class);
        HttpHandlerMsgManager.getInstance().addHandler(JvmInfoHandler.class);
        HttpHandlerMsgManager.getInstance().addHandler(ThreadInfoHandler.class);

        //初始化tcp handler
        TcpHandlerMsgManager.getInstance().addHandler(ChatResHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(HeartHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(LoginReqHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(LoginResHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(LoginSubGameHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(QuitReqHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(QuitResHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(QuitSubGameReqHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(QuitSubGameResHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(UdpConnectHandler.class);

        TcpHandlerMsgManager.getInstance().addHandler(ChangeRoleServerHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(ServerEventHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(ServerListHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(ServerRegisterHandler.class);
        TcpHandlerMsgManager.getInstance().addHandler(ServerRegisterResHandler.class);


    }


    @Override
    public void onShutDown() {
        super.onShutDown();
        httpServer.stop();
    }


    public class GameHttpServerHandler extends HttpServerIoHandler {


        private final Service service;

        public GameHttpServerHandler(Service service) {
            this.service = service;
        }


        @Override
        protected Service getSevice() {
            return null;
        }
    }


}
