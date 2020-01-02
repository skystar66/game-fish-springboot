package com.xl.game.thread.timer.events;


import com.xl.game.script.ITimerEventScript;
import com.xl.game.thread.timer.TimerEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

/**
 * 服务器主定时器
 *
 * @author xuliang
 * @date 2018-12-09 QQ:2755055412
 */
@Slf4j
public class ServerHeartTimer extends TimerEvent {


    protected int hour = -1;
    protected int min = -1;
    protected int sec = -1;
    protected int date = -1;


    ITimerEventScript itimerEventTimer;


    public ServerHeartTimer(ITimerEventScript itimerEventTimer) {

        this.itimerEventTimer = itimerEventTimer;

    }


    @Override
    public void run() {

        LocalTime localTime = LocalTime.now();
        int _sec = localTime.getSecond();
        if (sec != _sec) {
            sec = _sec;
            long start = System.currentTimeMillis();
            itimerEventTimer.secondHandler(localTime);
            long executeTime = System.currentTimeMillis() - start;
            if (executeTime > 50) {
                log.warn("秒级别定时 定时脚本[{}] 执行{}ms", itimerEventTimer.getClass().getName(), executeTime);
            }
        }
        int _min = localTime.getMinute();

        if (min != _min) {
            min = _min;
            long start = System.currentTimeMillis();
            itimerEventTimer.minuteHandler(localTime);
            long executeTime = System.currentTimeMillis() - start;
            if (executeTime > 50) {
                log.warn("分级别定时 定时脚本[{}] 执行{}ms", itimerEventTimer.getClass().getName(), executeTime);
            }
        }
        int _hour = localTime.getHour();
        if (hour != _hour) {
            hour = _hour;
            long start = System.currentTimeMillis();
            itimerEventTimer.hourHandler(localTime);
            long executeTime = System.currentTimeMillis() - start;
            if (executeTime > 50) {
                log.warn("小时级别定时 定时脚本[{}] 执行{}ms", itimerEventTimer.getClass().getName(), executeTime);
            }
        }

    }
}
