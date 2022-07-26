package com.lin.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.lin.quartz.core.ScheduleConstants;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 定时任务调度类
 */
public class QuartzJob implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键id，任务序列*/
    @TableId
    @NotNull(message = "任务id不能为空")
    private Long jobId;

    /**调度任务名称*/
    private String jobName;

    /**调度任务所属任务组*/
    private String jobGroup;
    /**调用目标字符串*/
    private String invokeTarget;
    /**cron表达式*/
    private String cronExpression;
    /**错误策略，0-默认，1-立即执行，2-执行一次，3-不触发执行*/
    private String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;
    /**并发执行，0-允许，1-禁止*/
    private String concurrent;
    /**调度任务状态，0-正常，1-暂停*/
    private String status;
    /**备注*/
    private String remark;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getInvokeTarget() {
        return invokeTarget;
    }

    public void setInvokeTarget(String invokeTarget) {
        this.invokeTarget = invokeTarget;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getMisfirePolicy() {
        return misfirePolicy;
    }

    public void setMisfirePolicy(String misfirePolicy) {
        this.misfirePolicy = misfirePolicy;
    }

    public String getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(String concurrent) {
        this.concurrent = concurrent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "QuartzJob{" +
                "jobId=" + jobId +
                ", jobName='" + jobName + '\'' +
                ", jobGroup='" + jobGroup + '\'' +
                ", invokeTarget='" + invokeTarget + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                ", misfirePolicy='" + misfirePolicy + '\'' +
                ", concurrent='" + concurrent + '\'' +
                ", status='" + status + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
