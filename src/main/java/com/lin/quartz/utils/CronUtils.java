package com.lin.quartz.utils;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

/**
 * cron表达式工具类
 */
public class CronUtils {

    /**
     * 给定的cron表达式是否能被识别
     * @param cronExpression
     * @return boolean：表达式是否有效
     */
    public static boolean isValid(String cronExpression){
        return CronExpression.isValidExpression(cronExpression);
    }

    /**
     * 当cron表达式合法时，返回null，否则返回cron表达式错误描述
     * @param cronExpression
     * @return
     */
    public static String getInvalidMessage(String cronExpression){
        try {
            new CronExpression(cronExpression);
            return null;
        } catch (ParseException e) {
            return e.getMessage();
        }
    }

    /**
     * 根据给定的cron表达式获取下一次执行时间
     * @param cronExpression
     * @return
     */
    public static Date getNextExecution(String cronExpression){
        try {
            CronExpression cron= new CronExpression(cronExpression);
            return cron.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
