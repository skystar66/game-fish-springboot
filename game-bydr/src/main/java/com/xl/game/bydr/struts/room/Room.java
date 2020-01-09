package com.xl.game.bydr.struts.room;


import com.xl.game.bydr.struts.fish.Fish;
import com.xl.game.bydr.struts.role.Role;
import com.xl.game.bydr.thread.RoomThread;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 房间信息
 *
 * @author xuliang
 * @date 2017-02-28 QQ:359135103
 */
@Entity(value = "room", noClassnameStored = true)
@Data
public class Room {


    @Id
    private long id;

    /**类型*/
    private int type;
    /**级别*/
    private int rank;

    /**所在线程*/
    private transient RoomThread roomThread;
    private transient long state; // 房间状态
    private transient long robotCreateTime; // 机器人创建时间
    /**房间位置、角色ID*/
    private transient Map<Integer, Role> roles = new HashMap<>();
    private transient Map<Long, Fish> fishMap = new HashMap<>(); // 鱼
    // 多个刷新可能同时存在 value 单条鱼为List<Fish> 鱼群List<List<Fish>>
    private transient Map<Integer, Object> groupFishMap = new HashMap<>();
    private transient Set<Integer> refreshedBoom = new HashSet<>(); // 已经刷新的鱼潮ID
    private transient Map<Integer, Long> refreshTimes = new HashMap<>(); // 鱼刷新时间记录

    private transient long frozenEndTime; // 冰冻结束时间
    private transient long bossStartTime; // boss结束时间
    private transient long bossEndTime; // boss结束时间
    private transient long boomEndTime; // 鱼潮结束时间
    private transient long boomStartTime; // 鱼潮开始时间
    private transient int boomConfigId; // 鱼潮配置ID
    private transient int formationIndex; // 待刷新鱼潮阵型下标

    // 测试统计用
    private long allFireCount; // 房间总共开炮数
    private long hitFireCount; // 房间真实命中鱼数
    private long fireResultCount; // 房间结果请求数




    public long addAllFireCount() {
        return allFireCount++;
    }


    public long addFireResultCount() {
        return fireResultCount++;
    }


}
