package com.xl.game.hall.manager;


import com.xl.game.hall.script.IRoleScript;
import com.xl.game.model.constants.Reason;
import com.xl.game.model.redis.key.HallChannel;
import com.xl.game.model.redis.key.HallKey;
import com.xl.game.model.struct.Role;
import com.xl.game.redis.manager.JedisManager;
import com.xl.game.redis.manager.JedisPubSubMessage;
import com.xl.game.util.JsonUtil;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 角色管理
 *
 * @author xuliang
 * @QQ 359135103 2017年7月7日 下午4:00:37
 */
@Slf4j
public class RoleManager {

    private static volatile RoleManager roleManager;


    private IRoleScript createRole;
    private IRoleScript roleLogin;
    private IRoleScript quitRole;


    private JedisManager jedisManager;


    /**
     * role 数据需要实时存数据库
     */
    private final Map<Long, Role> roles = new ConcurrentHashMap<>();

    private RoleManager() {

        if (jedisManager == null) {
            jedisManager = SpringUtil.getBean(JedisManager.class);
        }


        if (createRole == null) {
            createRole = (IRoleScript)SpringUtil.getBean("createRole");
        }

        if (roleLogin == null) {
            roleLogin = (IRoleScript)SpringUtil.getBean("roleLogin");
        }

        if (quitRole == null) {
            quitRole = (IRoleScript)SpringUtil.getBean("roleQuit");
        }
    }

    public static RoleManager getInstance() {
        if (roleManager == null) {
            synchronized (RoleManager.class) {
                if (roleManager == null) {
                    roleManager = new RoleManager();
                }
            }
        }
        return roleManager;
    }


    /**
     * 创建角色
     *
     * @param userConsumer
     * @return
     */
    public Role createUser(long userId, Consumer<Role> roleConsumer) {


        return createRole.createRole(userId, roleConsumer);


    }


    public Role getRole(long id) {

        Role role = roles.get(id);
        Map<String, String> hgetAll = jedisManager.getJedisCluster().hgetAll(role.getRoleRedisKey());
        // 从redis读取最新数据
        if (hgetAll != null && role != null) {
            JsonUtil.map2Object(hgetAll, role);
        }
        return role;
    }

    /**
     * 登陆
     *
     * @param role
     * @author xuliang
     * @QQ 359135103 2017年9月18日 下午6:23:14
     */
    public void login(Role role, Reason reason) {
        roleLogin.login(role, reason);
    }


    /**
     * 退出
     *
     * @param rid
     * @param reason
     * @author xuliang
     * @QQ 359135103 2017年9月18日 下午6:28:51
     */
    public void quit(long rid, Reason reason) {
        quit(roles.get(rid), reason);
    }

    /**
     * 退出游戏
     *
     * @param role
     * @param reason
     * @author xuliang
     * @QQ 359135103 2017年9月18日 下午6:09:51
     */
    public void quit(Role role, Reason reason) {
        quitRole.quit(role, reason);
    }

    /**
     * 广播金币改变
     *
     * @param roleId
     * @param gold   金币改变量
     * @author xuliang
     * @QQ 359135103 2017年10月17日 上午10:11:59
     */
    public void publishGoldChange(long roleId, int gold) {
        String gameIdStr = jedisManager.getJedisCluster().hget(HallKey.Role_Map_Info.getKey(roleId), "gameId");
        if (gameIdStr != null && !"0".equals(gameIdStr)) {
            JedisPubSubMessage message = new JedisPubSubMessage(roleId, Integer.parseInt(gameIdStr), gold);
            jedisManager.getJedisCluster().publish(HallChannel.HallGoldChange.name(), message.toString());
        }
    }

    public Map<Long, Role> getRoles() {
        return roles;
    }
}
