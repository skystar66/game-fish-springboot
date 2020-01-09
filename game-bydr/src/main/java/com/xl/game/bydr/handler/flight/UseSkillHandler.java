package com.xl.game.bydr.handler.flight;

import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.bydr.BydrFightMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用技能，道具
 * @author xuliang
 * @QQ 359135103
 * 2017年9月18日 下午2:44:50
 */
@HandlerEntity(mid=Mid.MID.UseSkillReq_VALUE,msg=BydrFightMessage.UseSkillRequest.class)
public class UseSkillHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplyAthleticsHandler.class);

	@Override
	public void run() {
		
	}

}
