package com.xl.game.bydr.struts.fish;


import com.alibaba.fastjson.JSON;
import com.xl.game.bydr.struts.room.Room;
import com.xl.game.cache.IMemoryObject;
import com.xl.game.util.Config;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 鱼
 *
 * @author xuliang
 * @date 2020-01-21 QQ:359135103
 */
@Data
public class Fish implements IMemoryObject, Serializable {


    private static final long serialVersionUID = 1L;
    private long id;
    // 配置ID
    private int configId;
    // 出生时间
    private long bornTime;
    // 当前速度
    private int speed;
    private long speedEndTime; // 当前速度结束时间
    private long dieTime;    // 死亡时间
    private List<Integer> trackIds; // 鱼游动路线ID
    private int refreshId; // 刷新ID
    private long topSpeedStartTime; // 极速开始时间
    private int topSpeed; // 极速
    private int formationId;    //阵型ID


    private transient Room room;    //所属的房间


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


    @Override
    public void reset() {
        id = Config.getId();
        bornTime = 0;
        configId = 0;
        dieTime = 0;
        refreshId = 0;
        speed = 0;
        speedEndTime = 0;
        topSpeed = 0;
        topSpeedStartTime = 0;
        trackIds.clear();
        formationId = 0;
        room = null;
    }
}
