package com.xl.game.thread;

import com.xl.game.config.MailConfig;
import com.xl.game.mail.MailManager;
import com.xl.game.thread.timer.TimerEvent;
import com.xl.game.thread.timer.TimerThread;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 服务器线程
 * 两类线程模型
 * 1.为逻辑或接受到的消息预先分配一个线程，所有逻辑放在线程队列中依次执行
 * 2.为逻辑或消息分配一个队列，再由队列分配线程，依次执行，
 *
 * @author xuliang
 * @date 2019-12-06 QQ:2755055412
 */
@Slf4j
public class ServerThread extends Thread implements Executor {

    //线程名称
    private final String threadName;
    //线程心跳间隔
    private final long heart;
    //线程处理命令队列
    private LinkedBlockingQueue<Runnable> command_queue = new LinkedBlockingQueue<>();
    //是否暂停
    private volatile boolean stop;
    //最后一次执行命令的时间
    private long lastExcuteTime;

    //mail配置
    private MailConfig mailConfig;

    protected TimerThread timer;


    /**
     * 如果没有定时任务，心跳设为0 ，大于零会启动一个定时检查线程（比较浪费）
     *
     * @param group
     * @param threadName
     * @param heart         心跳
     * @param commandCount
     * @param classLogNames
     */
    @SafeVarargs
    public ServerThread(ThreadGroup group, String threadName, long heart, int commandCount,
                        MailConfig mailConfig,
                        Class<? extends TimerEvent>... classLogNames) {
        super(group, threadName);
        this.threadName = threadName;
        this.heart = heart;

        if (this.heart > 0) {
            timer = new TimerThread(this, classLogNames);
        }

        /**
         * 当一个线程因未捕获的异常而即将终止时虚拟机将使用 Thread.getUncaughtExceptionHandler()
         * 获取已经设置的 UncaughtExceptionHandler 实例，并通过调用其 uncaughtException(...) 方
         * 法而传递相关异常信息。
         * 如果一个线程没有明确设置其 UncaughtExceptionHandler，则将其 ThreadGroup 对象作为其
         * handler，如果 ThreadGroup 对象对异常没有什么特殊的要求，则 ThreadGroup 会将调用转发给
         * 默认的未捕获异常处理器（即 Thread 类中定义的静态未捕获异常处理器对象）。
         *
         * @see #setDefaultUncaughtExceptionHandler
         * @see #setUncaughtExceptionHandler
         * @see ThreadGroup#uncaughtException
         */
        setDefaultUncaughtExceptionHandler((Thread t, Throwable th) -> {
            log.error("ServerThread.setUncaughtExceptionHandler", th);
//            mailConfig
            MailManager.getInstance(mailConfig).sendTextMailAsync("线程异常", "线程" + threadName,
                    mailConfig.getReciveUser().toArray(new String[mailConfig.getReciveUser().size()]));
            if (timer != null) {
                timer.stop(true);
            }
            command_queue.clear();
        });
        command_queue = new LinkedBlockingQueue<>(commandCount);
    }


    /**
     * 当前执行的线程
     */

    protected Runnable command;


    public Runnable getCommand() {
        return command;
    }

    public void setCommand(Runnable command) {
        this.command = command;
    }


    @Override
    public void execute(Runnable command) {
        if (command_queue.contains(command)) {
            return;
        }
        command_queue.add(command);
        synchronized (this) {
            notify();
        }
    }


    @Override
    public void run() {
        //判断定时任务是否开启
        if (heart > 0) {
            timer.start();
        }
        stop = false;
        int loop = 0;
        while (!stop && !interrupted()) {
            //按顺序拿出排队等待执行的线程
            command = command_queue.poll();
            if (null == command) {
                try {
                    synchronized (this) {
                        loop = 0;
                        lastExcuteTime = 0;
                        wait();
                    }
                } catch (InterruptedException e) {
                    log.error("ServerThread.run 1 ", e);
                }
            } else {
                try {
                    synchronized (this) {
                        loop++;
                        lastExcuteTime = System.currentTimeMillis();
                        command.run();
                        long cost = System.currentTimeMillis() - lastExcuteTime;
                        if (cost > 30L) {
                            log.warn("线程：{} 执行 {} 消耗时间过长 {}毫秒,当前命令数 {} 条", threadName, command.getClass().getName(), cost,
                                    command_queue.size());
                        }
                        if (loop > 300) {
                            loop = 0;
                            log.warn("线程：{} 已循环执行{} 次,当前命令数{}", threadName, loop, command_queue.size());
                        }
                    }
                } catch (Exception e) {
                    log.error("ServerThread[" + threadName + "]执行任务错误 ", e);
                }
            }
        }
    }


    public void stop(boolean flag) {
        stop = flag;
        log.warn("线程{}停止", threadName);
        if (timer != null) {
            timer.stop(flag);
        }
        //清空线程命令
        command_queue.clear();
        try {
            synchronized (this) {
                notify();
                interrupt();
            }
        } catch (Exception e) {
            log.error("Main Thread " + threadName + " Notify Exception:" + e.getMessage());
        }
    }

    public boolean contains(Runnable runnable) {
        return command_queue.contains(runnable);
    }


    public void addTimerEvent(TimerEvent event) {
        if (timer != null) {
            timer.addTimerEvents(event);
        }
    }


    public String getThreadName() {
        return threadName;
    }


    public long getHeart() {
        return heart;
    }
}
