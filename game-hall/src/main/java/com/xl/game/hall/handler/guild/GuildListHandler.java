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


@HandlerEntity(mid=Mid.MID.GuildListReq_VALUE,msg=HallGuildMessage.GuildListRequest.class)
public class GuildListHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GuildListHandler.class);

	@Override
	public void run() {
		HallGuildMessage.GuildListRequest request = getMsg();
	}
}