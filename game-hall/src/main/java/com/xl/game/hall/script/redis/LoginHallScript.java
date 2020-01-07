package com.xl.game.hall.script.redis;

import com.xl.game.model.redis.key.HallChannel;
import com.xl.game.redis.IPubSubScript;
import com.xl.game.redis.manager.JedisPubSubMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 登录大厅脚本
 * <p>
 * 监听玩家登录其他大厅服务器，移除在本服务器的相关信息
 * </p>
 *
 * @author JiangZhiYong
 * @QQ 359135103 2020年1月2日 下午2:19:13
 */
@Component(value = "loginHall")
@Slf4j
public class LoginHallScript implements IPubSubScript {


    @Override
    public void onMessage(String channel, JedisPubSubMessage message) {
        if (!HallChannel.LoginHall.name().equals(channel)) {
            return;
        }
        log.debug(message.toString());
    }
}
