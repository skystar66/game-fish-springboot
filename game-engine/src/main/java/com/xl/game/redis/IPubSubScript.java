package com.xl.game.redis;


import com.xl.game.redis.manager.JedisPubSubMessage;

/**
 * 订阅消息处理器
 * @author xuliang
 * @QQ 359135103
 * 2017年7月10日 上午10:29:29
 */
public interface IPubSubScript {

    /**
     * 消息处理
     * @param channel
     * @param message
     */
    void onMessage(String channel, JedisPubSubMessage message);

}
