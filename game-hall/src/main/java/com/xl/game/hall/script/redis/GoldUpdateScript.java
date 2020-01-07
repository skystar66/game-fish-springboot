package com.xl.game.hall.script.redis;

import com.xl.game.model.redis.key.HallChannel;
import com.xl.game.model.redis.key.HallKey;
import com.xl.game.redis.IPubSubScript;
import com.xl.game.redis.manager.JedisManager;
import com.xl.game.redis.manager.JedisPubSubMessage;
import com.xl.game.util.Config;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 游戏服更新大厅金币
 *
 * @author xuliang
 * @QQ 359135103
 * 2020年1月1日 上午11:19:56
 * @deprecated 子游戏直接操作redis
 */
@Component(value = "goldUpdate")
@Slf4j
public class GoldUpdateScript implements IPubSubScript {

    private JedisManager jedisManager;

    @Override
    public void onMessage(String channel, JedisPubSubMessage message) {

        if (!HallChannel.HallGoldChange.name().equals(channel)) {
            return;
        }
        if (message.getServer() != Config.SERVER_ID) {
            return;
        }
        String key = HallKey.Role_Map_Info.getKey(message.getId());

        if (jedisManager == null) {
            jedisManager = SpringUtil.getBean(JedisManager.class);
        }
        Long finalGold = jedisManager.getJedisCluster().hincrBy(key, "gold", message.getTarget());
        log.info("{}更新{}-->{}", key, message.getTarget(), finalGold);


    }
}
