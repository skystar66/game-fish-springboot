package com.xl.game.gate.init;

import com.xl.game.redis.manager.JedisManager;

public class MongoInit implements Runnable {

    private JedisManager jedisManager;


    public MongoInit(JedisManager jedisManager){

        this.jedisManager=jedisManager;
    }


    @Override
    public void run() {




    }
}
