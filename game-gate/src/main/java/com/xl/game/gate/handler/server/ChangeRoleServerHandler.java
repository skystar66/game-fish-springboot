package com.xl.game.gate.handler.server;


import com.xl.game.engine.ServerInfo;
import com.xl.game.engine.ServerType;
import com.xl.game.gate.manager.ServerManager;
import com.xl.game.gate.manager.UserSessionManager;
import com.xl.game.gate.session.UserSession;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.ServerMessage;
import com.xl.game.message.hall.HallLoginMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * 修改角色在网关服所连接的其他服务器信息
 *
 * @author xuliang
 * @QQ 359135103 2017年8月1日 下午4:01:09 TODO 待测试
 */
@HandlerEntity(mid = Mid.MID.ChangeRoleServerReq_VALUE, msg = ServerMessage.ChangeRoleServerRequest.class)
@Slf4j
public class ChangeRoleServerHandler extends TcpHandler {


    @Override
    public void run() {


        log.info("修改角色在网关服所连接的其他服务器信息处理器。。。。。。");


        ServerMessage.ChangeRoleServerRequest req = getMsg();
        ServerMessage.ChangeRoleServerResponse.Builder builder = ServerMessage.ChangeRoleServerResponse.newBuilder();
        builder.setResult(0);

        long roleId = req.getRoleId();
        //获取角色所在的session
        UserSession userSession = UserSessionManager.getInstance().getUserSessionbyRoleId(roleId);

        log.debug("角色{}进行跨服", roleId);

        if (userSession == null) {
            builder.setResult(1);//用户未登录
        } else {
            ServerType serverType = ServerType.valueof(req.getServerType());

            ServerInfo serverInfo = null;
            //todo 目标服务器
            if (req.getServerId() < 1) {
                //找空闲服务器
                serverInfo = ServerManager.getInstance().getIdleGameServer(serverType, userSession);
            } else {
                serverInfo = ServerManager.getInstance().getGameServerInfo(serverType, req.getServerId());
            }
            if (serverInfo == null) {
                builder.setResult(2);//未找到服务器
            } else {

                if (serverType == ServerType.HALL) {
                    userSession.setHallServerId(serverInfo.getId());
                    userSession.setHallSession(serverInfo.getMostIdleIoSession());
                    //TODO 重发连接大厅消息
                } else {
                    userSession.setGameSession(serverInfo.getMostIdleIoSession());
                    userSession.setServerId(serverInfo.getId());
                    userSession.setServerType(serverType);
                    //发送登录消息
                    HallLoginMessage.LoginSubGameRequest.Builder loginGameBuilder = HallLoginMessage.LoginSubGameRequest.newBuilder();
                    loginGameBuilder.setGameType(serverType.getType());
                    loginGameBuilder.setRid(roleId);
                    loginGameBuilder.setType(2);
                    userSession.sendToGame(loginGameBuilder.build());
                }
            }
        }
        log.debug("跨服结果：{}", builder.build().toString());
        sendIdMsg(builder.build());
    }
}
