package com.xl.game.gate.handler.server;

import com.xl.game.gate.manager.ServerManager;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.ServerMessage;
import lombok.extern.slf4j.Slf4j;


/**
 * 返回游戏服务器列表
 *
 * @author xuliang
 * @QQ 359135103 2017年6月30日 下午5:47:25
 */
@HandlerEntity(mid = Mid.MID.ServerListRes_VALUE,
        msg = ServerMessage.ServerListResponse.class)
@Slf4j
public class ServerListHandler extends TcpHandler {


    @Override
    public void run() {

        ServerMessage.ServerListResponse response = getMsg();
        if (response != null) {
            response.getServerInfoList().forEach(info -> {
                ServerManager.getInstance().updateServerInfo(info);
            });
        }

    }
}
