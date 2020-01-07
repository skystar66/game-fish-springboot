package com.xl.game.hall.handler.server;

import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.ServerMessage;
import com.xl.game.thread.ThreadType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器注册消息返回
 * 
 * @author xuliang
 * @QQ 359135103 2017年6月29日 上午11:15:57
 */
@HandlerEntity(mid = Mid.MID.ServerRegisterRes_VALUE, msg = ServerMessage.ServerRegisterResponse.class, thread = ThreadType.SYNC)
public class ServerRegisterHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerRegisterHandler.class);

	@Override
	public void run() {
		// TODO Auto-generated method stub
		 LOGGER.debug("更新服务器信息返回");
	}

}
