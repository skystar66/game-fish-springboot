package com.xl.game.model.struct;

import com.alibaba.fastjson.annotation.JSONField;
import com.xl.game.model.redis.key.HallKey;
import com.xl.game.redis.manager.JedisManager;
import com.xl.game.struts.Person;
import com.xl.game.util.JsonUtil;
import com.xl.game.util.ReflectUtil;
import com.xl.game.util.SpringUtil;
import org.mongodb.morphia.annotations.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 大厅玩家角色实体 <br>
 * 大厅角色数据实时存储,小心消息覆盖
 *
 * @author JiangZhiYong
 * @note 其他子游戏单独在另外定义角色实体，要求各子游戏数据独立，分开
 * @mail 359135103@qq.com
 */
@Entity(value = "role", noClassnameStored = true)
public class Role extends Person {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(Role.class);

    /**
     * setter 方法集合
     */
    @JSONField(serialize = false)
    protected static final transient Map<String, Method> WRITEMETHODS = ReflectUtil.getReadMethod(Role.class);

    /**
     * 所在游戏服类型
     */
    @JSONField
    private int gameType;

    /** 背包 *//*
	private transient Map<Long, Item> items = new ConcurrentHashMap<Long, Item>();*/

    /**
     * 存储玩家基本属性到redis
     *
     * @param propertiesName
     */
    @Override
    public void saveToRedis(String propertiesName) {
        if (id < 1) {
            // throw new RuntimeException(String.format("角色ID %d 异常", this.id));
            return;
        }
        String key = HallKey.Role_Map_Info.getKey(id);
        Method method = WRITEMETHODS.get(propertiesName);
        if (method != null) {
            try {
                Object value = method.invoke(this);
                if (value != null) {
                    JedisManager jedisManager = SpringUtil.getBean(JedisManager.class);

                    // 使用redisson
                    jedisManager.getJedisCluster().hset(key, propertiesName, value.toString());
                    // RMap<String, Object> map = RedissonManager.getRedissonClient().getMap(key);
                    // map.put(propertiesName, value);
                } else {
                    LOGGER.warn("属性{}值为null", propertiesName);
                }

            } catch (Exception e) {
                LOGGER.error("属性存储", e);
            }
        } else {
            LOGGER.warn("属性：{}未找到对应方法", propertiesName);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        if (this.nick == null || !this.nick.equals(nick)) {
            saveToRedis("nick");
        }
        this.nick = nick;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public long getGem() {
        return gem;
    }

    public void setGem(long gem) {
        this.gem = gem;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }


    /**
     * 道具数量
     *
     * @return
     * @author JiangZhiYong
     * @QQ 359135103
     * 2017年10月24日 上午10:23:57
     */
    public long getItemCount() {
        String key = HallKey.Role_Map_Packet.getKey(id);
        JedisManager jedisManager = SpringUtil.getBean(JedisManager.class);

        return jedisManager.getJedisCluster().hlen(key);
    }


    /**
     * 获取道具
     *
     * @param itemId
     * @return
     * @author JiangZhiYong
     * @QQ 359135103
     * 2017年10月24日 上午10:21:18
     */
    public Item getItem(long itemId) {
        String key = HallKey.Role_Map_Packet.getKey(id);
        JedisManager jedisManager = SpringUtil.getBean(JedisManager.class);

        String hget = jedisManager.getJedisCluster().hget(key, String.valueOf(itemId));
        if (hget == null) {
            return null;
        }
        return JsonUtil.parseObject(hget, Item.class);
    }

    /**
     * 所有道具
     *
     * @return
     * @author JiangZhiYong
     * @QQ 359135103
     * 2017年10月24日 上午10:36:11
     */
    public Map<Long, Item> getItems() {
        JedisManager jedisManager = null;
        if (jedisManager == null) {
            jedisManager = SpringUtil.getBean(JedisManager.class);
        }
        String key = HallKey.Role_Map_Packet.getKey(id);
        return jedisManager.hgetAll(key, Long.class, Item.class);
    }


    /**
     * 角色存redis key
     *
     * @return
     * @author JiangZhiYong
     * @QQ 359135103 2017年9月26日 下午5:02:40
     */
    public String getRoleRedisKey() {
        return HallKey.Role_Map_Info.getKey(id);
    }

    /**
     * 存储整个role对象
     *
     * @author JiangZhiYong
     * @QQ 359135103
     * 2017年9月26日 下午5:06:51
     */
    public void saveToRedis() {
        JedisManager jedisManager = SpringUtil.getBean(JedisManager.class);

        jedisManager.getJedisCluster().hmset(getRoleRedisKey(), JsonUtil.object2Map(this));
    }
}
