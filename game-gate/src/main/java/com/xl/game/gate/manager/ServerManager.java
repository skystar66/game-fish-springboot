package com.xl.game.gate.manager;


import com.xl.game.engine.ServerInfo;
import com.xl.game.engine.ServerType;
import com.xl.game.gate.session.UserSession;
import com.xl.game.message.ServerMessage;
import com.xl.game.message.system.SystemMessage;
import com.xl.game.server.ServerState;
import com.xl.game.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器管理类
 *
 * @author xuliang
 * @QQ 359135103 2019年12月328日 下午5:49:38
 */
@Slf4j
public class ServerManager {


    private static volatile ServerManager serverManager;

    /**
     * 服务器列表
     */
    private final Map<ServerType, Map<Integer, ServerInfo>> serverMap = new ConcurrentHashMap<>();

    private ServerManager() {

    }

    public static ServerManager getInstance() {
        if (serverManager == null) {
            synchronized (ServerManager.class) {
                if (serverManager == null) {
                    serverManager = new ServerManager();
                }
            }
        }
        return serverManager;
    }


    /**
     * 获取游戏服务器信息
     *
     * @param serverType
     * @param serverId
     * @return
     */
    public ServerInfo getGameServerInfo(ServerType serverType, Integer serverId) {
        if (serverMap.containsKey(serverType)) {
            return serverMap.get(serverType).get(serverId);
        }
        return null;
    }

    /**
     * 更新大厅服务器信息
     *
     * @param info
     */
    public void updateServerInfo(ServerMessage.ServerInfo info) {

        if (info.getType() != ServerType.HALL.getType()) {
            return;
        }

        //更新大厅服
        ServerType serverType = ServerType.valueof(info.getType());
        ServerInfo server = getGameServerInfo(serverType, info.getId());
        if (server == null) {
            server = new ServerInfo();
            server.setId(info.getId());
        }

        server.setIp(info.getIp());
        server.setPort(info.getPort());
        server.setOnline(info.getOnline());
        server.setMaxUserCount(info.getMaxUserCount());
        server.setName(info.getName());
        server.setHttpPort(server.getHttpPort());
        server.setState(info.getState());
        server.setType(info.getType());
        server.setWwwip(info.getWwwip());

        if (!serverMap.containsKey(serverType)) {
            serverMap.put(serverType, new ConcurrentHashMap<>());
        }

        serverMap.get(serverType).put(info.getId(), server);

    }

    /**
     * 构建错误信息
     *
     * @param erroCode
     * @param msg
     * @return
     * @author JiangZhiYong
     * @QQ 359135103 2017年7月21日 上午10:37:49
     */
    public SystemMessage.SystemErrorResponse buildSystemErrorResponse(SystemMessage.SystemErroCode erroCode, String msg) {
        SystemMessage.SystemErrorResponse.Builder builder = SystemMessage.SystemErrorResponse.newBuilder();
        builder.setErrorCode(erroCode);
        if (msg != null) {
            builder.setMsg(msg);
        }
        return builder.build();
    }


    /**
     * 获取空闲的游戏服务器
     *
     * @param serverType
     * @return
     */
    public ServerInfo getIdleGameServer(ServerType serverType, UserSession userSession) {
        Map<Integer, ServerInfo> map = serverMap.get(serverType);
        if (map == null || map.size() == 0) {
            return null;
        }
        Optional<ServerInfo> findFirst = map.values().stream()
                .filter(server -> StringUtil.isNullOrEmpty(userSession.getVersion())
                        || userSession.getVersion().equals(server.getVersion())) // 版本号
                .filter(server -> server.getState() == ServerState.NORMAL.ordinal()) // 状态
                .sorted((s1, s2) -> s1.getOnline() - s2.getOnline()).findFirst();
        return findFirst.orElse(null);
    }


}
