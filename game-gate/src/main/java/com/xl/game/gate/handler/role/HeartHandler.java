package com.xl.game.gate.handler.role;

import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.system.SystemMessage;
import lombok.extern.slf4j.Slf4j;


/**
 * 心跳
 * @author xuliang
 * @QQ 359135103
 * 2019年12月30日 下午3:39:03
 */
@HandlerEntity(mid=Mid.MID.HeartReq_VALUE,msg=SystemMessage.HeartRequest.class)
@Slf4j
public class HeartHandler extends TcpHandler {

    @Override
    public void run() {
        log.info("网关心跳处理器。。。。。。");
        SystemMessage.HeartResponse.Builder builder=SystemMessage.HeartResponse.newBuilder();
        builder.setServerTime(System.currentTimeMillis());
        session.write(builder.build());
        //LOGGER.info("{}心跳",MsgUtil.getIp(session));
    }
}
