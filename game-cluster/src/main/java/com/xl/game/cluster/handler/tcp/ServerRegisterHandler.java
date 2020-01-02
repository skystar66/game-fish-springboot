package com.xl.game.cluster.handler.tcp;


import com.xl.game.cluster.manager.ServerManager;
import com.xl.game.engine.ServerInfo;
import com.xl.game.engine.ServerType;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.ServerMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * 注册tcp服务器信息
 *
 * @author xuliang
 * @date 2019-12-26 QQ:359135103
 */
@HandlerEntity(mid = Mid.MID.ServerRegisterReq_VALUE, msg = ServerMessage.ServerRegisterRequest.class)
@Slf4j
public class ServerRegisterHandler extends TcpHandler {


    @Override
    public void run() {

        ServerMessage.ServerRegisterRequest registerRequest = getMsg();
        ServerMessage.ServerInfo serverInfo = registerRequest.getServerInfo();
        log.info("服务器 {}_{} 注册", ServerType.valueof(serverInfo.getType()).toString(),
                serverInfo.getId());
        ServerInfo info = ServerManager.getInstance().registerServer(serverInfo, getSession());
        ServerMessage.ServerRegisterResponse.Builder builder = ServerMessage.ServerRegisterResponse.newBuilder();
        ServerMessage.ServerInfo.Builder infoBuilder = ServerMessage.ServerInfo.newBuilder();
        infoBuilder.mergeFrom(serverInfo);
        //反向更新
        infoBuilder.setState(info.getState());
        builder.setServerInfo(infoBuilder);

        //后续实现netty版本
        getSession().write(builder.build());
    }
}
