package com.xl.game.cluster.timer;

import com.xl.game.cluster.manager.ServerManager;
import com.xl.game.config.MailConfig;
import com.xl.game.mail.MailManager;
import com.xl.game.script.ITimerEventScript;
import com.xl.game.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;


/**
 * 服务器信息心跳检查
 *
 * @author xulaing
 * @QQ 2755055412 2019年12月20日 下午13:33:20
 */
@Component
public class ServerInfoHeartTimer implements ITimerEventScript {


    @Autowired
    private MailConfig mailConfig;

    @Override
    public void secondHandler(LocalTime localTime) {

    }

    @Override
    public void minuteHandler(LocalTime localTime) {

    }

    @Override
    public void hourHandler(LocalTime localTime) {

        ServerManager.getInstance().getServers().values().forEach(integerServerInfoMap ->
                integerServerInfoMap.forEach(((serverId, serverInfo) -> {
                    // 发送服务器内存使用量不足
                    double freepro = serverInfo.getFreeMemory() * 1.0 / serverInfo.getTotalMemory();
                    if (freepro < 0.1) {
                        StringBuffer sb = new StringBuffer(serverInfo.getName());
                        sb.append("<br>IP:").append(serverInfo.getIp()).append(" 空闲内存:")
                                .append(serverInfo.getFreeMemory())
                                .append(" 总内存:").append(serverInfo.getTotalMemory());
                        MailManager.getInstance(mailConfig).sendTextMailAsync("服务器内存不足", sb.toString(), "2755055412@qq.com");
                    }
                })));
    }

    @Override
    public void dayHandler(LocalDateTime localDateTime) {

    }
}
