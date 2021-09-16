package com.bluewind.base.module.scheduling;

import com.bluewind.base.common.util.DateTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @author liuxingyu01
 * @date 2021-09-16-9:47
 * @description 测试@Scheduled定时任务
 **/
@Component //使用spring管理
public class TestSchedulingTask {
    final static Logger logger = LoggerFactory.getLogger(TestSchedulingTask.class);


    /**
     * @Schedule参数解释
     * fixedRate 表示任务执行之间的时间间隔，具体是指两次任务的开始时间间隔，即第二次任务开始时，第一次任务可能还没结束。
     * fixedDelay 表示任务执行之间的时间间隔，具体是指本次任务结束到下次任务开始之间的时间间隔。
     * initialDelay 表示首次任务启动的延迟时间。
     * 所有时间的单位都是毫秒。
     */
    @Scheduled(initialDelay = 5000, fixedDelay = 5000)
    public void uploadIdTables() {
        if (logger.isInfoEnabled()) {
            logger.info("TestSchedulingTask 测试定时任务启动 " + DateTool.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
        }
    }
}
