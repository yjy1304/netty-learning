package com.gemyoung;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author weilong
 * @date 2018/3/4 下午11:22.
 */
public class MessageCallback {
    private MessageRequest request;
    private MessageResponse response;
    private Lock lock = new ReentrantLock();
    private Condition waitRet = lock.newCondition();

    public MessageCallback(MessageRequest request){
        this.request = request;
    }
    public Object start() throws InterruptedException{
        try {
            lock.tryLock();
            waitRet.await(10*1000, TimeUnit.MILLISECONDS);
            if(response != null){
                return response.getResult();
            }else {
                return null;
            }
        }finally {
            lock.unlock();
        }

    }
}
