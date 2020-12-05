package com.lcvc.guojiaoyuan.yuliaoku.test.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.Future;

/**
 * 用于异步请求测试
 * 并返回数据
 */
@Component
public class AsyncReturnTask {
    @Async
    public Future<String> doTaskOne() throws Exception {
        System.out.println("开始做任务一");
        long start = Calendar.getInstance().getTimeInMillis();
        Thread.sleep(new Random().nextInt(10000));
        long end = Calendar.getInstance().getTimeInMillis();
        return new AsyncResult<String>("完成任务一");
    }

    @Async
    public Future<String> doTaskTwo() throws Exception {
        System.out.println("开始做任务二");
        long start = Calendar.getInstance().getTimeInMillis();
        Thread.sleep(new Random().nextInt(10000));
        long end = Calendar.getInstance().getTimeInMillis();
        System.out.println("完成任务二，耗时：" + (end - start) + "毫秒");
        return new AsyncResult<>("完成任务二");
    }

    @Async
    public Future<String> doTaskThree() throws Exception {
        System.out.println("开始做任务三");
        long start = Calendar.getInstance().getTimeInMillis();
        Thread.sleep(new Random().nextInt(10000));
        long end = Calendar.getInstance().getTimeInMillis();
        System.out.println("完成任务三，耗时：" + (end - start) + "毫秒");
        return new AsyncResult<>("完成任务三");
    }
}
