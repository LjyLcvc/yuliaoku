package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.test.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Random;

/**
 * 用于异步请求测试
 */
@Component
public class AsyncTask {
    @Async
    public void doTaskOne() throws Exception {
        System.out.println("开始做任务一");
        long start = Calendar.getInstance().getTimeInMillis();
        Thread.sleep(new Random().nextInt(10000));
        long end = Calendar.getInstance().getTimeInMillis();
        System.out.println("完成任务一，耗时：" + (end - start) + "毫秒");
    }

    @Async
    public void doTaskTwo() throws Exception {
        System.out.println("开始做任务二");
        long start = Calendar.getInstance().getTimeInMillis();
        Thread.sleep(new Random().nextInt(10000));
        long end = Calendar.getInstance().getTimeInMillis();
        System.out.println("完成任务二，耗时：" + (end - start) + "毫秒");
    }

    @Async
    public void doTaskThree() throws Exception {
        System.out.println("开始做任务三");
        long start = Calendar.getInstance().getTimeInMillis();
        Thread.sleep(new Random().nextInt(10000));
        long end = Calendar.getInstance().getTimeInMillis();
        System.out.println("完成任务三，耗时：" + (end - start) + "毫秒");
    }
}
