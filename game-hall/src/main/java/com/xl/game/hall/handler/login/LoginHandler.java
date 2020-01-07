package com.xl.game.hall.handler.login;


import com.xl.game.hall.manager.RoleManager;
import com.xl.game.hall.manager.UserManager;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.hall.HallLoginMessage;
import com.xl.game.model.constants.Reason;
import com.xl.game.model.mongo.dao.RoleDao;
import com.xl.game.model.mongo.dao.UserDao;
import com.xl.game.model.redis.key.HallChannel;
import com.xl.game.model.redis.key.HallKey;
import com.xl.game.model.struct.Role;
import com.xl.game.model.struct.User;
import com.xl.game.redis.manager.JedisManager;
import com.xl.game.redis.manager.JedisPubSubMessage;
import com.xl.game.thread.ThreadType;
import com.xl.game.util.Config;
import com.xl.game.util.JsonUtil;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 登陆
 *
 * @author xuliang
 * @mail 359135103@qq.com
 */

@HandlerEntity(mid = Mid.MID.LoginReq_VALUE, desc = "登陆",
        thread = ThreadType.IO, msg = HallLoginMessage.LoginRequest.class)
@Slf4j
public class LoginHandler extends TcpHandler {


    private JedisManager jedisManager;

    @Override
    public void run() {

        if (jedisManager == null) {
            jedisManager = SpringUtil.getBean(JedisManager.class);
        }

        HallLoginMessage.LoginRequest request = getMsg();

        switch (request.getLoginType()) {
            case ACCOUNT:
                loginWithAccount(request);
                break;
        }

    }


    public void loginWithAccount(HallLoginMessage.LoginRequest request) {

        log.info("用户：{} 登录", request.getAccount());

        if (request.getAccount() == null || request.getPassword() == null) {
            // TODO
            // 验证
            return;
        }
        User user = UserDao.findByAccount(request.getAccount());

        if (user == null) {
            user = UserManager.getInstance().createUser(u -> {
                u.setAccunt(request.getAccount());
                u.setPassword(request.getPassword());
            });
        }
        UserDao.saveUser(user);

        Role role = RoleDao.getRoleByUserId(user.getId());

        if (role == null) {
            role = RoleManager.getInstance().createUser(user.getId(), r -> {
                r.setNick("xl");
                r.setGem(1000);
            });

        } else {
            //以redis数据为准
            Map<String, String> hgetAll = jedisManager.getJedisCluster().hgetAll(HallKey.Role_Map_Info.getKey(role.getId()));
            JsonUtil.map2Object(hgetAll, role);
        }


        log.info("{}_key:{}", role.getNick(), HallKey.Role_Map_Info.getKey(role.getId()));


        RoleManager.getInstance().login(role, Reason.UserLogin);


        // 广播给其他服务器
        JedisPubSubMessage message = new JedisPubSubMessage(role.getId(), Config.SERVER_ID);
        jedisManager.getJedisCluster().publish(HallChannel.LoginHall.name(), message.toString());


        HallLoginMessage.LoginResponse.Builder builder = HallLoginMessage.LoginResponse.newBuilder();
        builder.setIsOk(true);
        builder.setUid(user.getId());
        builder.setRid(role.getId());
        builder.setSessionId(request.getSessionId());
        sendIdMsg(builder.build());

    }


}
