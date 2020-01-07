package com.xl.game.hall.handler.mail;

import com.xl.game.hall.manager.MailManager;
import com.xl.game.hall.script.IMailScript;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.hall.HallChatMessage;
import com.xl.game.model.mongo.dao.MailDao;
import com.xl.game.model.struct.Mail;
import com.xl.game.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 修改邮件状态
 * 
 * @author xuliang
 * @QQ 359135103 2017年9月21日 下午5:20:08
 */
@HandlerEntity(mid = Mid.MID.ModifyMailReq_VALUE, msg = HallChatMessage.ModifyMailRequest.class)
public class ModifyMailHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ModifyMailHandler.class);

	private IMailScript mailScript;

	@Override
	public void run() {

		if (mailScript == null) {
			mailScript = SpringUtil.getBean(IMailScript.class);
		}

		HallChatMessage.ModifyMailRequest req = getMsg();
		Mail mail = MailManager.getInstance(mailScript).getMail(req.getMailId());
		if (mail == null) {
			LOGGER.warn("{}请求邮件{}已不存在", rid, req.getMailId());
			return;
		}
		if (mail.getType() == Mail.MailType.PRIVATE.ordinal() && mail.getReceiverId() != rid) {
			LOGGER.warn("{}请求邮件{}不属于自己", rid, req.getMailId());
			return;
		}
		if (req.getState() <= mail.getState()) {
			LOGGER.warn("{}请求状态{}非法，邮件当前状态为{}", rid, req.getState(), mail.getState());
			return;
		}
		mail = MailDao.modifyMailState(mail.getId(), req.getState());
		if (mail.getState() != req.getState()) {
			LOGGER.warn("{}更新状态{}失败", rid, req.getState());
			return;
		}
		
		//TODO 领取物品等逻辑
		
		HallChatMessage.ModifyMailResponse.Builder builder = HallChatMessage.ModifyMailResponse.newBuilder();
		builder.setResult(0);
		sendIdMsg(builder.build());
	}

}
