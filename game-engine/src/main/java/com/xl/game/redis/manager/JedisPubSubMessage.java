package com.xl.game.redis.manager;


import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.Set;

/**
 *  redis的订阅发布消息
 * @author xuliang
 * @QQ 359135103
 * 2017年7月10日 上午10:27:29
 */
@Data
public class JedisPubSubMessage {


    //消息的目标
    private long id;
    //消息的目标集
    private Set<Long> ids;
    //消息来源的服务器
    private int server;

    //消息目标的服务器
    private int target;
    //消息的值，json格式
    private String json;
    //属性 key
    private String key;
    //属性 int key
    private int intValue;
    //属性 long key
    private long longValue;

    public JedisPubSubMessage() {
    }

    public JedisPubSubMessage(long id) {
        this.id = id;
    }

    public JedisPubSubMessage(long id, int server) {
        this.id = id;
        this.server = server;
    }

    public JedisPubSubMessage(long id, int server, int target) {
        this.id = id;
        this.server = server;
        this.target = target;
    }

    public JedisPubSubMessage(long id, int server, int target, String key, int intValue) {
        this.id = id;
        this.server = server;
        this.target = target;
        this.key = key;
        this.intValue = intValue;
    }

    public JedisPubSubMessage(long id, int server, int target, String key, long longValue) {
        this.id = id;
        this.server = server;
        this.target = target;
        this.key = key;
        this.longValue = longValue;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
