package com.xl.game.hall.script.impl;

import com.xl.game.hall.manager.RoleManager;
import com.xl.game.hall.script.IRoleScript;
import com.xl.game.model.constants.Reason;
import com.xl.game.model.mongo.dao.RoleDao;
import com.xl.game.model.struct.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 角色退出游戏
 *
 * @author XULIANG
 * @QQ 359135103 2017年9月18日 下午6:01:57
 */
@Component(value = "roleQuit")
@Slf4j
public class RoleQuitScript implements IRoleScript {


    @Override
    public void quit(Role role, Reason reason) {
        RoleManager.getInstance().getRoles().remove(role.getId());

        saveData(role);

    }

    /**
     * 存储数据
     *
     * @author xuliang
     * @QQ 359135103 2017年9月18日 下午6:06:41
     * @param role
     */
    private void saveData(Role role) {
        // ----- mongodb -----
        RoleDao.saveRole(role);

        //----- redis-----

        //角色 数据不保存，需要实时存储
//		String key = HallKey.Role_Map_Info.getKey(role.getId());
//		Map<String, String> map = JsonUtil.object2Map(role);
//		JedisManager.getJedisCluster().hmset(key, map);
    }



}
