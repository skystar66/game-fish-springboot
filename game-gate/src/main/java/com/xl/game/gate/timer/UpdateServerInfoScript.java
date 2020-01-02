package com.xl.game.gate.timer;

import com.xl.game.config.MinaClientConfig;
import com.xl.game.config.MinaServerTcpConfig;
import com.xl.game.engine.ServerType;
import com.xl.game.gate.config.MinaServerTcpUserConfig;
import com.xl.game.gate.manager.UserSessionManager;
import com.xl.game.gate.server.GateServer;
import com.xl.game.gate.server.Server;
import com.xl.game.gate.server.client.Gate2ClusterClient;
import com.xl.game.message.ServerMessage;
import com.xl.game.script.ITimerEventScript;
import com.xl.game.server.ServerState;
import com.xl.game.util.SysUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * 更新服务器信息脚本
 *
 * @author xuliang
 * @date 2019-12-30 QQ:359135103
 */
@Component
@Slf4j
public class UpdateServerInfoScript implements ITimerEventScript {


    @Autowired
    MinaServerTcpUserConfig minaServerConfig;

    @Autowired
    MinaClientConfig minaClientConfig;

    @Autowired
    Server server;


    @Override
    public void secondHandler(LocalTime localTime) {

        if (localTime.getSecond() % 5 == 0) { // 每5秒更新一次
            // todo 向服务器集群更新服务器信息
            // 向服务器集群更新服务器信息
            ServerMessage.ServerRegisterRequest request = buildServerRegisterRequest();
            log.info("发送集群服务器注册请求");
            server.getGateServer().getGate2ClusterClient().sendMsg(request);
            // LOGGER.debug("更新服务器信息");
            // 重连服务器监测
            server.getGateServer().getGate2ClusterClient().checkStatus();
            // 请求游戏服务器列表
            ServerMessage.ServerListRequest.Builder builder = ServerMessage.ServerListRequest.newBuilder();
            builder.setServerType(ServerType.GAME.getType());
            log.info("发送获取集群服务器列表请求");
            server.getGateServer().getGate2ClusterClient().sendMsg(builder.build());


        }


    }


    /**
     * 构建服务器更新注册信息
     *
     * @param
     * @return
     */
    public ServerMessage.ServerRegisterRequest buildServerRegisterRequest() {
        ServerMessage.ServerRegisterRequest.Builder builder = ServerMessage.ServerRegisterRequest.newBuilder();
        ServerMessage.ServerInfo.Builder info = ServerMessage.ServerInfo.newBuilder();
        info.setId(minaServerConfig.getId());
        info.setIp(minaServerConfig.getIp());
        info.setMaxUserCount(1000);
        info.setOnline(UserSessionManager.getInstance().getOlineCount());
        info.setName(minaServerConfig.getName());
        info.setState(ServerState.NORMAL.getState());
        info.setType(minaServerConfig.getType().getType());
        info.setWwwip("");
        info.setPort(minaServerConfig.getPort());
        info.setHttpport(minaServerConfig.getHttpPort());
        info.setFreeMemory(SysUtil.freeMemory());
        info.setVersion(minaServerConfig.getVersion());
        info.setTotalMemory(SysUtil.totalMemory());
        builder.setServerInfo(info);
        return builder.build();
    }


}
