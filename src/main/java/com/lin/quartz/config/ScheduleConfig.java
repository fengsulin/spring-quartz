package com.lin.quartz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class ScheduleConfig {

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource){
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);

        // quartz参数
        Properties prop = new Properties();
        prop.put("org.quartz.scheduler.instanceName","linScheduler");
        prop.put("org.quartz.scheduler.instanceId","AUTO");

        // 线程池配置
        prop.put("org.quartz.threadPool.class","org.quartz.simpl.SimpleThreadPool");
        prop.put("org.quartz.threadPool.threadCount","20");
        prop.put("org.quartz.threadPool.threadPriority","5");

        // JobStore配置
        prop.put("org.quartz.jobStore.class","org.quartz.impl.jdbcjobstore.JobStoreTX");

        // 集群配置
        prop.put("org.quartz.jobStore.isClustered","true");
        prop.put("org.quartz.jobStore.clusterCheckinInterval","15000");
        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtTime","1");
        prop.put("org.quartz.jobStore.txIsolationLevelSerializable","true");

        // sqlserver 启用
        // prop.put("org.quartz.jobStore.selectWithLockSQL", "SELECT * FROM {0}LOCKS UPDLOCK WHERE LOCK_NAME = ?")
        prop.put("org.quartz.jobStore.misfireThreshold","1200");
        prop.put("org.quartz.jobStore.tablePrefix","QRTZ_");
        factory.setSchedulerName("linScheduler");

        // 延时启动
        factory.setStartupDelay(1);
        factory.setApplicationContextSchedulerContextKey("applicationContextKey");

        // 可选，QuartzScheduler
        // 启动时更新已经存在的job，这样就不用每次修改targetObject后删除qrtz_job_dettail表对应的记录了
        factory.setOverwriteExistingJobs(true);
        // 设置自动启动，默认为true
        factory.setAutoStartup(true);
        return factory;

    }
}
