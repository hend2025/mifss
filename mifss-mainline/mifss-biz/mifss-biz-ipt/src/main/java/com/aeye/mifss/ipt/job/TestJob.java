package com.aeye.mifss.ipt.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;

/**
 * 测试Job
 */
@Component
@JobHandler("testJob")
public class TestJob extends IJobHandler {

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        System.out.println("xxl-job调用成功");
        XxlJobLogger.log("xxl-job调用成功");
        return ReturnT.SUCCESS;
    }
}
