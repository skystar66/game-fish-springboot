package com.xl.game.thread.timer;

import com.xl.game.thread.ServerThread;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 定时任务线程
 */
@Slf4j
public class TimerThread extends Timer {

//    public volatile boolean stop;


    //定时器依附的线程
    private final ServerThread serverThread;
    // 定时器任务
    private final Collection<TimerEvent> events = Collections.synchronizedCollection(new ArrayList<TimerEvent>());
    //定时器中使用定时器类型
    private final HashSet<String> classLogNames = new HashSet<>();
    //使用JDK 的 timetask
    private TimerTask task;

    public TimerThread(ServerThread serverThread, Class<? extends TimerEvent>... classLogNames) {
        //定义定时任务线程名称
        super(serverThread.getThreadName() + "-Timer");
        this.serverThread = serverThread;
        //添加定时器类型
        for (Class<? extends TimerEvent> cls : classLogNames) {
            this.classLogNames.add(cls.getSimpleName());
        }
        log.info("TimerThread:TimerThread = " + serverThread.getThreadName());
    }


    public void start() {
        log.info("TimerThread:start=" + serverThread.getThreadName() + "=heart=" + serverThread.getHeart());
        task = new TimerTask() {
            @Override
            public void run() {
                synchronized (events) {
                    Iterator<TimerEvent> timeEvent = events.iterator();
                    while (timeEvent.hasNext()) {
                        TimerEvent timerEvent = timeEvent.next();
                        if (timerEvent.remain() >= 0) {
                            if (timerEvent.getLoop() > 0) {
                                timerEvent.setLoop(timerEvent.getLoop() - 1);
                                serverThread.execute(timerEvent);
                            } else if (timerEvent.getLoop() < 0) {
                                //一直循环检测
                                serverThread.execute(timerEvent);
                            }
                        } else {
                            //截止时间已到，销毁该任务
                            timerEvent.setLoop(0);
                            serverThread.execute(timerEvent);
                        }
                        if (timerEvent.getLoop() == 0) {
                            //销毁该任务
                            timeEvent.remove();
                        }
                    }
                }
            }
        };
        //开启定时轮训
        schedule(task, 0l, serverThread.getHeart());
        log.error("TimerThread:end=" + serverThread.getThreadName() + "=heart=" + serverThread.getHeart());
    }

    public void stop(boolean flag) {
        synchronized (events) {
            //清空线程命令队列
            events.clear();
            if (task != null) {
                task.cancel();
            }
            cancel();
        }
        int sign = flag ? 1 : 0;
        log.error("TimerThread:stop=" + serverThread.getThreadName() + "=flag=" + sign);
    }


    public void addTimerEvents(TimerEvent timerEvent) {

        log.info("添加定时任务timerEvent");

        synchronized (events) {
            events.add(timerEvent);
        }

    }

    public void removeTimerEvents(TimerEvent timerEvent) {
        synchronized (events) {
            events.remove(timerEvent);
        }
    }


}
