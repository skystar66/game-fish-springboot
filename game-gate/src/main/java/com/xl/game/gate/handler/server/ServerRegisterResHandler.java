package com.xl.game.gate.handler.server;

import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.ServerMessage;
import com.xl.game.thread.ThreadType;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 注册集群服返回
 *
 * @author xuliang
 * @date 2017-04-09 QQ:359135103
 */
@HandlerEntity(mid = Mid.MID.ServerRegisterRes_VALUE, msg = ServerMessage.ServerRegisterResponse.class, thread = ThreadType.SYNC)
@Slf4j
public class ServerRegisterResHandler extends TcpHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRegisterResHandler.class);

    @Override
    public void run() {

        log.info("注册集群服成功!!");

    }

}
