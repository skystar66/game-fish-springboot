package com.xl.game.bydr.redis;

import com.xl.game.bydr.manager.RoleManager;
import com.xl.game.bydr.struts.role.Role;
import com.xl.game.model.constants.Reason;
import com.xl.game.model.redis.channel.BydrChannel;
import com.xl.game.redis.IPubSubScript;
import com.xl.game.redis.manager.JedisPubSubMessage;
import com.xl.game.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 大厅通知角色数据改变
 *
 * @author xuliang
 * @QQ 359135103 2017年10月17日 上午10:20:00
 */
@Slf4j
@Component(value = "hallRoleDataChangeScript")
public class HallRoleDataChangeScript implements IPubSubScript {


    @Override
    public void onMessage(String channel, JedisPubSubMessage message) {

        if (!channel.startsWith("Hall")) { // channel必须以Hall开头
            return;
        }

        if(message.getServer()!=Config.SERVER_ID) {
            return;
        }


        Role role = RoleManager.getInstance().getRole(message.getId());
        if(role==null) {
            log.warn("角色[{}]已退出游戏:{} {}更新失败",message.getId(),channel,message.toString());
            return;
        }


        switch (BydrChannel.valueOf(channel)) {
            case HallGoldChange:
                role.changeGold(message.getTarget(), Reason.HallGoldChange);
                break;

            default:
                break;
        }







    }
}
