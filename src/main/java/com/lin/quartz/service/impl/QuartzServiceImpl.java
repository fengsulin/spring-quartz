package com.lin.quartz.service.impl;

import com.lin.quartz.core.ScheduleConstants;
import com.lin.quartz.entity.QuartzJob;
import com.lin.quartz.mapper.QuartzMapper;
import com.lin.quartz.service.QuartzService;
import com.lin.quartz.utils.CronUtils;
import com.lin.quartz.utils.ScheduleUtils;
import com.lin.quartz.utils.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class QuartzServiceImpl implements QuartzService {
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private QuartzMapper quartzMapper;

    /**
     * 项目启动初始化，初始化定时器，防止手动修改数据库导致未同步到定时任务处理
     * @throws Exception
     */
    @PostConstruct
    public void init() throws Exception{
        scheduler.clear();
        List<QuartzJob> quartzJobs = quartzMapper.selectJobAll();
        for(QuartzJob job : quartzJobs){
            ScheduleUtils.createScheduleJob(scheduler,job);
        }
    }
    @Override
    public int pauseJob(QuartzJob job) throws Exception {
        if(StringUtils.isNotNull(job)){
            job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
            int insert = quartzMapper.insert(job);
            if(insert > 0){
                ScheduleUtils.createScheduleJob(scheduler,job);
            }
        }
        return 0;
    }

    @Override
    public int resumeJob(QuartzJob job) throws SchedulerException {
        if(StringUtils.isNotNull(job)){
            Long jobId = job.getJobId();
            String jobGroup = job.getJobGroup();
            int changeStatus = quartzMapper.changeStatus(jobId, ScheduleConstants.Status.NORMAL.getValue());
            if(changeStatus > 0){
                scheduler.resumeJob(ScheduleUtils.getJobKey(jobId,jobGroup));
            }
            return changeStatus;
        }
        return 0;
    }

    @Override
    public int deleteJob(QuartzJob job) throws SchedulerException {
        if(StringUtils.isNotNull(job)){
            Long jobId = job.getJobId();
            String jobGroup = job.getJobGroup();
            int deleteJobById = quartzMapper.deleteJobById(jobId);
            if(deleteJobById > 0){
                scheduler.deleteJob(ScheduleUtils.getJobKey(jobId,jobGroup));
            }
            return deleteJobById;

        }
        return 0;
    }

    @Override
    public int changeStatus(Long jobId,String status) throws SchedulerException {
        int changeStatus = quartzMapper.changeStatus(jobId, status);
        if(changeStatus == 0){
            return changeStatus;
        }
        QuartzJob quartzJob = quartzMapper.selectJobById(jobId);
        String jobGroup = quartzJob.getJobGroup();
        if(ScheduleConstants.Status.NORMAL.getValue().equals(status)){
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId,jobGroup));
        }else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)){
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId,jobGroup));
        }
        return changeStatus;
    }

    @Override
    public void run(QuartzJob job) throws SchedulerException {
        if(StringUtils.isNotNull(job)){
            Long jobId = job.getJobId();
            String jobGroup = job.getJobGroup();
            QuartzJob quartzJob = quartzMapper.selectJobById(jobId);
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(ScheduleConstants.TASK_PROPERTIES,quartzJob);

            scheduler.triggerJob(ScheduleUtils.getJobKey(jobId,jobGroup),jobDataMap);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertJob(QuartzJob job) throws Exception {
        if(StringUtils.isNotNull(job)){
            // 插入前，先设置任务为暂停状态
            job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
            int insert = quartzMapper.insert(job);
            if(insert > 0){
                ScheduleUtils.createScheduleJob(scheduler,job);
            }
            return insert;
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateJob(QuartzJob job) throws Exception {
        Long jobId = job.getJobId();
        int updateById = quartzMapper.updateById(job);
        QuartzJob quartzJob = quartzMapper.selectJobById(jobId);
        if(updateById > 0){
            updateScheduleJob(quartzJob,quartzJob.getJobGroup());
            return updateById;
        }
        return 0;
    }

    @Override
    public void updateScheduleJob(QuartzJob job, String jobGroup) throws Exception {
        Long jobId = job.getJobId();
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        // 更新任务前，判断scheduler中是否存在该任务，若存在，则先删除掉，再重新创建
        if(scheduler.checkExists(jobKey)){
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler,job);
    }

    @Override
    public boolean checkCronExpressionIsValid(String cronExpression) throws Exception {
        boolean valid = CronUtils.isValid(cronExpression);
        return valid;
    }
}
