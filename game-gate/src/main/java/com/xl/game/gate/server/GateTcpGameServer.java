package com.xl.game.gate.server;


import com.xl.game.config.MinaServerTcpConfig;
import com.xl.game.gate.config.MinaServerTcpGameConfig;
import com.xl.game.gate.script.IGateServerScript;
import com.xl.game.gate.server.handler.GateTcpGameServerHandler;
import com.xl.game.mina.TcpServer;
import com.xl.game.script.IInitScript;
import com.xl.game.server.Service;
import com.xl.game.util.CommonBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 子游戏连接 服务
 * <p>游戏服、大厅服等内部共用的服务器</p>
 *
 * @author xuliang
 * @QQ 359135103 2019年12月30日 下午2:20:01
 */
@Component
@Slf4j
public class GateTcpGameServer extends Service {


    private TcpServer tcpServer;

    @Autowired
    private MinaServerTcpGameConfig minaServerTcpGameConfig;

    @Resource(name = "udpMsgRegister")
    IGateServerScript iGateServerScript;

    @Resource(name = "udpMsgRegister")
    IInitScript initScript;

    private GateTcpGameServerHandler gateTcpGameServerHandler;

    @PostConstruct
    public void init() {
        initScript.init();
        gateTcpGameServerHandler = new GateTcpGameServerHandler(12, iGateServerScript);
        gateTcpGameServerHandler.setService(this);

        MinaServerTcpConfig minaServerTcpConfig = new MinaServerTcpConfig();
        BeanUtils.copyProperties(minaServerTcpGameConfig,minaServerTcpConfig);
        tcpServer = new TcpServer(minaServerTcpConfig, gateTcpGameServerHandler);
    }


    @Override
    protected void running() {
        log.info("start gate game tcp server ........");
        tcpServer.run();
        log.warn("已开始监听网关游戏 TCP端口：{}", minaServerTcpGameConfig.getPort());

    }

    @Override
    public void onShutDown() {
        super.onShutDown();
        tcpServer.stop();
    }
}
