package com.xl.game.model.server;

import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.HttpHandler;
import com.xl.game.util.CMDConstants;
import com.xl.game.util.Config;
import com.xl.game.util.SysUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 获取线程信息
 * <p>http://192.168.0.17:9002/server/thread/info?auth=daa0cf5b-e72d-422c-b278-450b28a702c6</p>
 *
 * @author xulaing
 * @QQ 359135103 2019年12月24日 下午2:38:44
 */
@HandlerEntity(path = CMDConstants.THREAD_INFO_URL)
@Slf4j
public class ThreadInfoHandler extends HttpHandler {


    @Override
    public void run() {
        String auth = getString("auth");
        if (!Config.SERVER_AUTH.equals(auth)) {
            sendMsg("验证失败");
            return;
        }

        String info = SysUtil.threadInfo("<br>");
        log.info("获取线程池信息成功！");
        info = info.trim().replaceAll("/n", "").replaceAll("/t", "&nbsp;&nbsp;");
        sendMsg(info);
    }
}
