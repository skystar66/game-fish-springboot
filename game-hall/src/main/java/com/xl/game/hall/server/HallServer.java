package com.xl.game.hall.server;


import com.xl.game.config.*;
import com.xl.game.hall.config.MinaClusterClientConfig;
import com.xl.game.hall.config.MinaGateClientConfig;
import com.xl.game.hall.init.HandlerInit;
import com.xl.game.message.ServerMessage;
import com.xl.game.model.redis.key.HallChannel;
import com.xl.game.model.script.IGameServerCheckScript;
import com.xl.game.model.timer.GameServerCheckTimer;
import com.xl.game.mq.IMQScript;
import com.xl.game.mq.MQConsumer;
import com.xl.game.redis.IPubSubScript;
import com.xl.game.redis.listener.JedisPubListener;
import com.xl.game.redis.manager.JedisManager;
import com.xl.game.server.ServerState;
import com.xl.game.util.Config;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 大厅服务器
 *
 * @author xuliang
 * @QQ 359135103 2017年6月28日 下午3:37:19
 */
@Component
public class HallServer {


    /**
     * 连接网关 （接收网关转发过来的消息）
     */
    private Hall2GateClient hall2GateClient;

    /**
     * 连接集群服 （获取各服务器信息）
     */
    private Hall2ClusterClient hall2ClusterClient;


    /**
     * redis订阅发布
     */
    private JedisPubListener hallPubListener;


    /**
     * 服务器状态监测
     */
    private GameServerCheckTimer hallServerCheckTimer;

    /**
     * MQ消息
     */
    private MQConsumer mqConsumer;

    @Autowired
    ThreadPoolExecutorTcpConfig threadPoolExecutorTcpConfig;


    @Autowired
    MinaServerHttpConfig minaServerHttpConfig;

    @Autowired
    MQConfig mqConfig;

    @Autowired
    IMQScript imqScript;

    @Autowired
    MinaGateClientConfig minaGateClientConfig;
    @Autowired
    MinaClusterClientConfig minaClusterClientConfig;

    @Autowired
    MailConfig mailConfig;

    /**
     * HTTP服务
     */
    @Autowired
    HallHttpServer hallHttpServer;

    @Autowired
    JedisManager jedisManager;

    @Autowired
    IGameServerCheckScript gameServerCheckScript;



    @PostConstruct
    public void init() {

        MinaClientConfig minaClientConfig_gate = new MinaClientConfig();
        BeanUtils.copyProperties(minaGateClientConfig, minaClientConfig_gate);
        hall2GateClient = new Hall2GateClient(threadPoolExecutorTcpConfig, minaClientConfig_gate, mailConfig,gameServerCheckScript);
        MinaClientConfig minaClientConfig_cluster = new MinaClientConfig();
        BeanUtils.copyProperties(minaClusterClientConfig, minaClientConfig_cluster);
        hall2ClusterClient = new Hall2ClusterClient(minaClientConfig_cluster);
        hallServerCheckTimer = new GameServerCheckTimer(hall2ClusterClient, hall2GateClient, minaClientConfig_gate,gameServerCheckScript);
        hallPubListener = new JedisPubListener(jedisManager, HallChannel.getChannels());
        mqConsumer = new MQConsumer(mqConfig, imqScript);
        Config.SERVER_ID = minaClientConfig_gate.getId();


    }


    public void start() {

        new Thread(hall2GateClient).start();
        new Thread(hall2ClusterClient).start();
        new Thread(hallHttpServer).start();

        new Thread(new HandlerInit()).start();
        hallServerCheckTimer.start();
        hallPubListener.start();


    }

    public Hall2GateClient getHall2GateClient() {
        return hall2GateClient;
    }

    public Hall2ClusterClient getHall2ClusterClient() {
        return hall2ClusterClient;
    }

    public HallHttpServer getHallHttpServer() {
        return hallHttpServer;
    }


    /**
     * 构建服务器更新注册信息
     *
     * @param minaClientConfig
     * @return
     */
    public ServerMessage.ServerRegisterRequest buildServerRegisterRequest(MinaClientConfig minaClientConfig) {
        ServerMessage.ServerRegisterRequest.Builder builder = ServerMessage.ServerRegisterRequest.newBuilder();
        ServerMessage.ServerInfo.Builder info = ServerMessage.ServerInfo.newBuilder();
        info.setId(minaClientConfig.getId());
        info.setIp("");
        info.setMaxUserCount(1000);
        info.setOnline(1);
        info.setName(minaClientConfig.getName());
        info.setState(ServerState.NORMAL.getState());
        info.setType(minaClientConfig.getType().getType());
        info.setWwwip("");
        builder.setServerInfo(info);
        return builder.build();
    }


}
