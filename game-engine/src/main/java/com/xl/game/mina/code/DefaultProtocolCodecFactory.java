package com.xl.game.mina.code;


/**
 * 默认消息解析工厂
 *
 * @author xuliang
 * @date 2019-12-07
 * QQ:359135103
 */
public class DefaultProtocolCodecFactory extends ProtocolCodecFactoryImpl {


    public DefaultProtocolCodecFactory() {
        super(new ProtocolDecoderImpl(), new ProtocolEncoderImpl());
    }
}
