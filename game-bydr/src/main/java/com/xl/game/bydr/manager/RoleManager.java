package com.xl.game.bydr.manager;


import com.xl.game.bydr.script.IRoleScript;
import com.xl.game.bydr.struts.role.Role;
import com.xl.game.model.constants.Reason;
import com.xl.game.model.redis.key.BydrKey;
import com.xl.game.redis.manager.JedisManager;
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
 * @QQ 359135103 2020年1月8日 下午4:01:42
 */
@Slf4j
public class RoleManager {


    private static volatile RoleManager roleManager;

    private IRoleScript roleLogin;
    private IRoleScript roleQuit;
    private IRoleScript roleChangeGold;

    private JedisManager jedisManager;


    // 在线的角色
    private final Map<Long, Role> onlineRoles = new ConcurrentHashMap<>();

    private RoleManager() {

        if (roleLogin == null) {
            roleLogin = (IRoleScript) SpringUtil.getBean("roleLogin");
        }
        if (roleQuit == null) {
            roleQuit = (IRoleScript) SpringUtil.getBean("roleQuit");
        }
        if (roleChangeGold == null) {
            roleChangeGold = (IRoleScript) SpringUtil.getBean("roleGoldChange");
        } if (jedisManager == null) {
            jedisManager =  SpringUtil.getBean(JedisManager.class);
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


    public Map<Long, Role> getOnlineRoles() {
        return onlineRoles;
    }

    public Role getRole(long roleId) {
        return onlineRoles.get(roleId);
    }

    /**
     * 登录
     *
     * @param roleId
     * @param reason
     * @return 0 成功
     * @author xuliang
     * @QQ 359135103 2017年8月3日 下午3:49:37
     */

    public void login(long roleId, Reason reason, Consumer<Role> roleConsumer) {
        roleLogin.login(roleId, reason, roleConsumer);
    }


    /**
     * 退出
     *
     * @param role
     * @param reason 原因
     * @author xuliang
     * @QQ 359135103 2017年8月3日 下午3:21:15
     */

    public void quit(Role role, Reason reason) {
        roleQuit.quit(role, reason);
    }


    /**
     * 加载角色数据
     *
     * @param roleId
     * @author xuliang
     * @QQ 359135103 2017年8月3日 下午3:43:59
     */
    public Role loadRoleData(long roleId) {

        Map<String, String> roleMap = jedisManager.getJedisCluster().hgetAll(BydrKey.Role_Map.getKey(roleId));


        if (roleMap == null || roleMap.size() < 1) {
            return null;
        }
        Role role = new Role();
        JsonUtil.map2Object(roleMap, role);
        // TODO 其他角色数据
//		//大厅角色数据
//		RMap<String, Object> hallRole = RedissonManager.getRedissonClient().getMap(HallKey.Role_Map_Info.getKey(roleId), new StringCodec());
//		role.setHallRole(hallRole);

        return role;


    }


    /**
     * 存储角色数据
     *
     * @param role TODO 存储到mongodb
     * @author xuliang
     * @QQ 359135103 2017年8月3日 下午3:22:58
     */

    public void saveRoleData(Role role) {
        String key = BydrKey.Role_Map.getKey(role.getId());
        log.debug("{}存储数据", key);
        jedisManager.getJedisCluster().hmset(key, JsonUtil.object2Map(role));
    }


}
