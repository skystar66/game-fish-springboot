package com.xl.game.bydr.script.impl;

import com.xl.game.bydr.script.IRoomScript;
import com.xl.game.bydr.struts.room.Room;
import com.xl.game.model.constants.Reason;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalTime;


/**
 * 房间角色监测更新
 *
 * @author xuliang
 * @QQ 359135103
 * 2017年9月26日 下午2:28:29
 */
@Slf4j
@Component(value = "roomRoleUpdateScript")
public class RoomRoleUpdateScript implements IRoomScript {

    @Override
    public void secondHandler(Room room, LocalTime localTime) {
        room.getRoles().forEach((seat, role) -> {
//			//TODO 测试
//			RMap<Long, Item> items = (RMap<Long, Item>)role.getItems();
//			if(items!=null) {
//				Item item = items.get(380220319399937L);
//				item.setNum(item.getNum()+1);
//				items.put(380220319399937L, item);
//				LOGGER.debug("道具：{}",item.toString());
//			}
        });
    }

    @Override
    public void minuteHandler(Room room, LocalTime localTime) {
        room.getRoles().forEach((seat, role) -> {
            //更新金币
            if (role.getWinGold() != 0) {
                role.syncGold(Reason.RoleSync);
            }
        });
    }
}
