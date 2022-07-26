package com.lin.quartz;

import com.lin.quartz.utils.SpringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringQuartzApplicationTests {

    @Test
    void contextLoads() {
        Object mysqlJob = SpringUtils.getBean("mysqlJob");
        System.out.println(mysqlJob);
    }

}
