package com.xl.game.bydr.script.impl;

import com.xl.game.bydr.manager.RoleManager;
import com.xl.game.bydr.script.IRoleScript;
import com.xl.game.bydr.struts.role.Role;
import com.xl.game.model.constants.Reason;
import com.xl.game.model.redis.key.HallKey;
import com.xl.game.redis.manager.JedisManager;
import com.xl.game.util.Config;
import com.xl.game.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;


/**
 * 登录
 *
 * @author xuliang
 * @QQ 359135103 2017年8月4日 下午2:14:53
 */
@Component(value = "roleLogin")
@Slf4j
public class RoleLoginScript implements IRoleScript {


    @Autowired
    JedisManager jedisManager;

    @Override
    public void login(long roleId, Reason reason, Consumer<Role> roleConsumer) {

        Role role = RoleManager.getInstance().getRole(roleId);

        if (role == null) {
            role = new Role();
            role.setId(roleId);
        }

        role.setGameId(Config.SERVER_ID);
        if (roleConsumer != null) {
            roleConsumer.accept(role);
        }

        //设置在线用户
        RoleManager.getInstance().getOnlineRoles().put(roleId, role);

        // 同步大厅角色数据，昵称、头像等
        syncHallData(role);
        tempInit(role);
    }


    /**
     * 同步大厅数据
     *
     * @param role
     * @author xuliang
     * @QQ 359135103 2017年9月26日 下午2:44:18
     */
    private void syncHallData(Role role) {

        //同步角色
        String key = HallKey.Role_Map_Info.getKey(role.getId());
        Map<String, String> hgetAll = jedisManager.getJedisCluster().hgetAll(key);
        if (hgetAll == null) {
            log.warn("{}为找到角色数据", key);
            return;
        }

        com.xl.game.model.struct.Role hallRole = new com.xl.game.model.struct.Role();
        JsonUtil.map2Object(hgetAll, hallRole);
        role.setNick(hallRole.getNick());
        role.setGold(hallRole.getGold());
        role.setLevel(hallRole.getLevel());
        RoleManager.getInstance().saveRoleData(role);


//		//加载大厅数据
//		// 道具
//		RMap<Long, Item> items = RedissonManager.getRedissonClient()
//				.getMap(HallKey.Role_Map_Packet.getKey(role.getId()),new FastJsonCodec(Long.class,Item.class));
//		role.setItems(items);


    }

    /**
     * 临时初始化
     *
     * @param role
     * @author xuliang
     * @QQ 359135103 2017年9月25日 下午5:31:37
     */

    private void tempInit(Role role) {

        // 赠送金币
        if (role.getGold() < 100) {
            role.changeGold(100000, Reason.RoleFire);
        }
    }


}
