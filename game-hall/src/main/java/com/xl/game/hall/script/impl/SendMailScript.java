package com.xl.game.hall.script.impl;

import com.xl.game.hall.script.IMailScript;
import com.xl.game.model.mongo.dao.MailDao;
import com.xl.game.model.struct.Mail;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Consumer;

/**
 * 发送邮件
 *
 * @author xuliang
 * @QQ 359135103 2017年9月21日 下午4:52:57
 */
@Component
public class SendMailScript implements IMailScript {

    @Override
    public void sendMail(long senderId, long receiverId, String title, String content, Mail.MailType type,
                         Consumer<Mail> mailConsumer) {
        Mail mail = new Mail();
        mail.setTitle(title);
        mail.setContent(content);
        mail.setType(type.ordinal());
        mail.setCreateTime(new Date());
        mail.setSenderId(senderId);
        mail.setReceiverId(receiverId);
        mail.setState(Mail.MailState.NEW.ordinal());
        if (mailConsumer != null) {
            mailConsumer.accept(mail);
        }
        MailDao.saveMail(mail);

    }

}
