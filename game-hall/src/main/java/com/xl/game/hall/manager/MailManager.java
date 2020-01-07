package com.xl.game.hall.manager;

import com.xl.game.hall.script.IMailScript;
import com.xl.game.message.hall.HallChatMessage;
import com.xl.game.model.mongo.dao.MailDao;
import com.xl.game.model.struct.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * 邮件
 * <p>
 * 个人邮件单独存储，系统通用邮件只存一封,直接操作mongodb，不缓存
 * </p>
 *
 * @author JiangZhiYong
 * @QQ 359135103 2017年9月21日 下午3:25:17
 */
public class MailManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailManager.class);
    private static volatile MailManager mailManager;


    private IMailScript mailScript;

    private MailManager(IMailScript mailScript) {
        this.mailScript = mailScript;
    }

    public static MailManager getInstance(IMailScript mailScript) {
        if (mailManager == null) {
            synchronized (MailManager.class) {
                if (mailManager == null) {
                    mailManager = new MailManager(mailScript);
                }
            }
        }
        return mailManager;
    }

    public Mail getMail(long mailId) {
        return MailDao.getMail(mailId);
    }

    /**
     * 发送邮件
     *
     * @param title
     * @param content
     * @param type
     * @param mailConsumer
     * @author JiangZhiYong
     * @QQ 359135103 2017年9月21日 下午4:26:31
     */
    public void sendMail(long senderId, long receiverId, String title, String content, Mail.MailType type, Consumer<Mail> mailConsumer) {
        mailScript.sendMail(senderId, receiverId, title, content, type, mailConsumer);
    }

    /**
     * 构建邮箱信息
     *
     * @param mail
     * @return
     * @author JiangZhiYong
     * @QQ 359135103 2017年9月21日 下午5:45:09
     */
    public HallChatMessage.MailInfo buildMailInfo(Mail mail) {
        HallChatMessage.MailInfo.Builder builder = HallChatMessage.MailInfo.newBuilder();
        builder.setContent(mail.getContent());
        builder.setTitle(mail.getTitle());
        builder.setCreateTime(mail.getCreateTime().getTime());
        builder.setId(mail.getId());
        builder.setSenderId(mail.getSenderId());
        builder.setState(mail.getState());
        return builder.build();
    }
}
