package com.xl.game.hall.script.impl;

import com.xl.game.hall.script.IRoleScript;
import com.xl.game.model.mongo.dao.HallInfoDao;
import com.xl.game.model.struct.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;


/**
 * 创建角色
 * @author xuliang
 * @QQ 359135103
 * 2017年7月7日 下午4:34:02
 */
@Component(value = "createRole")
@Slf4j
public class CreateRoleScript implements IRoleScript {


    @Override
    public Role createRole(long userId, Consumer<Role> roleConsumer) {
        Role role = new Role();
        role.setId(HallInfoDao.getRoleId());
        role.setUserId(userId);
        if (roleConsumer != null) {
            roleConsumer.accept(role);
        }
        return role;
    }
}
