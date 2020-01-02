package com.xl.game.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * IO线程池工厂
 *
 * @author xuliang
 * @date 2019-12-06 QQ:2755055412
 */
public class IoThreadFactory implements ThreadFactory {

    private static AtomicInteger atomicInteger = new AtomicInteger(1);

    private final ThreadGroup group;

    public IoThreadFactory() {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(group, r, ThreadType.IO + "-" + atomicInteger.getAndIncrement(), 0);
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
