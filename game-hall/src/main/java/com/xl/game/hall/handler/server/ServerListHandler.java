package com.xl.game.hall.handler.server;

import com.xl.game.engine.ServerInfo;
import com.xl.game.hall.server.HallServer;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.ServerMessage;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;


/***
 * 返回服务器列表
 *
 * @author xuliang
 * @QQ 359135103 2017年6月29日 下午1:58:40
 */
@HandlerEntity(mid = Mid.MID.ServerListRes_VALUE,
        msg = ServerMessage.ServerListResponse.class)
@Slf4j
public class ServerListHandler extends TcpHandler {


    private HallServer hallServer;

    @Override
    public void run() {


        log.info("获取网关可用服务器列表成功！！！");
        ServerMessage.ServerListResponse res = getMsg();

        if (res == null) {
            return;
        }


        List<ServerMessage.ServerInfo> list = res.getServerInfoList();

        if (list == null) {
            log.warn("没有可用网关服务器");
            return;
        }

        if (hallServer == null) {
            hallServer = SpringUtil.getBean(HallServer.class);
        }

        // 更新服务器信息
        Set<Integer> serverIds = new HashSet<>();
        list.forEach(serverInfo -> {

            hallServer.getHall2GateClient().updateHallServerInfo(serverInfo);
            serverIds.add(serverInfo.getId());

            //todo
        });
//
        Map<Integer, ServerInfo> hallServers = hallServer.getHall2GateClient()
                .getServers();


        if (hallServers.size() != list.size()) {
            List<Integer> ids = new ArrayList<>(hallServers.keySet());
            ids.removeAll(serverIds);
            ids.forEach(serverId -> {
                hallServer.getHall2GateClient().removeTcpClient(serverId);
            });
        }
    }
}
