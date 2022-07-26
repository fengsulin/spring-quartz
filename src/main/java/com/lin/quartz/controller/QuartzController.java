package com.lin.quartz.controller;

import com.lin.quartz.core.Constants;
import com.lin.quartz.entity.QuartzJob;
import com.lin.quartz.service.QuartzService;
import com.lin.quartz.utils.CronUtils;
import com.lin.quartz.utils.StringUtils;
import com.lin.quartz.vo.ResultVo;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuartzController {
    private final QuartzService quartzService;

    public QuartzController(QuartzService quartzService){
        this.quartzService = quartzService;
    }

    @PostMapping( "/add")
    public ResultVo<QuartzJob> add(@RequestBody QuartzJob job) throws Exception {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return ResultVo.failed("失败，Cron表达式不正确");
        } else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            return ResultVo.failed("失败，目标字符串不允许'rmi://'调用");
        } else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_LDAP)) {
            return ResultVo.failed("失败，目标字符串不允许'ldap://'调用");
        }
        quartzService.insertJob(job);
        return ResultVo.success();
    }

    @PostMapping("/changeStatus")
    public ResultVo<Object> changeStatus(Long jobId, String status) throws SchedulerException {
        quartzService.changeStatus(jobId, status);
        return ResultVo.success();
    }




    @PostMapping("/update")
    public ResultVo<QuartzJob> update(@RequestBody QuartzJob job) throws Exception {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return ResultVo.failed("失败，Cron表达式不正确");
        } else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            return ResultVo.failed("失败，目标字符串不允许'rmi://'调用");
        } else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_LDAP)) {
            return ResultVo.failed("失败，目标字符串不允许'ldap://'调用");
        }
        int updateJob = quartzService.updateJob(job);
        if(updateJob > 0){
            return ResultVo.success();
        }
        return ResultVo.failed();
    }

    @DeleteMapping("/delete")
    public ResultVo<QuartzJob> delete(@RequestBody QuartzJob job) throws SchedulerException {
        int deleteJob = quartzService.deleteJob(job);
        if(deleteJob >0){
            return ResultVo.success();
        }
        return ResultVo.failed();
    }

}
