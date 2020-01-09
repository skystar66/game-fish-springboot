package com.xl.game.bydr.script.impl;

import com.xl.game.bydr.script.IRoleScript;
import com.xl.game.bydr.struts.role.Role;
import com.xl.game.model.constants.Reason;
import com.xl.game.model.redis.key.HallKey;
import com.xl.game.redis.manager.JedisManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 修改角色金币
 *
 * @author xuliang
 * @QQ 359135103 2017年9月25日 下午5:21:32
 */
@Component(value = "roleGoldChange")
@Slf4j
public class RoleGoldChangeScript implements IRoleScript {

    @Autowired
    JedisManager jedisManager;

    @Override
    public void changeGold(Role role, int add, Reason reason) {

        long gold = role.getGold() + add;

        if (gold < 0 || gold > Long.MAX_VALUE) {
            log.warn("玩家更新金币异常,{}+{}={}", role.getGold(), add, gold);
            role.setGold(0);
        }

        role.setGold(gold);
        if (reason == Reason.RoleFire) {
            role.setWinGold(role.getWinGold() + add);
        }
    }


    @Override
    public void syncGold(Role role, Reason reason) {
        if (role.getWinGold() != 0) {
            String key = HallKey.Role_Map_Info.getKey(role.getId());
            long addAndGet = jedisManager.getJedisCluster().hincrBy(key, "gold", role.getWinGold());
            role.setWinGold(0);
            log.info("更新后金币为{}", addAndGet);
        }
    }
}
