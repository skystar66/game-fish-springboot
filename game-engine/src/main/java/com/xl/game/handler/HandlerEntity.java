package com.xl.game.handler;


import com.google.protobuf.Message;
import com.xl.game.thread.ThreadType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 消息处理注解
 *
 * @author xulaing
 * @date 2017-03-30 QQ:359135103
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface HandlerEntity {



    /**
     * TCP 消息id
     */
    int mid() default 0;

    /***
     * http请求路径
     *
     * @return
     */
    String path() default "";

    /**
     *
     * @return 描述
     */
    String desc() default "";

    /**
     * 调用的线程
     *
     * @return
     */
    ThreadType thread() default ThreadType.IO;

    /**
     * tcp 请求的消息类
     *
     * @return
     */
    Class<? extends Message> msg() default Message.class;




}
