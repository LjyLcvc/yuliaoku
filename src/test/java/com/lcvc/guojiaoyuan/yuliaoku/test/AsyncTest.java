package com.lcvc.guojiaoyuan.yuliaoku.test;

import com.lcvc.guojiaoyuan.yuliaoku.YuliaokuApplicationTests;
import com.lcvc.guojiaoyuan.yuliaoku.test.async.AsyncReturnTask;
import com.lcvc.guojiaoyuan.yuliaoku.test.async.AsyncTask;
import com.lcvc.guojiaoyuan.yuliaoku.test.async.SyncTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Future;

/**
 * 异步机制调用测试
 */
public class AsyncTest extends YuliaokuApplicationTests {
    @Autowired
    private SyncTask syncTask;
    @Autowired
    private AsyncTask asyncTask;
    @Autowired
    private AsyncReturnTask asyncReturnTask;


    //执行同步任务测试
    @Test
    public void testSyncTasks() throws Exception {
        syncTask.doTaskOne();
        syncTask.doTaskTwo();
        syncTask.doTaskThree();
    }

    //执行异步任务测试
    @Test
    public void testAsyncTasks() throws Exception {
        asyncTask.doTaskOne();
        asyncTask.doTaskTwo();
        asyncTask.doTaskThree();
    }

    //执行异步任务返回信息测试
    @Test
    public void testAsyncReturnTasks() throws Exception {
        Future<String> task1 = asyncReturnTask.doTaskOne();
        Future<String> task2=  asyncReturnTask.doTaskTwo();
        Future<String> task3= asyncReturnTask.doTaskThree();
        // 三个任务都调用完成，退出循环等待
       /* while (!task1.isDone() || !task2.isDone() || !task3.isDone()) {
            Thread.sleep(1000);
        }
        System.out.println("任务全部完成");*/
       /* while(true) {
            if(task1.isDone()&&task2.isDone() &&task3.isDone()){
                System.out.println("任务全部完成");
                break;
            }
        }*/
       String s=task1.get();
       System.out.println(s);
    }
}
