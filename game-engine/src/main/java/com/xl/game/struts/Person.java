package com.xl.game.struts;


import com.alibaba.fastjson.annotation.JSONField;
import com.xl.game.cache.cooldown.Cooldown;
import com.xl.game.message.IDMessage;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 人物
 * <br>
 * TODO 组合替换继承
 *
 * @author JiangZhiYong
 * @QQ 359135103 2017年7月26日 下午1:40:11
 */
@Slf4j
@Data
public abstract class Person {


    @JSONField
    @Id
    protected long id;

    /**
     * 昵称
     */
    @JSONField
    @Indexed
    protected String nick;

    /**
     * 用户ID
     */
    @JSONField
    @Indexed
    protected long userId;

    /**
     * 金币
     */
    @JSONField
    protected long gold;

    /**
     * 钻石
     */
    @JSONField
    protected long gem;

    /**
     * 所在大厅ID
     */
    @JSONField
    protected int hallId;

    /**
     * 所在游戏服ID
     */
    @JSONField
    protected int gameId;

    /**
     * 头像
     */
    @JSONField
    protected String head;

    /**
     * 创建时间
     */
    @JSONField
    protected Date createTime;

    /**
     * 登录时间
     */
    @JSONField
    protected Date loginTime;

    /**
     * 等级
     */
    @JSONField
    protected int level;

    /**
     * 冷却缓存
     */
    protected transient Map<String, Cooldown> cooldowns = new HashMap<>();

    /**
     * 连接会话
     */
    protected transient IoSession ioSession;

    protected transient Channel channel;


    public void saveToRedis(String propertiesName) {

    }

    /**
     * 发送消息，带ID头
     *
     * @param message
     * @author JiangZhiYong
     * @QQ 359135103 2017年8月3日 下午2:53:28
     */
    public boolean sendMsg(Object message) {
        if (getIoSession() != null) {
            IDMessage idm = new IDMessage(getIoSession(), message, getId());
            getIoSession().write(idm);
            return true;
        } else if (getChannel() != null) {
            getChannel().writeAndFlush(new IDMessage(channel, message, getId(), null));
        } else {
            log.warn("连接session==null | channel==null {}", message);
        }
        return false;
    }


}
