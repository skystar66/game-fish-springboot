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


@HandlerEntity(mid=Mid.MID.GuildInfoReq_VALUE,msg=HallGuildMessage.GuildInfoRequest.class)
public class GuildInfoHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GuildInfoHandler.class);

	@Override
	public void run() {
		HallGuildMessage.GuildInfoRequest request = getMsg();
	}
}