package com.xl.game.gate.script;


import com.xl.game.model.constants.Reason;
import org.apache.mina.core.session.IoSession;

/**
 * 用户接口
 * @author JiangZhiYong
 * @QQ 359135103
 * 2017年7月26日 下午4:42:39
 */
public interface IUserScript {


    /**
     * 用户退出处理
     * @author xuliang
     * @QQ 359135103
     * 2017年7月26日 下午4:47:34
     * @param session 游戏客户端会话
     * @param reason 原因
     */
    default void quit(IoSession session, Reason reason){

    }



}
