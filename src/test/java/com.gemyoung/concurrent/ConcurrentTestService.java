package com.gemyoung.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author weilong
 * @date 2017/12/15 下午3:34.
 */
@Slf4j
public class ConcurrentTestService{

    private ExecutorService executorService;
    private CyclicBarrier start = null;
    private CountDownLatch finish = null;

    public ConcurrentTestService(ExecutorService executorService, int concurrentCount){
        this.executorService = executorService;
        start = new CyclicBarrier(concurrentCount);
        finish = new CountDownLatch(concurrentCount);
    }
    public void submit(List<Runnable> tasks) throws InterruptedException {
        final AtomicInteger count = new AtomicInteger(0);
        log.info("开始提交任务");
        tasks.forEach(task ->executorService.submit(() -> {
            try {
                start.await();
                log.info("第{}个任务开始执行", count.incrementAndGet());
                task.run();
                finish.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        log.info("所有任务提交完成");
        finish.await();
        log.info("所有{}个任务执行完成",tasks.size());
    }

    public static void main(String[] args) throws Exception{
        List<Runnable> tasks = new ArrayList<>();
        ConcurrentTestService concurrentTestService = new ConcurrentTestService(Executors.newFixedThreadPool(10000), 10000);
        for(int i = 1;i<=10000;i++){
            tasks.add(new LogTask(i));
        }
        concurrentTestService.submit(tasks);
    }
}
