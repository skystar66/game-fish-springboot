package com.xl.game.gate.handler.role;


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
import com.xl.game.thread.ThreadType;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 登陆返回
 * <p>处理用户的连接session,设置UserSession用户ID，角色ID</p>
 *
 * @author xuliang
 * @mail 359135103@qq.com
 */
@HandlerEntity(mid = Mid.MID.LoginRes_VALUE, desc = "登陆",
        thread = ThreadType.IO, msg = HallLoginMessage.LoginResponse.class)
@Slf4j
public class LoginResHandler extends TcpHandler {


    private JedisManager jedisManager;
    @Override
    public void run() {

        log.info("登录返回处理器。。。。。。");


        if (jedisManager == null) {

            jedisManager = SpringUtil.getBean(JedisManager.class);
        }
        HallLoginMessage.LoginResponse response = getMsg();
        UserSession userSession = UserSessionManager.getInstance().getUserSessionBySessionId(response.getSessionId());

        if (userSession == null) {
            session.write(
                    ServerManager.getInstance().buildSystemErrorResponse(SystemMessage.SystemErroCode.ConectReset, "连接会话已失效，请重连"));
            log.warn("连接会话已失效，请重连");
            return;
        }
        String key = HallKey.Role_Map_Info.getKey(userSession.getRoleId());
        jedisManager.hset(key, "hallId", String.valueOf(userSession.getHallServerId()));
        UserSessionManager.getInstance().loginHallSuccess(userSession, response.getUid(), response.getRid());
        userSession.sendToClient(response);

    }
}
