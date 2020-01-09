/**工具生成，请遵循规范<br>
 @auther JiangZhiYong
 */
package com.xl.game.bydr.handler.flight;

import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.bydr.BydrFightMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@HandlerEntity(mid=Mid.MID.GunLeveUpReq_VALUE,msg=BydrFightMessage.GunLeveUpRequest.class)
public class GunLeveUpHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GunLeveUpHandler.class);

	@Override
	public void run() {
		BydrFightMessage.GunLeveUpRequest request = getMsg();
	}
}