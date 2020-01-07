package com.xl.game.model.struct;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xl.game.model.redis.key.HallKey;
import com.xl.game.redis.manager.JedisManager;
import com.xl.game.util.Config;
import com.xl.game.util.JsonUtil;
import com.xl.game.util.SpringUtil;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.redisson.api.RRemoteService;

import java.util.Date;

/**
 * 道具 <br>
 * redis实时存储
 * <p> 暂时各个服务器各种修改，可以考虑再通过redisson
 * {@link RRemoteService} 实现在大厅处理</p>
 * 
 * @author JiangZhiYong
 * @QQ 359135103 2017年9月18日 下午2:31:29
 */
@JSONType(serialzeFeatures = SerializerFeature.WriteClassName)
@Entity(value = "item", noClassnameStored = true)
@Data
public class Item {

	@JSONField
	private long id;
	/**配置Id*/
	@JSONField
	private int configId;
	/**数量*/
	@JSONField
	private int num;
	/**创建时间*/
	@JSONField
	private Date createTime;

	public Item() {
        id = Config.getId();
	}

	public void saveToRedis(long roleId) {
		String key = HallKey.Role_Map_Packet.getKey(roleId);
		 JedisManager jedisManager = SpringUtil.getBean(JedisManager.class);
		jedisManager.getJedisCluster().hset(key, String.valueOf(id), JsonUtil.toJSONString(this));
	}
}
