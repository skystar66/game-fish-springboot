package com.xl.game.hall.script;


import com.xl.game.model.struct.Mail;

import java.util.function.Consumer;


/**
 * 邮件
 * @author xuliang
 * @QQ 359135103
 * 2017年9月21日 下午4:45:19
 */
public interface IMailScript {

	/**
	 * 发送邮件
	 * @author xuliang
	 * @QQ 359135103
	 * 2017年9月21日 下午4:26:31
	 * @param title
	 * @param content
	 * @param type
	 * @param mailConsumer
	 */
	default void sendMail(long senderId, long receiverId, String title, String content, Mail.MailType type, Consumer<Mail> mailConsumer) {
		
	}
}
