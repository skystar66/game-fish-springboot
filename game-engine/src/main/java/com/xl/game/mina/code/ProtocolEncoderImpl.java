package com.xl.game.mina.code;

import com.google.protobuf.Message;
import com.xl.game.message.IDMessage;
import com.xl.game.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import sun.misc.MessageUtils;

import java.util.function.Predicate;


/**
 * 消息编码
 *
 * @author xuliang
 * @date 2019-12-07
 * QQ:359135103
 */
@Slf4j
public class ProtocolEncoderImpl implements ProtocolEncoder {


    /**
     * 允许的最大堆积未发送消息条数
     */
    protected int maxScheduledWriteMessages = 256;
    /**
     * 当超过设置的最大堆积消息条数时的处理
     */
    protected Predicate<IoSession> overScheduledWriteBytesHandler;


    /**
     * 编码，格式：数据长度|数据部分
     *
     * @param session
     * @param obj
     * @param out
     * @throws Exception
     */
    @Override
    public void encode(IoSession session, Object obj, ProtocolEncoderOutput out) throws Exception {

        if (getOverScheduledWriteBytesHandler() != null && session.getScheduledWriteMessages() > getMaxScheduledWriteMessages() && getOverScheduledWriteBytesHandler().test(session)) {
            return;
        }
        IoBuffer buf = null;
        if (obj instanceof Message) {
            buf = MsgUtil.toIoBuffer((Message) obj);
        } else if (obj instanceof IDMessage) {
            buf = MsgUtil.toIobuffer((IDMessage) obj);
        } else if (obj instanceof IoBuffer) {//必须符合完整的编码格式
            buf = (IoBuffer) obj;
        } else if (obj instanceof byte[]) {//必须符合除去消息长度后的编码格式
            byte[] data = (byte[]) obj;
            buf = IoBuffer.allocate(data.length + 4);
            buf.putInt(data.length);
            buf.put(data);
        } else {
            log.info("未知的数据类型:{}",obj);
            return;
        }
        if (buf != null && session.isConnected()) {
            buf.rewind();
//            log.warn("发送的数据byte[]{}",IntUtil.BytesToStr(buf.array()));
            out.write(buf);
            out.flush();
        }
    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }


    public int getMaxScheduledWriteMessages() {
        return maxScheduledWriteMessages;
    }

    public void setMaxScheduledWriteMessages(int maxScheduledWriteMessages) {
        this.maxScheduledWriteMessages = maxScheduledWriteMessages;
    }

    public Predicate<IoSession> getOverScheduledWriteBytesHandler() {
        return overScheduledWriteBytesHandler;
    }

    public void setOverScheduledWriteBytesHandler(Predicate<IoSession> overScheduledWriteBytesHandler) {
        this.overScheduledWriteBytesHandler = overScheduledWriteBytesHandler;
    }
}
