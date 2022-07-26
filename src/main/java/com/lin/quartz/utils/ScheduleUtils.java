package com.lin.quartz.utils;

import com.lin.quartz.core.ScheduleConstants;
import com.lin.quartz.entity.QuartzJob;
import com.lin.quartz.job.QuartzDisallowConcurrentExecution;
import com.lin.quartz.job.QuartzJobExecution;
import org.quartz.*;

public class ScheduleUtils {

    /**
     * 获取quartz任务类型，是并发执行，还是非并发执行
     * @param job
     * @return
     */
    private static Class<? extends Job> getQuartzJobClass(QuartzJob job){
        boolean isConcurrent = "0".equals(job.getConcurrent());
        return isConcurrent ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
    }

    /**
     * 构建触发键对象
     * @param jobId
     * @param jobGroup
     * @return
     */
    public static TriggerKey getTriggerKey(Long jobId,String jobGroup){
        return TriggerKey.triggerKey(ScheduleConstants.TASK_CLASS_NAME + jobId,jobGroup);
    }

    /**
     * 构建任务键对象
     * @param jobId
     * @param jobGroup
     * @return
     */
    public static JobKey getJobKey(Long jobId,String jobGroup){
        return JobKey.jobKey(ScheduleConstants.TASK_CLASS_NAME + jobId,jobGroup);
    }

    /**
     * 创建调度任务
     * @param scheduler：任务调度器
     * @param job：job对象
     * @throws Exception
     */
    public static void createScheduleJob(Scheduler scheduler,QuartzJob job) throws Exception {
        Class<? extends Job> jobClass = getQuartzJobClass(job);
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        // 构建job信息
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(jobId, jobGroup)).build();

        // 构建cron表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(job,cronScheduleBuilder);

        // 根据调度构建器创建触发器
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobId, jobGroup))
                .withSchedule(cronScheduleBuilder)
                .build();

        jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES,job);

        // 判断是否已存在
        if(scheduler.checkExists(getJobKey(jobId,jobGroup))){
            scheduler.deleteJob(getJobKey(jobId,jobGroup));
        }
        scheduler.scheduleJob(jobDetail,cronTrigger);

        // 暂停任务
        if(job.getStatus().equals(ScheduleConstants.Status.PAUSE.getValue())){
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId,jobGroup));
        }

    }

    /**
     * 构建调度策略
     * @param job
     * @param cb
     * @return
     * @throws Exception
     */
    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(QuartzJob job,CronScheduleBuilder cb) throws Exception {
        switch(job.getMisfirePolicy()){
            case ScheduleConstants.MISFIRE_DEFAULT:
                return cb;
            case ScheduleConstants.MISFIRE_IGNORE_MISFIRES:
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            case ScheduleConstants.MISFIRE_FIRE_AND_PROCEED:
                return cb.withMisfireHandlingInstructionFireAndProceed();
            case ScheduleConstants.MISFIRE_DO_NOTHING:
                return cb.withMisfireHandlingInstructionDoNothing();
            default:
                throw new Exception("The task misfire policy'" + job.getMisfirePolicy() +"' cannot be used in cron schedule tasks");
        }
    }
}
