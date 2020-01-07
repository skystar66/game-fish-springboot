package com.xl.game.gate.handler.role;


import com.xl.game.engine.ServerType;
import com.xl.game.gate.session.UserSession;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.hall.HallLoginMessage;
import com.xl.game.model.redis.key.HallKey;
import com.xl.game.redis.manager.JedisManager;
import com.xl.game.util.Config;
import com.xl.game.util.MsgUtil;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 退出子游戏
 * // TODO 处理用户session 请求处理，还是返回处理，根据客户端需求
 *
 * @author xuliang
 * @QQ 359135103
 * 2017年7月26日 下午5:34:06
 */
@HandlerEntity(mid = Mid.MID.QuitSubGameReq_VALUE, msg = HallLoginMessage.QuitSubGameRequest.class)
@Slf4j
public class QuitSubGameReqHandler extends TcpHandler {

    private JedisManager jedisManager;


    @Override
    public void run() {

        log.info("退出子游戏处理器。。。。。。");

        if (jedisManager == null) {
            jedisManager = SpringUtil.getBean(JedisManager.class);
        }
        HallLoginMessage.QuitSubGameRequest request = getMsg();
        Object attribute = getSession().getAttribute(Config.USER_SESSION);
        if (attribute == null) {
            log.warn("{} 无用户会话", MsgUtil.getIp(getSession()));
            return;
        }

        if (request == null) {
            request = HallLoginMessage.QuitSubGameRequest.newBuilder().build();
        }

        UserSession userSession = (UserSession) attribute;
        userSession.sendToGame(request);
        userSession.removeGame();

        //清空角色服务器ID，服务类型，网关统一处理，避免游戏服重复处理
        String key = HallKey.Role_Map_Info.getKey(userSession.getRoleId());
        Map<String, String> redisMap = new HashMap<String, String>();
        redisMap.put("gameType", ServerType.NONE.toString());
        redisMap.put("gameId", String.valueOf(0));
        jedisManager.getJedisCluster().hmset(key, redisMap);
    }
}
