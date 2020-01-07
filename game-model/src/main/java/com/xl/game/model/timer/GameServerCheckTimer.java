package com.xl.game.model.timer;

import com.xl.game.config.BaseServerConfig;
import com.xl.game.config.MinaClientConfig;
import com.xl.game.engine.ServerType;
import com.xl.game.message.ServerMessage;
import com.xl.game.model.script.IGameServerCheckScript;
import com.xl.game.server.IMutilTcpClientService;
import com.xl.game.server.ITcpClientService;
import com.xl.game.server.ServerState;
import com.xl.game.thread.timer.ScheduledTask;
import com.xl.game.util.SysUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 游戏服务器 状态监测，重连线程
 * <p>
 * 每隔10秒监测一次
 * </p>
 *
 * @author xuliang
 * @QQ 359135103 2017年7月3日 上午10:47:45
 */
@Slf4j
public class GameServerCheckTimer extends ScheduledTask {
    private ITcpClientService clusterService; // 集群连接服务

    private IMutilTcpClientService gateService; // 网关连接服务


    private BaseServerConfig config; // 游戏服配置


    private IGameServerCheckScript gameServerCheckScript;

    /**
     * @param clusterService 连接集群客户端
     * @param gateService    连接网关客户端，可为null
     * @param config
     */
    public GameServerCheckTimer(ITcpClientService clusterService,
                                IMutilTcpClientService gateService,
                                BaseServerConfig config, IGameServerCheckScript gameServerCheckScript) {
        super(10000);
        this.clusterService = clusterService;
        this.gateService = gateService;
        this.config = config;
        this.gameServerCheckScript = gameServerCheckScript;
    }


    public GameServerCheckTimer(int taskMaxTime) {
        super(taskMaxTime);
    }

    @Override
    protected void executeTask() {
        // 向网关和集群注册游戏服务器信息

        ServerMessage.ServerRegisterRequest.Builder registerRequestBuilder = buildServerRegisterRequest(config);


        ServerMessage.ServerRegisterRequest serverRegisterRequest = registerRequestBuilder.build();


        // 集群服
        clusterService.sendMsg(serverRegisterRequest);
        clusterService.checkStatus();

        // 网关服 监测连接到其他服务器客户端状态

        if (gateService != null) {

            if (!gateService.broadcastMsg(serverRegisterRequest)) {
                log.warn("大厅服未连接");
            }
            gateService.checkStatus();
        }
        // 获取可连接的网关列表
        ServerMessage.ServerListRequest.Builder builder = ServerMessage.ServerListRequest.newBuilder();
        builder.setServerType(ServerType.GATE.getType());
        clusterService.sendMsg(builder.build());

    }


    /**
     * 构建服务器更新注册信息
     *
     * @param baseServerConfig
     * @return
     */
    private ServerMessage.ServerRegisterRequest.Builder buildServerRegisterRequest(BaseServerConfig baseServerConfig) {
        ServerMessage.ServerRegisterRequest.Builder builder = ServerMessage.ServerRegisterRequest.newBuilder();


        ServerMessage.ServerInfo.Builder info = ServerMessage.ServerInfo.newBuilder();
        info.setId(baseServerConfig.getId());
        info.setIp("");
        info.setMaxUserCount(1000);
        info.setName(baseServerConfig.getName());
        info.setState(ServerState.NORMAL.getState());
        info.setWwwip("");
        info.setVersion(baseServerConfig.getVersion());
        info.setTotalMemory(SysUtil.totalMemory());
        info.setFreeMemory(SysUtil.freeMemory());
        if (baseServerConfig instanceof MinaClientConfig) {
            MinaClientConfig minaClientConfig = (MinaClientConfig) baseServerConfig;
            info.setType(minaClientConfig.getType().getType());
        }
//
//        else if(baseServerConfig instanceof NettyClientConfig) {
//            NettyClientConfig nettyClientConfig=(NettyClientConfig) baseServerConfig;
//            info.setType(nettyClientConfig.getType().getType());
//        }
//
        else {
            throw new RuntimeException("服务器配置未实现");
        }
        gameServerCheckScript.buildServerInfo(info);
        builder.setServerInfo(info);
        return builder;

    }


}
