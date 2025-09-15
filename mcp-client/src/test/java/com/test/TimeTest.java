package com.test;

import org.dromara.App;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

/**
 * @author lee
 * @description
 */
@SpringBootTest(classes = App.class)
public class TimeTest {

    @Test
    public void testTime() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("任务1");
        Thread.sleep(1000);
        stopWatch.stop();

        stopWatch.start("任务2");
        Thread.sleep(300);
        stopWatch.stop();

        stopWatch.start("任务3");
        Thread.sleep(100);
        stopWatch.stop();

        // 打印任务的耗时统计
        System.out.println(stopWatch.prettyPrint());
//        System.out.println(stopWatch.shortSummary());

        // 任务总览s
        System.out.println("所有任务总耗时：" + stopWatch.getTotalTimeMillis());
        System.out.println("任务总数：" + stopWatch.getTaskCount());
    }

}
