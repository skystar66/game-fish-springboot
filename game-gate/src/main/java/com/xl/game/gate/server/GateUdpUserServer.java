package com.xl.game.gate.server;

import com.xl.game.config.MailConfig;
import com.xl.game.config.MinaServerTcpConfig;
import com.xl.game.config.ThreadPoolExecutorTcpConfig;
import com.xl.game.gate.config.MinaServerUdpUserConfig;
import com.xl.game.gate.script.IGateServerScript;
import com.xl.game.gate.server.handler.GateUdpUserServerHandler;
import com.xl.game.mina.UdpServer;
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
 * 网关 用户udp 服务器
 * <p>
 * 1.在弱网条件下，udp效率更高，tcp存在阻塞重发，三次握手，消息重组等条件，速度很慢；如果消息丢包影响不大，实时性要求高，可以使用udp替换tcp
 * <br>
 * 2.网关服务器收到前端udp消息通过内部的tcp消息进行转发，当收到内部服务器的tcp消息为udp消息时，用udp返回给用户
 * <br>
 * 3.udp消息是不可靠的，只能辅助进行消息处理（可考虑实现可靠udp）
 * </p>
 *
 * @author xuliang
 * @QQ 359135103 2017年9月1日 下午1:45:20
 */

@Component
@Slf4j
public class GateUdpUserServer extends Service {


    @Autowired
    ThreadPoolExecutorTcpConfig threadPoolExecutorTcpConfig;

    @Autowired
    MailConfig mailConfig;

    @Autowired
    MinaServerUdpUserConfig minaServerUdpUserConfig;

    private UdpServer udpServer;

    final Map<String, IoFilter> filters = new HashMap<>();
    private BlacklistFilter blacklistFilter;    //IP黑名单过滤器


    @Resource(name = "ipBlackList")
    IGateServerScript gateServerScript;

    @Resource(name = "ipBlackList")
    IInitScript initScript;

    private GateUdpUserServerHandler gateUdpUserServerHandler;


    @PostConstruct
    public void init() {
        blacklistFilter = new BlacklistFilter();
        filters.put("Blacklist", blacklistFilter);
        initScript.init();
        gateServerScript.setIpBlackList(blacklistFilter);
        gateUdpUserServerHandler = new GateUdpUserServerHandler(8);
        gateUdpUserServerHandler.setService(this);

        MinaServerTcpConfig minaServerTcpConfig = new MinaServerTcpConfig();
        BeanUtils.copyProperties(minaServerUdpUserConfig,minaServerTcpConfig);
        udpServer = new UdpServer(minaServerTcpConfig, gateUdpUserServerHandler, filters);
    }


    @Override
    protected void running() {
        log.info("start gate udp server ........");
        udpServer.run();
        log.warn("已开始监听网关用户 UDP端口：{}", minaServerUdpUserConfig.getPort());
    }


    @Override
    public void onShutDown() {
        super.onShutDown();
        udpServer.stop();
    }
}
