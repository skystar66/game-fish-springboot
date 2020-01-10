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
 * 登录子游戏返回前端
 *
 * @author xuliang
 * @QQ 359135103 2017年6月30日 下午5:03:26
 */
@HandlerEntity(mid = Mid.MID.LoginSubGameRes_VALUE, msg = HallLoginMessage.LoginSubGameResponse.class)
@Slf4j
public class LoginResSubGameHandler extends TcpHandler {


    private JedisManager jedisManager;


    @Override
    public void run() {
        log.info("登录子游戏返回处理器。。。。。。");
        if (jedisManager == null) {
            jedisManager = SpringUtil.getBean(JedisManager.class);
        }
        HallLoginMessage.LoginSubGameResponse response = getMsg();
        UserSession userSession = UserSessionManager.getInstance().getUserSessionByUserId(rid);
        userSession.sendToClient(response);
    }
}
