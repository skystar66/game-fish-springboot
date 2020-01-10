package com.xl.game.cluster.handler.http;


import com.xl.game.cluster.manager.ServerManager;
import com.xl.game.engine.ServerInfo;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.HttpHandler;
import com.xl.game.util.CMDConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务器列表
 * <p>
 * http://127.0.0.1:8001/server/list
 * </p>
 *
 * @author xuliang
 * @QQ 2755055412 2019年12月26日 上午11:04:06
 */
@Slf4j
@HandlerEntity(path = CMDConstants.SERVER_LIST_URL)
public class ServerListHandler extends HttpHandler {


    @Override
    public void run() {


        log.info("http handler 获取所有服务器");

        List<ServerInfo> serverInfoList = new ArrayList<>();
        ServerManager.getInstance().getServers().values().forEach(serverInfoMap -> {

            serverInfoMap.forEach((serverId, serverInfo) -> {
                serverInfoList.add(serverInfo);
            });

        });

        sendMsg(serverInfoList);

    }
}
