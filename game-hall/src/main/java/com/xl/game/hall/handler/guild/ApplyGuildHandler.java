/**工具生成，请遵循规范<br>
 @auther JiangZhiYong
 */
package com.xl.game.hall.handler.guild;

import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.hall.HallGuildMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@HandlerEntity(mid = Mid.MID.ApplyGuildReq_VALUE, msg = HallGuildMessage.ApplyGuildRequest.class)
public class ApplyGuildHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplyGuildHandler.class);

	@Override
	public void run() {
		HallGuildMessage.ApplyGuildRequest request = getMsg();
	}
}