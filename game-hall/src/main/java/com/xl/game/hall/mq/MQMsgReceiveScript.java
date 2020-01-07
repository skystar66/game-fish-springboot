package com.xl.game.hall.mq;

import com.xl.game.mq.IMQScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * MQ 消息接收
 * @author JiangZhiYong
 * @QQ 359135103
 * 2017年7月28日 下午3:01:03
 */
@Component
public class MQMsgReceiveScript implements IMQScript {
	private static final Logger LOGGER=LoggerFactory.getLogger(MQMsgReceiveScript.class);

	@Override
	public void onMessage(String msg) {
		LOGGER.info(msg);
	}

	
}
