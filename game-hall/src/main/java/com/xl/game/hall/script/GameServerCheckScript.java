package com.xl.game.hall.script;

import com.xl.game.config.MinaServerHttpConfig;
import com.xl.game.hall.manager.RoleManager;
import com.xl.game.message.ServerMessage;
import com.xl.game.model.script.IGameServerCheckScript;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 服务器状态监测脚本
 *
 * @author xuliang
 * @QQ 359135103 2017年7月10日 下午4:36:25
 */
@Component
@Slf4j
public class GameServerCheckScript implements IGameServerCheckScript {


    @Autowired
    MinaServerHttpConfig minaServerHttpConfig;

    @Override
    public void buildServerInfo(ServerMessage.ServerInfo.Builder builder) {

        IGameServerCheckScript.super.buildServerInfo(builder);
        builder.setHttpport(minaServerHttpConfig.getHttpPort());
        builder.setIp(minaServerHttpConfig.getIp());
        builder.setOnline(RoleManager.getInstance().getRoles().size());	//设置在线人数 TODO
    }
}
