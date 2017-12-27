package com.gemyoung.concurrent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author weilong
 * @date 2017/12/15 下午4:50.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestConcurrentTask {
    @Test
    public void testSubmit() throws Exception{
        List<Runnable> tasks = new ArrayList<>();
        ConcurrentTestService concurrentTestService = new ConcurrentTestService(Executors.newFixedThreadPool(2000), 2000);
        for(int i = 1;i<=2000;i++){
            tasks.add(new LogTask(i));
        }
        concurrentTestService.submit(tasks);
    }
}
