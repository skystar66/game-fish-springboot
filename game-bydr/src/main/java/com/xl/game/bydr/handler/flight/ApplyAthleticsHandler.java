package com.xl.game.bydr.handler.flight;


import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.bydr.BydrRoomMessage;
import com.xl.game.model.redis.key.BydrWorldChannel;
import com.xl.game.redis.manager.JedisManager;
import com.xl.game.redis.manager.JedisPubSubMessage;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 报名竞技赛
 *
 * @author JiangZhiYong
 * @QQ 359135103 2017年8月3日 上午9:23:04
 */
@HandlerEntity(mid = Mid.MID.ApplyAthleticsReq_VALUE, msg = BydrRoomMessage.ApplyAthleticsRequest.class)
@Slf4j
public class ApplyAthleticsHandler extends TcpHandler {


    private JedisManager jedisManager;

    @Override
    public void run() {


        if (jedisManager == null) {
            jedisManager = SpringUtil.getBean(JedisManager.class);
        }

        BydrRoomMessage.ApplyAthleticsRequest req = getMsg();
        log.info("{}参加竞技赛", rid);
        JedisPubSubMessage msg = new JedisPubSubMessage(rid, req.getType().getNumber(), req.getRank());
        jedisManager.getJedisCluster().publish(BydrWorldChannel.ApplyAthleticsReq.toString(), msg.toString());
    }







}
