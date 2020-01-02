package com.xl.game.gate.script;

import com.xl.game.engine.ServerType;
import org.apache.mina.filter.firewall.BlacklistFilter;

public interface IGateServerScript {



    /**
     * 是否为udp消息
     * @author JiangZhiYong
     * @QQ 359135103
     * 2017年9月1日 下午2:40:01
     * @param serverType 判断游戏类型是否支持udp
     * @param msgId 消息ID
     * @return
     */
    default boolean isUdpMsg(ServerType serverType, int msgId){
        return false;
    }

    /**
     * 设置IP黑名单
     * @author JiangZhiYong
     * @QQ 359135103
     * 2017年9月4日 上午11:23:21
     * @param filter
     */
    default void setIpBlackList(BlacklistFilter filter){

    }


}
