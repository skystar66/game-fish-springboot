package com.xl.game.bydr.server;

import com.xl.game.config.MinaServerHttpConfig;
import com.xl.game.handler.HttpServerIoHandler;
import com.xl.game.mina.HttpServer;
import com.xl.game.mina.service.GameHttpSevice;
import com.xl.game.server.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * http服务器
 *
 * @author xuliang
 * @QQ 359135103 2020年1月7日 下午1:46:00
 */
@Component
@Slf4j
public class BydrHttpServer extends Service {


    private HttpServer httpServer;
    @Autowired
    private MinaServerHttpConfig minaServerConfig;


    @PostConstruct
    public void init() {
        httpServer = new HttpServer(minaServerConfig, new GameHttpServerHandler(this));
    }


    @Override
    protected void running() {

        log.info("bydr http server start 。。。。。。");
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
            this.service = service;
        }


        @Override
        protected Service getSevice() {
            return null;
        }
    }


}
