package com.xl.game.http.cluster;

import com.xl.game.cluster.manager.ServerManager;
import com.xl.game.engine.ServerInfo;
import com.xl.game.engine.ServerType;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.HttpHandler;
import com.xl.game.server.ServerState;
import com.xl.game.util.CMDConstants;
import com.xl.game.util.Config;
import com.xl.game.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 设置服务器状态
 * <p>
 * http://127.0.0.1:8001/server/state
 * </p>
 *
 * @author xuliang
 * @QQ 2755055412 2019年12月26日 下午3:16:56
 */
@Slf4j
@HandlerEntity(path = CMDConstants.SET_SERVER_STAT)
public class SetServerStateHandler extends HttpHandler {

    @Override
    public void run() {

        String auth = getString("auth");
        if (!Config.SERVER_AUTH.equals(auth)) {
            sendMsg("验证失败");
            return;
        }
        //服务类型
        int serverType = getInt("serverType");
        //服务id
        int serverId = getInt("serverId");
        //服务状态
        int serverState = getInt("serverState");
        ServerInfo serverInfo = ServerManager.getInstance().getServer(ServerType.valueof(serverType), serverId);
        if (serverInfo == null) {
            sendMsg(String.format("服务器 %d %d 未启动", serverType, serverId));
            return;
        }
        serverInfo.setState(serverState);
        log.info("{}设置服务器{}_{} 状态：{}", MsgUtil.getIp(getSession()), serverType, serverId, serverState);
        sendMsg("服务器状态设置为：" + ServerState.valueOf(serverState));
    }
}
