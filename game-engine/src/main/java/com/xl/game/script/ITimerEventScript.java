package com.xl.game.script;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 心跳脚本，最低按秒循环
 *
 * @author xulaing
 * @date 2019-12-20
 * QQ:359135103
 */
public interface ITimerEventScript extends IScript {

	/**
	 * 每秒执行
	 * @param localTime
	 */
    default void secondHandler(LocalTime localTime) {

    }

    /**
     * 每分钟执行
     * @param localTime
     */
    default void minuteHandler(LocalTime localTime) {

    }

    /**
     * 每小时执行
     * @param localTime
     */

    default void hourHandler(LocalTime localTime) {

    }

    /**
     * 每天执行
     * @param localDateTime
     */
    default void dayHandler(LocalDateTime localDateTime) {

    }
}
