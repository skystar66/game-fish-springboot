package com.xl.game.handler;


import org.apache.mina.core.session.IoSession;

/**
 * 处理器接口
 */
public interface IHandler extends Runnable {


    /**
     * 设置会话
     *
     * @return
     */

    void setSession(IoSession session);


    /**
     * 获取会话
     *
     * @return
     */
    IoSession getSession();


    /**
     * 请求消息
     *
     * @return
     */
    Object getMessage();

    /**
     * 设置消息
     *
     * @return
     */
    void setMessage(Object message);

    /**
     * 创建时间
     *
     * @param time
     */
    void setCreateTime(long time);

    /**
     * 创建时间
     */
    long getCreateTime();

    /**
     * http 参数
     *
     * @return
     */
    Object getParameter();

    /**
     * http 参数
     *
     * @return
     */
    void setParameter(Object parameter);


}
