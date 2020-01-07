package com.xl.game.redis.listener;

import com.alibaba.fastjson.JSON;
import com.xl.game.redis.IPubSubScript;
import com.xl.game.redis.manager.JedisManager;
import com.xl.game.redis.manager.JedisPubSubMessage;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPubSub;


/**
 * Redis 监听事件
 *
 * @author xuliang
 * @QQ 359135103 2017年7月10日 下午2:00:34
 */
@Slf4j
public class JedisPubListener extends JedisPubSub implements Runnable {


    IPubSubScript pubSubScript;


    JedisManager jedisManager;

    public JedisPubListener() {
    }

    private String[] channels;

    public JedisPubListener(JedisManager jedisManager, String... channels) {
        this.channels = channels;
        this.jedisManager = jedisManager;
    }

    @Override
    public void onMessage(String channel, String message) {
        JedisPubSubMessage jedisPubSubMessage = JSON.parseObject(message, JedisPubSubMessage.class);

        if (jedisPubSubMessage != null) {
            if (channel.equals("LoginHall")) {
                pubSubScript = (IPubSubScript) SpringUtil.getBean("loginHall");
            } else if (channel.equals("HallGoldChange")) {
                pubSubScript = (IPubSubScript) SpringUtil.getBean("goldUpdate");

            }
            pubSubScript.onMessage(channel, jedisPubSubMessage);
        }


    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {


        super.onSubscribe(channel, subscribedChannels);
        log.info("onSubscribe：{},{}", channel, subscribedChannels);

    }

    public void start() {
        Thread thread = new Thread(this, "JedisPubSub");
        thread.start();
    }

    public void stop() {
        unsubscribe();
    }

    @Override
    public void run() {
        try {
            if (channels != null && channels.length > 0) {
                jedisManager.getJedisCluster().subscribe(this, channels);
            }
        } catch (Exception e) {
            log.error(null, e);
        }
    }
}
