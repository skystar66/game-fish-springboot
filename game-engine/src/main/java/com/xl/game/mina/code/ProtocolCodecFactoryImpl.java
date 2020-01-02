package com.xl.game.mina.code;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


/**
 * 消息解析工厂
 *
 * @author xuliang
 * @date 2019-12-07
 * QQ:359135103
 */
public class ProtocolCodecFactoryImpl implements ProtocolCodecFactory {

    private ProtocolDecoderImpl decoder;
    private ProtocolEncoderImpl encoder;

    public ProtocolCodecFactoryImpl(ProtocolDecoderImpl decoder, ProtocolEncoderImpl encoder) {
        this.decoder = decoder;
        this.encoder = encoder;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return getEncoder();
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return getDecoder();
    }


    public ProtocolDecoderImpl getDecoder() {
        return decoder;
    }

    public void setDecoder(ProtocolDecoderImpl decoder) {
        this.decoder = decoder;
    }

    public ProtocolEncoderImpl getEncoder() {
        return encoder;
    }

    public void setEncoder(ProtocolEncoderImpl encoder) {
        this.encoder = encoder;
    }
}
