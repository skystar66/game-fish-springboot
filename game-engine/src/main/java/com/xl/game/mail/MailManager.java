package com.xl.game.mail;


import com.xl.game.config.MailConfig;
import lombok.extern.slf4j.Slf4j;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件发送
 *
 * @author xuliang
 * @QQ 2755055412 2019年12月06日 下午5:09:21
 */
@Slf4j
public class MailManager {


    private static volatile MailManager mailManager;

    private MailConfig mailConfig;


    public MailManager() {

    }

    public MailManager(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }

    public static MailManager getInstance(MailConfig mailConfig) {
        if (mailManager == null) {
            synchronized (MailManager.class) {
                if (mailManager == null) {
                    mailManager = new MailManager(mailConfig);
                }
            }
        }
        return mailManager;
    }

    /**
     * 异步发送邮件 <br>
     * 邮件发送比较耗时
     *
     * @param title
     * @param content
     * @author JiangZhiYong
     * @QQ 359135103 2017年8月22日 下午5:24:57
     */
    public void sendTextMailAsync(String title, String content, String... recives) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendTextMail(title, content, recives);
            }
        }).start();
    }

    /**
     * 发送文本邮件
     *
     * @param title
     * @param content
     * @author JiangZhiYong
     * @QQ 359135103 2017年8月22日 下午5:36:45
     */
    public void sendTextMail(String title, String content, String... recives) {
        try {
            Properties props = new Properties();
            // 使用smtp：简单邮件传输协议
            props.put("mail.smtp.ssl.enable", mailConfig.getMailSmtpSslEnable());
            props.put("mail.smtp.host", mailConfig.getMailSmtpHost());// 存储发送邮件服务器的信息
            props.put("mail.smtp.auth", mailConfig.getMailSmtpAuth());// 同时通过验证

            Session session = Session.getInstance(props);// 根据属性新建一个邮件会话
            // session.setDebug(true); //有他会打印一些调试信息。
            MimeMessage message = new MimeMessage(session);// 由邮件会话新建一个消息对象

            message.setFrom(new InternetAddress(mailConfig.getSendUser()));// 设置发件人的地址
            for (String recive : recives) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recive));
            }
            // 设置标题
            message.setSubject(title);
            // 设置信件内容
            message.setText(content, "utf-8"); // 发送文本文件
            // message.setContent(context, "text/html;charset=utf-8");
            // //发送HTML邮件，内容样式比较丰富
            message.setSentDate(new Date());// 设置发信时间
            message.saveChanges();// 存储邮件信息
            // 发送邮件
            Transport transport = null;
            try {
                transport = session.getTransport("smtp");
                transport.connect(mailConfig.getSendUser(), mailConfig.getPassword());
                // 发送邮件,其中第二个参数是所有已设好的收件人地址
                transport.sendMessage(message, message.getAllRecipients());
            } catch (Exception e) {
                log.error("发送邮件失败", e);
            } finally {
                if (transport != null) {
                    transport.close();
                }
            }
        } catch (AddressException e) {
            log.error("邮件", e);
        } catch (MessagingException e) {
            log.error("邮件", e);
        }
    }


}
