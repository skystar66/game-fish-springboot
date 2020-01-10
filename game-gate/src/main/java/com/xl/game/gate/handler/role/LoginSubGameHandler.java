package com.xl.game.gate.handler.role;


import com.xl.game.engine.ServerInfo;
import com.xl.game.engine.ServerType;
import com.xl.game.gate.manager.ServerManager;
import com.xl.game.gate.manager.UserSessionManager;
import com.xl.game.gate.session.UserSession;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.hall.HallLoginMessage;
import com.xl.game.message.system.SystemMessage;
import com.xl.game.model.redis.key.HallKey;
import com.xl.game.redis.manager.JedisManager;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录子游戏
 *
 * @author xuliang
 * @QQ 359135103 2017年6月30日 下午5:03:26
 */
@HandlerEntity(mid = Mid.MID.LoginSubGameReq_VALUE, msg = HallLoginMessage.LoginSubGameRequest.class)
@Slf4j
public class LoginSubGameHandler extends TcpHandler {



    private JedisManager jedisManager;


    @Override
    public void run() {



        log.info("登录子游戏处理器。。。。。。");

        if (jedisManager == null) {
            jedisManager = SpringUtil.getBean(JedisManager.class);
        }

        HallLoginMessage.LoginSubGameRequest request = getMsg();


        long uid = request.getRid();
        ServerType serverType = ServerType.valueof(request.getGameType());
        log.info("[{}]登录游戏{}", uid, serverType);

        UserSession userSession = UserSessionManager.getInstance().getUserSessionByUserId(uid);
        userSession.setServerType(serverType);
        ServerInfo serverInfo = null;


        // 重连登录到之前保留的游戏服
        String key = HallKey.Role_Map_Info.getKey(userSession.getRoleId());
        String gameType = jedisManager.getJedisCluster().hget(key, "gameType");
        if (gameType != null && gameType.equals(serverType.toString())) {
            String gameId = jedisManager.getJedisCluster().hget(key, "gameId");
            if (gameId != null) {
                serverInfo = ServerManager.getInstance().getGameServerInfo(serverType, Integer.parseInt(gameId));
            }
        }
        // 随机选择一个空闲的服务器
        if (serverInfo == null) {
            serverInfo = ServerManager.getInstance().getIdleGameServer(serverType,userSession);
        }

        if (serverInfo == null) {
            SystemMessage.SystemErrorResponse response = ServerManager.getInstance()
                    .buildSystemErrorResponse(SystemMessage.SystemErroCode.GameNotFind, "游戏服务器维护中");
            userSession.getClientSession().write(response);
            log.warn("{} 没有可用服务器", serverType.toString());
            return;
        }

        userSession.setServerType(serverType);
        userSession.setServerId(serverInfo.getId());

        //设置玩家登录游戏服属性
        Map<String, String> redisMap=new HashMap<>(2);
        redisMap.put("gameId", String.valueOf(serverInfo.getId()));
        redisMap.put("gameType", String.valueOf(serverType.getType()));
        jedisManager.getJedisCluster().hmset(key, redisMap);

        userSession.sendToGame(request);





    }
}
