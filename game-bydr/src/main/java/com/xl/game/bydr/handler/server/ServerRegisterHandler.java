package com.xl.game.bydr.handler.server;

import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.ServerMessage;
import com.xl.game.server.ServerState;
import com.xl.game.thread.ThreadType;
import lombok.extern.slf4j.Slf4j;


/**
 * 服务器注册消息返回
 *
 * @author xuliang
 * @QQ 359135103 2020年1月7日 上午11:15:57
 */
@Slf4j
@HandlerEntity(mid = Mid.MID.ServerRegisterRes_VALUE, msg = ServerMessage.ServerRegisterResponse.class, thread = ThreadType.SYNC)
public class ServerRegisterHandler extends TcpHandler {

    @Override
    public void run() {

        ServerMessage.ServerRegisterResponse res = getMsg();
        if (res != null && res.getServerInfo() != null) {
            if (ServerState.MAINTAIN.getState() == res.getServerInfo().getState()) {
                log.info("服务器状态变为维护");
            }
            // TODO 更新服务器状态信息，处理维护状态下的逻辑
        }
         log.debug("更新服务器信息返回");


    }
}
