package com.xl.game.cluster.manager;


import com.xl.game.cluster.server.tcp.ClusterTcpServer;
import com.xl.game.engine.ServerInfo;
import com.xl.game.engine.ServerType;
import com.xl.game.message.ServerMessage;
import com.xl.game.server.ServerState;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 集群服务器管理类
 *
 * @author xuliang
 * @date 2019-04-05 QQ:2755055412
 */

public class ServerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerManager.class);


    private static final ServerManager instance = new ServerManager();

    /**
     * 服务器列表
     */
    final Map<ServerType, Map<Integer, ServerInfo>> servers = new ConcurrentHashMap<>();

    public static ServerManager getInstance() {
        return instance;
    }


    /**
     * 注册服务器信息
     *
     * @param serverInfo
     */
    public ServerInfo registerServer(ServerMessage.ServerInfo serverInfo, IoSession session) {

        Map<Integer, ServerInfo> map = servers.get(ServerType.valueof(serverInfo.getType()));
        if (map == null) {
            map = new ConcurrentHashMap<>();
            servers.put(ServerType.valueof(serverInfo.getType()), map);
        }
        ServerInfo info = map.get(serverInfo.getId());
        if (null == info) {
            info = new ServerInfo();
            map.put(serverInfo.getId(), info);
        }

        info.setHttpPort(serverInfo.getHttpport());//http 端口
        info.setId(serverInfo.getId()); //服务id
        info.setFreeMemory(serverInfo.getFreeMemory());//空闲内存
        info.setIp(serverInfo.getIp());//内网ip
        info.setMaxUserCount(serverInfo.getMaxUserCount());//最大用户数量
        info.setName(serverInfo.getName());//名字
        info.setOnline(serverInfo.getOnline());//在线用户数量
        info.setPort(serverInfo.getPort());
        info.setType(serverInfo.getType()); //注册服务类型《ServerType》
        info.setWwwip(serverInfo.getWwwip());//外网地址
        info.setTotalMemory(serverInfo.getTotalMemory());//可用内存
        info.onIoSessionConnect(session);//客户端多连接管理
        info.setVersion(serverInfo.getVersion());
        info.setFreeMemory(serverInfo.getFreeMemory());

        if (!session.containsAttribute(ClusterTcpServer.SERVER_INFO)) {
            session.setAttribute(ClusterTcpServer.SERVER_INFO, info);
        }
        return info;

    }


    /**
     * 获取服务器列表
     *
     * @param serverType
     * @return
     */

    public Map<Integer, ServerInfo> getServers(ServerType serverType) {
        return servers.get(serverType);
    }


    /**
     * 获取服务器
     */
    public ServerInfo getServer(ServerType serverType, int serverId) {

        Map<Integer, ServerInfo> map = getServers(serverType);
        if (map != null) {
            return map.get(serverId);
        }
        return null;
    }

    public Map<ServerType, Map<Integer, ServerInfo>> getServers() {
        return servers;
    }


    /**
     * 空闲服务器 网关
     * <p>
     * 服务器状态监测,在线人数统计 <br>
     * 服务健康度检测（除了根据在线人数判断还可根据cpu内存等服务器检测设置优先级）
     * <p>
     *
     * @param version 版本号 null 无版本要求
     * @return
     */

    public ServerInfo getIdleGate(String version) {


        Map<Integer, ServerInfo> halls = getServers(ServerType.GATE);
        if (null == halls) {
            return null;
        }

        Optional<ServerInfo> findFirst = halls.values().stream()
                .filter(
                        serverInfo -> serverInfo.getState() == ServerState.NORMAL.ordinal()
                                && serverInfo.getSession() != null && serverInfo.getSession().isConnected()
                ).filter(serverInfo -> version == null || serverInfo.getVersion().equals(version)) //版本号检查
                .sorted((s1, s2) -> s1.getOnline() - s2.getOnline()).findFirst();

        if (findFirst.isPresent()) {
            return findFirst.get();
        }
        return null;

    }

    /**
     * 移除服务器
     *
     * @param serverInfo
     */

    public void removeServer(ServerInfo serverInfo) {
        LOGGER.info("服务器关闭：{}", serverInfo.toString());
        ServerType serverType = ServerType.valueof(serverInfo.getType());
        Map<Integer, ServerInfo> map = servers.get(serverType);
        if (map != null) {
            if (map.remove(serverInfo.getId()) != null) {
                LOGGER.info("服务器{}_{}已移除", serverType.toString(), serverInfo.getId());
            }
        }
    }


}
