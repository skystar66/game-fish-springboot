package com.xl.game.thread;


import java.util.concurrent.Executor;

/**
 * 自定义线程池
 *
 * @author xuliang
 * @date 2019-12-07
 * QQ:2755055412
 */
public interface ExecutorPool extends Executor {

    /**
     * 关闭线程
     */
    void stop();


}
