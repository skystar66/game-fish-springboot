package com.xl.game.model.script;


import com.xl.game.message.ServerMessage;

/**
 * 游戏服务器状态监测脚本
 *
 * @author xuliang
 * @QQ 359135103
 * 2017年7月10日 下午4:29:45
 */
public interface IGameServerCheckScript {
    /**
     * 构建服务器状态信息
     *
     * @param builder
     */
    default void buildServerInfo(ServerMessage.ServerInfo.Builder builder) {
    }


}
