package com.lin.quartz.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component("mysqlJob")
public class MysqlJob {
    protected final Logger logger = LoggerFactory.getLogger(MysqlJob.class);

    public void execute(String param){
        logger.info("执行Mysql Job，当前时间：{}，任务参数：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),param);
    }
}
