package com.xl.game.hall.handler.mail;

import com.xl.game.config.MailConfig;
import com.xl.game.hall.manager.MailManager;
import com.xl.game.hall.script.IMailScript;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.hall.HallChatMessage;
import com.xl.game.model.mongo.dao.MailDao;
import com.xl.game.model.struct.Mail;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;


/**
 * 请求邮件列表
 *
 * @author xuliang
 * @QQ 359135103
 * 2020年1月3日 下午5:18:19
 */
@HandlerEntity(mid = Mid.MID.MailListReq_VALUE, msg = HallChatMessage.MailListRequest.class)
@Slf4j
public class MailListHandler extends TcpHandler {


    private IMailScript mailConfig;

    @Override
    public void run() {

        if (mailConfig == null) {
            mailConfig = SpringUtil.getBean(IMailScript.class);
        }


        //获取公共邮件
        List<Mail> publicMails = MailDao.getPublicMails();
        //获取指定角色的邮件
        List<Mail> mails = MailDao.getMails(rid);
        if (mails != null && publicMails != null) {
            mails.addAll(publicMails);
        }

        HallChatMessage.MailListResponse.Builder builder = HallChatMessage.MailListResponse.newBuilder();


        if (mails != null) {

            Date now = new Date();
            mails.forEach(mail -> {

                //过期邮件检测
                if (mail.getDeleteTime() != null && mail.getDeleteTime().after(now)) {
                    MailDao.deleteMail(mail.getId());
                } else {
                    builder.addMails(MailManager.getInstance(mailConfig).buildMailInfo(mail));
                }
            });
        }
        sendIdMsg(builder.build());

    }
}
