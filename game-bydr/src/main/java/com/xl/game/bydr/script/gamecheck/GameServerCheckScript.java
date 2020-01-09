package com.xl.game.bydr.script.gamecheck;

import com.xl.game.bydr.manager.RoleManager;
import com.xl.game.bydr.server.BydrServer;
import com.xl.game.config.MinaServerTcpConfig;
import com.xl.game.message.ServerMessage;
import com.xl.game.model.script.IGameServerCheckScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 服务器状态监测脚本
 *
 * @author xuliang
 * @QQ 359135103 2017年7月10日 下午4:36:25
 */
@Component
public class GameServerCheckScript implements IGameServerCheckScript {


    @Autowired
    BydrServer bydrServer;

    @Autowired
    MinaServerTcpConfig minaServerTcpConfig;

    @Override
    public void buildServerInfo(ServerMessage.ServerInfo.Builder builder) {
        IGameServerCheckScript.super.buildServerInfo(builder);
        builder.setHttpport(minaServerTcpConfig.getHttpPort());
        builder.setIp(minaServerTcpConfig.getIp());
        builder.setOnline(RoleManager.getInstance().getOnlineRoles().size());    //设置在线人数
    }
}
