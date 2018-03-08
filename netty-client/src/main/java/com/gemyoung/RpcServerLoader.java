package com.gemyoung;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author weilong
 * @date 2018/3/6 下午10:42.
 */
public class RpcServerLoader {
    private Lock lock = new ReentrantLock();
    private Condition waitHandler = lock.newCondition();
    private MessageSendHandler messageSendHandler = null;
    private static RpcServerLoader instance = null;
    private RpcServerLoader(){

    }

    public static RpcServerLoader getInstance(){
        if(instance == null){
            synchronized (RpcServerLoader.class){
                if(instance == null){
                    instance = new RpcServerLoader();
                }
            }
        }
        return instance;
    }

    public void setMessageSendHandler(MessageSendHandler messageSendHandler){
        try {
            lock.lock();
            this.messageSendHandler = messageSendHandler;
            waitHandler.signalAll();
        }finally {
            lock.unlock();
        }
    }

    /**
     * 若MessageSendHandler未被先设置该方法会在wait处hang住并释放锁
     * @return
     */
    public MessageSendHandler getMessageSendHandler(){
        if(messageSendHandler != null){
            return messageSendHandler;
        }
        try{
            lock.lock();
            if(messageSendHandler == null){
                waitHandler.wait();
            }
            return messageSendHandler;
        }catch (InterruptedException e){
            return null;
        }finally {
            lock.unlock();
        }
    }
}
