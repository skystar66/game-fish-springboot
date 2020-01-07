package com.xl.game.mq;

/**
 * MQ 消息处理脚本
 * @author xuliang
 * @QQ 359135103
 * 2017年7月28日 上午10:39:14
 */
public interface IMQScript{

    /**
     * ＭＱ消息接收处理
     * @author xuliang
     * @QQ 359135103
     * 2017年7月28日 上午10:39:59
     * @param msg
     */
    default void onMessage(String msg){

    }
}