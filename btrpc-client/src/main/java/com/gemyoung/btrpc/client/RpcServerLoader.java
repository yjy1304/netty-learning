package com.gemyoung.btrpc.client;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author weilong
 * @date 2018/3/6 下午10:42.
 */
public class MessageServerLoader {
    private Lock lock = new ReentrantLock();
    private Condition waitHandler = lock.newCondition();
    private MessageSendHandler messageSendHandler = new
    private MessageServerLoader instance = null;
    private MessageServerLoader(){

    }

    public MessageServerLoader getInstance(){
        if(instance == null){
            synchronized (MessageServerLoader.class){
                if(instance == null){
                    instance = new MessageServerLoader();
                }
            }
        }
        return instance;
    }

    public void setMessageSendHandler(MessageSendHandler messageSendHandler){
        try {
            lock.lock();
            waitHandler.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public MessageSendHandler getMessageSendHandler(){
        try{
            lock.lock();
            waitHandler.wait();
        }catch (InterruptedException e){

        }finally {
            lock.unlock();
        }
    }
}
