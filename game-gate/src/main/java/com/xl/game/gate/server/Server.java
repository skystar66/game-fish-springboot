package com.xl.game.gate.server;

import com.xl.game.config.MinaClientConfig;
import com.xl.game.config.MongoClientConfig;
import com.xl.game.gate.manager.MongoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class Server {


    @Autowired
    private GateTcpUserServer gateTcpUserServer;//用户tcp服务
    @Autowired
    private GateUdpUserServer gateUdpUserServer;//用户udp服务
    @Autowired
    private GateTcpGameServer gateTcpGameServer;//游戏tcp服务 内部转发tcp
    @Autowired
    private GateWebSocketUserServer gateWebSocketUserServer;//用户websocket
    @Autowired
    private GateHttpServer gateHttpServer;//http服务
    @Autowired
    private MinaClientConfig minaClientConfig;//链接集群服

    @Autowired
    MongoClientConfig mongoClientConfig;


    private GateServer gateServer;


    public void start() {
        gateServer = new GateServer(gateTcpUserServer, gateUdpUserServer, gateTcpGameServer, gateWebSocketUserServer, gateHttpServer, minaClientConfig);
        Thread thread = new Thread(gateServer);
        thread.setDaemon(true);
        thread.start();
        MongoManager.getInstance().createConnect(mongoClientConfig);
    }


    public GateServer getGateServer() {
        return gateServer;
    }
}
