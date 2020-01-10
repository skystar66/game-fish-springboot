package com.xl.game.bydr.script.impl;

import com.xl.game.bydr.script.IRoomScript;
import com.xl.game.bydr.struts.role.Role;
import com.xl.game.bydr.struts.room.Room;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * 退出房间
 * 
 * @author xuliang
 * @QQ 359135103 2017年9月14日 上午10:10:12
 */
@Component(value = "quitRoom")
public class QuitRoomScript implements IRoomScript {

	@Override
	public void quitRoom(Role role, Room room) {
		Iterator<Role> iterator = room.getRoles().values().iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getId() == role.getId()) {
				iterator.remove();
			}
		}
		//TODO 
	}

}
