package com.lin.quartz.job;

import com.lin.quartz.entity.QuartzJob;
import com.lin.quartz.job.AbstractQuartzJob;
import com.lin.quartz.utils.JobInvokeUtils;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（允许并发构建）
 */
public class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, QuartzJob job) throws Exception {
        JobInvokeUtils.invokeMethod(job);
    }
}
