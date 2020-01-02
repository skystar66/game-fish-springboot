package com.xl.game.model.server;


import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.HttpHandler;
import com.xl.game.util.CMDConstants;
import com.xl.game.util.Config;
import com.xl.game.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 关闭服务器
 *
 * @author xulaing
 * @QQ 359135103 2017年7月24日 下午1:49:30
 */
@HandlerEntity(path = CMDConstants.EXIT_SERVER_URL)
@Slf4j
public class ExitServerHandler extends HttpHandler {

    @Override
    public void run() {
        //获取请求头中的认证信息
        String auth = getString("auth");
        if (!Config.SERVER_AUTH.equals(auth)) {
            sendMsg("验证失败");
            return;
        }

        String info = String.format("%s关闭服务器", MsgUtil.getIp(getSession()));
        log.info(info);
        sendMsg(info);

//        MailConfig mailConfig = MailManager.getInstance().getMailConfig();
//        String[] recives = mailConfig.getReciveUser().toArray(new String[mailConfig.getReciveUser().size()]);
//        MailManager.getInstance().sendTextMail("服务器关闭", Config.SERVER_NAME + info, recives);
        System.exit(1);

    }
}
