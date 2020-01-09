package com.xl.game.bydr.handler.server;

import com.xl.game.bydr.server.BydrServer;
import com.xl.game.engine.ServerInfo;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.ServerMessage;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;


/***
 * 返回服务器列表
 *
 * @author xuliang
 * @QQ 359135103 2020年1月7日 下午1:58:40
 */
@HandlerEntity(mid = Mid.MID.ServerListRes_VALUE, msg = ServerMessage.ServerListResponse.class)
@Slf4j
public class ServerListHandler extends TcpHandler {


    private BydrServer bydrServer;

    @Override
    public void run() {

        ServerMessage.ServerListResponse res = getMsg();
        if (res == null) {
            return;
        }

        if (bydrServer == null) {
            bydrServer = SpringUtil.getBean(BydrServer.class);
        }

        List<ServerMessage.ServerInfo> list = res.getServerInfoList();
        if (list == null) {
            log.warn("没有可用大厅服务器");
            return;
        }
        // 更新服务器信息
        Set<Integer> serverIds = new HashSet<>();
        list.forEach(info -> {
            //todo
            bydrServer.updateGateServerInfo(info);
            serverIds.add(info.getId());
        });



        //todo
        Map<Integer, ServerInfo>
                hallServers = bydrServer.getBydr2GateClient()
                .getServers();

        if (hallServers.size() != list.size()) {
            List<Integer> ids = new ArrayList<>(hallServers.keySet());
            ids.removeAll(serverIds);
            ids.forEach(serverId -> {
                bydrServer.getBydr2GateClient()
                        .removeTcpClient(serverId);
            });
        }



    }
}
