package com.xl.game.gate.handler.server;

import com.xl.game.engine.ServerInfo;
import com.xl.game.engine.ServerType;
import com.xl.game.gate.manager.ServerManager;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.ServerMessage;
import com.xl.game.thread.ThreadType;
import lombok.extern.slf4j.Slf4j;


/**
 * 游戏服循环注册更新
 *
 * @author JiangZhiYong
 * @date 2017-04-09 QQ:359135103
 */
@HandlerEntity(mid = Mid.MID.ServerRegisterReq_VALUE,
        msg = ServerMessage.ServerRegisterRequest.class, thread = ThreadType.SYNC)
@Slf4j
public class ServerRegisterHandler extends TcpHandler {


    @Override
    public void run() {
        ServerMessage.ServerRegisterRequest req = getMsg();
        ServerMessage.ServerInfo serverInfo = req.getServerInfo();
        ServerInfo gameInfo = ServerManager.getInstance().getGameServerInfo(ServerType.valueof(serverInfo.getType()),
                serverInfo.getId());
        if (gameInfo != null) {
            gameInfo.onIoSessionConnect(session);
            log.info("服务器：{} 连接注册到网关服 {} ip:{}", gameInfo.getName(), getSession().getId(),
                    getSession().getRemoteAddress().toString());
        } else {
            log.warn("网关服务没有{}服:{}", ServerType.valueof(serverInfo.getType()).toString(), serverInfo.getId());
        }


    }
}
