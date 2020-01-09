package com.xl.game.bydr.server;

import com.xl.game.config.MinaServerHttpConfig;
import com.xl.game.config.MinaServerTcpConfig;
import com.xl.game.mina.TcpServer;
import com.xl.game.mina.handler.ClientProtocolHandler;
import com.xl.game.server.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class BydrGameTcpServer extends Service {


    private TcpServer tcpServer;
    @Autowired
    private MinaServerTcpConfig minaServerConfig;

    @PostConstruct
    public void init() {
        ClientProtocolHandler handler = new ClientProtocolHandler(8);
        handler.setService(this);
        tcpServer = new TcpServer(minaServerConfig,
                handler);
    }


    @Override
    protected void running() {
        tcpServer.run();
    }


    @Override
    public void onShutDown() {
        super.onShutDown();
        tcpServer.stop();
    }
}
