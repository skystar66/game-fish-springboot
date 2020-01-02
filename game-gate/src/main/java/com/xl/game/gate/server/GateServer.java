package com.xl.game.gate.server;

import com.xl.game.config.MinaClientConfig;
import com.xl.game.config.MinaServerTcpConfig;
import com.xl.game.config.ThreadPoolExecutorTcpConfig;
import com.xl.game.gate.server.client.Gate2ClusterClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class GateServer implements Runnable {


    private GateTcpUserServer gateTcpUserServer;//用户tcp服务
    private GateUdpUserServer gateUdpUserServer;//用户udp服务
    private GateTcpGameServer gateTcpGameServer;//游戏tcp服务 内部转发tcp
    private GateWebSocketUserServer gateWebSocketUserServer;//用户websocket
    private GateHttpServer gateHttpServer;//http服务
    private Gate2ClusterClient gate2ClusterClient;//链接集群服


    public GateServer(GateTcpUserServer gateTcpUserServer, GateUdpUserServer gateUdpUserServer
            , GateTcpGameServer gateTcpGameServer, GateWebSocketUserServer gateWebSocketUserServer
            , GateHttpServer gateHttpServer, MinaClientConfig minaClientConfig) {

        this.gateTcpUserServer = gateTcpUserServer;
        this.gateHttpServer = gateHttpServer;
        this.gateTcpGameServer = gateTcpGameServer;
        this.gateUdpUserServer = gateUdpUserServer;
        this.gateWebSocketUserServer = gateWebSocketUserServer;
        this.gate2ClusterClient = new Gate2ClusterClient(minaClientConfig);

    }


    @Override
    public void run() {

        new Thread(gateTcpUserServer).start();
        new Thread(gate2ClusterClient).start();
        new Thread(gateTcpGameServer).start();
        new Thread(gateHttpServer).start();
        if (gateUdpUserServer != null) {
            new Thread(gateUdpUserServer).start();
        }
        if (gateWebSocketUserServer != null) {
            new Thread(gateWebSocketUserServer).start();
        }
    }


    public Gate2ClusterClient getGate2ClusterClient() {
        return gate2ClusterClient;
    }
}
