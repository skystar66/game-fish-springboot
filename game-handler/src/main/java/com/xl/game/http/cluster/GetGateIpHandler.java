package com.xl.game.http.cluster;

import com.xl.game.cluster.manager.ServerManager;
import com.xl.game.engine.ServerInfo;
import com.xl.game.engine.ServerType;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.HttpHandler;
import com.xl.game.util.CMDConstants;
import lombok.extern.slf4j.Slf4j;


/**
 * 获取网关服务器
 *
 * <p>
 * http://127.0.0.1:8001/server/gate/ip
 * </p>
 *
 * @author xuliang
 * @date 2019-12-31 QQ:359135103
 */
@Slf4j
@HandlerEntity(path = CMDConstants.SERVER_GATE_URL)
public class GetGateIpHandler extends HttpHandler {


    @Override
    public void run() {

        String version = getString("version");
        ServerInfo serverInfo = ServerManager.getInstance().getIdleGate(version);
        if (serverInfo == null) {
            getParameter().appendBody("无可用网关!");
        } else {
            getParameter().appendBody(serverInfo.getIp() + ":" + serverInfo.getPort());
        }
        response();
    }
}
