package com.xl.game.handler;

import com.google.protobuf.Message;
import com.xl.game.message.IDMessage;
import com.xl.game.struts.Person;


/**
 * Tcp 处理器
 * <br>也可能处理udp请求
 *
 * @author xuliang
 * @mail 2755055412@qq.com
 */
public class TcpHandler extends AbsHandler {

    private Message message;
    protected long rid; // 角色ID
    protected Person person; // 角色


    @Override
    public Object getMessage() {
        return null;
    }


    @Override
    public void setMessage(Object message) {
        this.message = (Message) message;
    }

    @Override
    public void run() {

    }


    /**
     * 获取消息
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends Message> T getMsg() {
        return (T) message;
    }

    @SuppressWarnings("unchecked")
    public <T extends Person> T getPerson() {
        return (T) person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }


    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }


    /**
     * 发送带ID的消息
     *
     * @param object
     */
    public void sendIdMsg(Object object) {
        if (getSession() != null && getSession().isConnected()) {
            getSession().write(new IDMessage(session, object, rid));
        } else if (getChannel() != null && getChannel().isActive()) {
            getChannel().writeAndFlush(new IDMessage(channel, object, rid, null));
        }
    }


}
