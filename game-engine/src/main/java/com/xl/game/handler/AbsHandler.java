package com.xl.game.handler;

import io.netty.channel.Channel;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;

/**
 * 抽象handler
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public abstract class AbsHandler implements IHandler {


    protected IoSession session;
    protected long createTime;
    protected Channel channel;



    @Override
    public IoSession getSession() {
        return session;
    }

    @Override
    public void setSession(IoSession session) {
        this.session = session;
    }


    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void setCreateTime(long time) {
        createTime =time;
    }

    @Override
    public long getCreateTime() {
        return createTime;
    }

    @Override
    public Object getParameter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setParameter(Object parameter) {
        // TODO Auto-generated method stub

    }




}
