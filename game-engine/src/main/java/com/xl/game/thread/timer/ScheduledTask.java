package com.xl.game.thread.timer;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 独立周期性单线程
 *
 * @author xuliang
 * @QQ 359135103 2017年7月3日 上午10:43:19
 */
@Slf4j
public abstract class ScheduledTask {

    private final ScheduledExecutorService scheduler;
    private final int period; // 周期


    /**
     * @param taskMaxTime 执行周期
     */

    public ScheduledTask(int taskMaxTime) {


        scheduler = Executors.newScheduledThreadPool(1);

        period = taskMaxTime < 100 ? 100 : taskMaxTime;


    }


    /**
     * 任务逻辑
     */
    protected abstract void executeTask();


    /**
     * 启动线程
     */
    public void start() {
        log.info("启动检查网关服，集群服注册任务");
        scheduler.scheduleAtFixedRate(new Task(), 100, period, TimeUnit.MILLISECONDS);
    }


    /**
     * 关闭调用
     */
    public void shutdown() {
        scheduler.shutdown();
    }


    /**
     * 任务
     *
     * @author xuliang
     * @QQ 359135103 2017年7月3日 上午10:45:01
     */

    class Task implements Runnable {

        @Override
        public void run() {
            try {

                long begin = System.currentTimeMillis();
                executeTask();
                if (System.currentTimeMillis() - begin > period) {
                    log.warn("定时器{}执行{}ms", getClass().getSimpleName(), System.currentTimeMillis() - begin);
                }
            } catch (Exception ex) {
                log.info("定时任务执行失败!!!，msg:{}", ex);
            }
        }
    }
}
