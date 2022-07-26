package com.lin.quartz.job;

import com.lin.quartz.core.ScheduleConstants;
import com.lin.quartz.entity.QuartzJob;
import com.lin.quartz.utils.BeanUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public abstract class AbstractQuartzJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        QuartzJob job = new QuartzJob();
        BeanUtils.copyBeanProp(job,context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        try{
            before(context,job);
            if(job != null){
                doExecute(context,job);
            }
            after(context,job,null);
        }catch (Exception e){
            log.error("任务异常 - ：",e);
            after(context,job,e);
        }
    }

    /**
     * 执行前
     * @param context
     * @param job
     */
    protected void before(JobExecutionContext context,QuartzJob job){
        threadLocal.set(new Date());
    }

    /**
     * 执行后
     * @param context
     * @param job
     * @param e
     */
    protected void after(JobExecutionContext context,QuartzJob job,Exception e){}

    /**
     * 执行方法，有子类重载
     * @param context
     * @param job
     * @throws Exception
     */
    protected abstract void doExecute(JobExecutionContext context,QuartzJob job) throws Exception;

}
