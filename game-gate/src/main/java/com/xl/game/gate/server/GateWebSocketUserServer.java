package com.xl.game.gate.server;

import com.xl.game.config.MinaServerTcpConfig;
import com.xl.game.config.MinaServerWebSocketConfig;
import com.xl.game.gate.config.MinaServerWebsocketConfig;
import com.xl.game.gate.script.IGateServerScript;
import com.xl.game.gate.script.IUserScript;
import com.xl.game.gate.server.handler.GateWebSocketUserServerHandler;
import com.xl.game.mina.TcpServer;
import com.xl.game.mina.websocket.WebSocketCodecFactory;
import com.xl.game.script.IInitScript;
import com.xl.game.server.Service;
import com.xl.game.util.CommonBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.firewall.BlacklistFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * 网关 用户WebSocket 服务器（网页链接）
 *
 * @author xuliang
 * @QQ 359135103
 * 2019年12月30日 下午1:37:02
 */
@Component
@Slf4j
public class GateWebSocketUserServer extends Service {

    @Autowired
    MinaServerWebsocketConfig minaServerWebSocketConfig;
    final Map<String, IoFilter> filters = new HashMap<>();
    private BlacklistFilter blacklistFilter;    //IP黑名单过滤器
    private GateWebSocketUserServerHandler gateWebSocketUserServerHandler;

    private TcpServer tcpServer;

    @Resource(name = "ipBlackList")
    IGateServerScript gateServerScript;
    @Autowired
    IUserScript userScript;

    @Resource(name = "ipBlackList")
    IInitScript initScript;


    @PostConstruct
    public void init() {
        blacklistFilter = new BlacklistFilter();

        filters.put("Blacklist", blacklistFilter);

        initScript.init();
        gateServerScript.setIpBlackList(blacklistFilter);

        gateWebSocketUserServerHandler = new GateWebSocketUserServerHandler(4, this, userScript);

        MinaServerTcpConfig minaServerTcpConfig = new MinaServerTcpConfig();

        BeanUtils.copyProperties(minaServerWebSocketConfig, minaServerTcpConfig);

        tcpServer = new TcpServer(minaServerTcpConfig, gateWebSocketUserServerHandler, new WebSocketCodecFactory(), filters);

    }


    @Override
    protected void running() {
        log.info("start gate websocket server .......");
        tcpServer.run();
        log.warn("已开始监听网关webspcket tcp端口：{}", minaServerWebSocketConfig.getPort());

    }

    @Override
    public void onShutDown() {
        super.onShutDown();
        tcpServer.stop();
    }
}
