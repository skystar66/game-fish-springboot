package com.xl.game.cluster.handler.http;


import com.xl.game.cluster.manager.ServerManager;
import com.xl.game.engine.ServerInfo;
import com.xl.game.engine.ServerType;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.HttpHandler;
import com.xl.game.util.CMDConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;

import java.util.Map;
import java.util.Optional;

/**
 * 获取大厅服务器
 *
 * <p>
 * http://127.0.0.1:8001/server/gate/ip
 * </p>
 *
 * @author xuliang
 * @date 2019-12-26 QQ:359135103
 */
@Slf4j
@HandlerEntity(path = CMDConstants.SERVER_HALL_URL)
public class GetHallIpHandler extends HttpHandler {


    @Override
    public void run() {

        Map<Integer, ServerInfo> serverInfoMap = ServerManager.getInstance().getServers(ServerType.HALL);

        if (MapUtils.isEmpty(serverInfoMap)) {
            getParameter().appendBody("无可用大厅服");
            response();
            return;
        }

        Optional<ServerInfo> serverInfo = serverInfoMap.values().stream().filter(serverInfo1 ->
                serverInfo1.getSession() != null && serverInfo1.getSession().isConnected() &&
                        serverInfo1.getType() == ServerType.HALL.getType()
        ).sorted((s1, s2) -> s1.getOnline() - s2.getOnline()).findFirst();

        if (serverInfo.isPresent()) {
            getParameter().appendBody(serverInfo.get().getIp() + ":" + serverInfo.get().getHttpPort());

        } else {
            getParameter().appendBody("无可用大厅服");
        }
        response();
    }
}
