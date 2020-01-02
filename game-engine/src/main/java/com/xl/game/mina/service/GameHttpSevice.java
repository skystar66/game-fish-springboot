package com.xl.game.mina.service;

import com.xl.game.config.MinaServerHttpConfig;
import com.xl.game.handler.HttpServerIoHandler;
import com.xl.game.mina.HttpServer;
import com.xl.game.server.Service;
import lombok.extern.slf4j.Slf4j;


/**
 * 游戏服http服务器
 * @author xuliang
 * @QQ 359135103
 * 2019年12月30日 上午11:28:28
 */
@Slf4j
public class GameHttpSevice extends Service {

    private MinaServerHttpConfig minaServerConfig;


    private HttpServer httpServer;

    public GameHttpSevice(MinaServerHttpConfig minaServerConfig){
        this.minaServerConfig = minaServerConfig;
        httpServer = new HttpServer(minaServerConfig, new GameHttpServerHandler(this));
    }


    @Override
    protected void running() {

        log.debug(" start game http server run ... ");

        httpServer.run();
    }


    @Override
    public void onShutDown() {
        super.onShutDown();
        httpServer.stop();
    }

    public class GameHttpServerHandler extends HttpServerIoHandler {



        private final Service service;

        public GameHttpServerHandler(Service service) {
            this.service=service;
        }


        @Override
        protected Service getSevice() {
            return null;
        }
    }


}
