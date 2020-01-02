package com.xl.game.cluster.handler.tcp;


import com.xl.game.cluster.manager.ServerManager;
import com.xl.game.engine.ServerInfo;
import com.xl.game.engine.ServerType;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.ServerMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * 获取服务器列表信息
 *
 * @author xuliang
 * @QQ 359135103 2019年12月26日 上午11:39:55
 */
@Slf4j
@HandlerEntity(mid = Mid.MID.ServerListReq_VALUE, msg = ServerMessage.ServerListRequest.class)
public class ServerListHandler extends TcpHandler {


    @Override
    public void run() {

        ServerMessage.ServerListRequest serverListRequest = getMsg();
        if (serverListRequest.getServerType() != ServerType.GAME.getType()) {
            //只获取非游戏服 服务器
            Map<Integer, ServerInfo> serverInfoMaps = ServerManager.getInstance().getServers(ServerType.valueof(serverListRequest.getServerType()));
            ServerMessage.ServerListResponse.Builder resBuilder = ServerMessage.ServerListResponse.newBuilder();
            ServerMessage.ServerInfo.Builder infoBuild = ServerMessage.ServerInfo.newBuilder();
            if (!MapUtils.isEmpty(serverInfoMaps)) {
                serverInfoMaps.forEach((id, serverinfo) -> {
                    if (serverinfo != null && serverinfo.getSession() != null
                            && serverinfo.getSession().isConnected()) {
                        resBuilder.addServerInfo(builderServerInfo(serverinfo, infoBuild));
                    }
                });
            }
            log.info("获取非游戏服务器成功：{}", resBuilder);
            getSession().write(resBuilder);
        } else {
            //获取游戏服，大厅服 （网管需要获取游戏服，大厅服）
            ServerMessage.ServerListResponse.Builder resBuilder = ServerMessage.ServerListResponse.newBuilder();
            ServerMessage.ServerInfo.Builder infoBuild = ServerMessage.ServerInfo.newBuilder();
            ServerManager.getInstance().getServers().forEach((serverType, serverInfoMaps) -> {
                if (serverType.getType() > 100 || serverType.getType() == ServerType.HALL.getType()) {
                    serverInfoMaps.forEach((id, serverInfo) -> {
                        if (serverInfo != null && serverInfo.getSession() != null
                                && serverInfo.getSession().isConnected()) {
                            resBuilder.addServerInfo(builderServerInfo(serverInfo, infoBuild));
                        }
                    });
                }
            });
            log.info("获取游戏服务器|大厅服务器成功：{}", resBuilder);
            getSession().write(resBuilder.build());
        }
    }

    /**
     * 构建服务器信息
     */

    public ServerMessage.ServerInfo builderServerInfo(ServerInfo serverInfo, ServerMessage.ServerInfo.Builder infoBuild) {
        infoBuild.clear();
        infoBuild.setId(serverInfo.getId());
        infoBuild.setFreeMemory(serverInfo.getFreeMemory());
        infoBuild.setHttpport(serverInfo.getHttpPort());
        infoBuild.setIp(serverInfo.getIp());
        infoBuild.setMaxUserCount(serverInfo.getMaxUserCount());
        infoBuild.setName(serverInfo.getName());
        infoBuild.setOnline(serverInfo.getOnline());
        infoBuild.setPort(serverInfo.getPort());
        infoBuild.setState(serverInfo.getState());
        infoBuild.setTotalMemory(serverInfo.getTotalMemory());
        infoBuild.setType(serverInfo.getType());
        infoBuild.setVersion(serverInfo.getVersion());
        infoBuild.setWwwip(serverInfo.getWwwip());
        return infoBuild.build();

    }


}
