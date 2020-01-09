package com.xl.game.bydr.script.impl;

import com.xl.game.bydr.manager.RoleManager;
import com.xl.game.bydr.script.IRoleScript;
import com.xl.game.bydr.struts.role.Role;
import com.xl.game.model.constants.Reason;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 退出
 * @author xuliang
 * @QQ 359135103
 * 2017年8月4日 下午2:14:32
 */
@Component(value = "roleQuit")
@Slf4j
public class RoleQuitScript implements IRoleScript {

	@Override
	public void quit(Role role, Reason reason) {
		RoleManager roleManager = RoleManager.getInstance();
		
		role.setGameId(0);
		roleManager.getOnlineRoles().remove(role.getId());
		role.syncGold(Reason.UserQuit);
		roleManager.saveRoleData(role);
	}

	
}
