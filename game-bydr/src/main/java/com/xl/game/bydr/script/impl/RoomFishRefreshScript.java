package com.xl.game.bydr.script.impl;

import com.xl.game.bydr.manager.RoomManager;
import com.xl.game.bydr.script.IFishScript;
import com.xl.game.bydr.struts.fish.Fish;
import com.xl.game.bydr.struts.room.Room;
import jodd.util.MathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * 刷新房间鱼群
 * TODO 新刷新规则
 *
 * @author xuliang
 * @QQ 359135103
 * 2017年9月14日 上午9:39:23
 */
@Component
@Slf4j
public class RoomFishRefreshScript implements IFishScript {


    @Override
    public void fishRefresh(Room room) {
        log.info("每秒刷新一次鱼🐟。。。。。。");
        //todo
        Fish fish1 = bornFish(room, MathUtil.randomInt(1, 5), null);
        Fish fish2 = bornFish(room, MathUtil.randomInt(1, 5), null);
        Fish fish3 = bornFish(room, MathUtil.randomInt(1, 5), null);
        Fish fish4 = bornFish(room, MathUtil.randomInt(1, 5), null);
        Fish fish5 = bornFish(room, MathUtil.randomInt(1, 5), null);
        RoomManager.getInstance().broadcastFishEnter(room, fish1, fish2,
                fish3, fish4, fish5);
    }

    @Override
    public void fishDie(Room room) {

    }


    /**
     * 出生鱼
     *
     * @param configId
     * @param fish
     * @return
     * @author xuliang
     * @QQ 359135103
     * 2017年9月25日 下午4:00:39
     */

    private Fish bornFish(Room room, int configId, Consumer<Fish> fishConsumer) {

        Fish fish = new Fish();
        fish.setConfigId(configId);
        if (fishConsumer != null) {
            fishConsumer.accept(fish);
        }

        fish.setRoom(room);
        fish.setTrackIds(new ArrayList<>());//鱼的路线id
        room.getFishMap().put(fish.getId(), fish);
        return fish;
    }


}
