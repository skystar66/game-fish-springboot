package com.xl.game.bydr.redis;

import com.xl.game.bydr.manager.RoleManager;
import com.xl.game.bydr.struts.role.Role;
import com.xl.game.engine.ServerType;
import com.xl.game.message.ServerMessage;
import com.xl.game.message.bydr.BydrRoomMessage;
import com.xl.game.model.redis.channel.BydrChannel;
import com.xl.game.redis.IPubSubScript;
import com.xl.game.redis.manager.JedisPubSubMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * 报名竞技赛结果
 * <p>广播人数变化 人数满后，进行跨服处理</p>
 *
 * @author JiangZhiYong
 * @QQ 359135103
 * 2017年8月3日 上午9:44:01
 */
@Slf4j
@Component(value = "applyAthleticsResScript")
public class ApplyAthleticsResScript implements IPubSubScript {


    @Override
    public void onMessage(String channel, JedisPubSubMessage message) {

        if (!BydrChannel.ApplyAthleticsRes.name().equals(channel)) {
            return;
        }

        log.info("==竞技赛收到 玩家竞技赛请求===");
        List<Role> roleList = new ArrayList<>();
        //获取 竞技赛内所有玩家信息
        message.getIds().forEach(roleId -> {
            Role role = RoleManager.getInstance().getRole(roleId);
            if (role != null) {
                roleList.add(role);
            }
        });

        if (roleList.size() < 0) {
            log.info("不是该服务器跨服玩家角色！！");
            return;
        }

        BydrRoomMessage.ApplyAthleticsResponse.Builder builder
                = BydrRoomMessage.ApplyAthleticsResponse.newBuilder();
        builder.addAllRoleId(message.getIds());
        BydrRoomMessage.ApplyAthleticsResponse response = builder.build();

        roleList.forEach(role -> {
            role.sendMsg(response);
        });

        //todo 进行跨服处理 机器数量等待 改变
        if (message.getIds().size() >= 4) {
            int targetServerId = message.getServer();
            ServerMessage.ChangeRoleServerRequest.Builder serverBuilder
                    = ServerMessage.ChangeRoleServerRequest.newBuilder();
            roleList.forEach(role -> {
                if (role.getGameId() != targetServerId) {
                    //通知网关服，改变session
                    serverBuilder.clear();
                    serverBuilder.setRoleId(role.getId());
                    serverBuilder.setServerType(ServerType.GAME_BYDR.getType());
                    serverBuilder.setServerId(targetServerId);
                    role.sendMsg(serverBuilder.build());
                    log.info("角色{}跨服", role.getId());
                }
            });
        }
    }
}
