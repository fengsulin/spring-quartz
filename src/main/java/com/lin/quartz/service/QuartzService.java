package com.lin.quartz.service;

import com.lin.quartz.entity.QuartzJob;
import org.quartz.SchedulerException;

public interface QuartzService {

    /**
     * 暂停任务
     * @param job
     * @return
     * @throws SchedulerException
     */
    public int pauseJob(QuartzJob job) throws Exception;

    /**
     * 回复任务
     * @param job
     * @return
     * @throws SchedulerException
     */
    public int resumeJob(QuartzJob job) throws SchedulerException;

    /**
     * 删除任务后，所对应的trigger也将被删除
     * @param job
     * @return
     * @throws SchedulerException
     */
    public int deleteJob(QuartzJob job) throws SchedulerException;

    /**
     * 根据调度任务id和状态修改
     * @param jobId
     * @param status
     * @return
     * @throws SchedulerException
     */
    public int changeStatus(Long jobId,String status) throws SchedulerException;

    /**
     * 立即运行认为
     * @param job
     * @throws SchedulerException
     */
    public void run(QuartzJob job) throws SchedulerException;

    /**
     * 新增任务
     * @param job
     * @return
     * @throws Exception
     */
    public int insertJob(QuartzJob job) throws Exception;

    /**
     * 更新任务
     * @param job
     * @return
     * @throws Exception
     */
    public int updateJob(QuartzJob job) throws Exception;

    /**
     * 更新任务
     * @param job
     * @param jobGroup
     * @return
     * @throws Exception
     */
    public void updateScheduleJob(QuartzJob job,String jobGroup) throws Exception;

    /**
     * 校验cron表达式是否有效
     * @param cronExpression
     * @return
     * @throws Exception
     */
    public boolean checkCronExpressionIsValid(String cronExpression) throws Exception;
}
