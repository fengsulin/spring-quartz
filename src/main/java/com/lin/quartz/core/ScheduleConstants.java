package com.lin.quartz.core;

/**
 * 任务调度通用常量
 *
 * @author ruoyi
 */
public class ScheduleConstants {
    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

    /**
     * 执行目标key
     */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    /**
     * 默认,对应：
     * withMisfireHandlingInstructionFireAndProceed（默认）
     * ——以当前时间为触发频率立刻触发一次执行
     * ——然后按照Cron频率依次执行
     */
    public static final String MISFIRE_DEFAULT = "0";

    /**
     * withMisfireHandlingInstructionIgnoreMisfires
     * ——以错过的第一个频率时间立刻开始执行
     * ——重做错过的所有频率周期后
     * ——当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行
     */
    public static final String MISFIRE_IGNORE_MISFIRES = "1";

    /**
     * 和0一样
     * withMisfireHandlingInstructionFireAndProceed（默认）
     * ——以当前时间为触发频率立刻触发一次执行
     * ——然后按照Cron频率依次执行
     */
    public static final String MISFIRE_FIRE_AND_PROCEED = "2";

    /**
     * withMisfireHandlingInstructionDoNothing
     * ——不触发立即执行
     * ——等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
     */
    public static final String MISFIRE_DO_NOTHING = "3";

    public enum Status {
        /**
         * 正常
         */
        NORMAL("0"),
        /**
         * 暂停
         */
        PAUSE("1");

        private String value;

        private Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
