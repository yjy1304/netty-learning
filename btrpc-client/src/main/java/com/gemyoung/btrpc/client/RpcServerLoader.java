package com.gemyoung.btrpc.client;

import com.gemyoung.netty.rpc.core.RpcSystemConfig;
import com.google.common.util.concurrent.*;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author weilong
 * @date 2018/3/6 下午10:42.
 */
public class RpcServerLoader {
    private Lock lock = new ReentrantLock();
    private Condition waitHandler = lock.newCondition();
    private MessageSendHandler messageSendHandler = null;
    private static volatile RpcServerLoader instance = null;
    private final static String DELIMITER = ":";
    private static int threadNums = RpcSystemConfig.SYSTEM_PROPERTY_THREADPOOL_THREAD_NUMS;
    private static int queueNums = RpcSystemConfig.SYSTEM_PROPERTY_THREADPOOL_QUEUE_NUMS;
    private static ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();
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

    public MessageSendHandler getMessageSendHandler() throws InterruptedException{
        if(messageSendHandler != null){
            return messageSendHandler;
        }
        Callable callable = new MessageSendInitializeTask(new NettyClient("127.0.0.1", 8082));
        threadPoolExecutor.submit(callable);
        try{
            lock.lock();
            waitHandler.await();
            return messageSendHandler;
        }finally {
            lock.unlock();
        }
    }

//    public void load(String serverAddress) {
//        String[] ipAddr = serverAddress.split(RpcServerLoader.DELIMITER);
//        if (ipAddr.length == 2) {
//            String host = ipAddr[0];
//            int port = Integer.parseInt(ipAddr[1]);
//            final InetSocketAddress remoteAddr = new InetSocketAddress(host, port);
//
//            System.out.printf("BT-RPC Client start success!\nip:%s\nport:%d\n\n", host, port);
//
//            ListenableFuture<Boolean> listenableFuture = threadPoolExecutor.submit(new MessageSendInitializeTask(new NettyClient(host, port)));
//
//            Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {
//                @Override
//                public void onSuccess(Boolean result) {
//                    try {
//                        lock.lock();
//
//                        if (messageSendHandler == null) {
//                            handlerStatus.await();
//                        }
//
//                        if (result.equals(Boolean.TRUE) && messageSendHandler != null) {
//                            connectStatus.signalAll();
//                        }
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(RpcServerLoader.class.getName()).log(Level.SEVERE, null, ex);
//                    } finally {
//                        lock.unlock();
//                    }
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                    t.printStackTrace();
//                }
//            }, threadPoolExecutor);
//        }
//    }
}
