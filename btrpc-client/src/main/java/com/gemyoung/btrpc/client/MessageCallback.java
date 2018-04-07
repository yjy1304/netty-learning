package com.gemyoung.btrpc.client;

import com.gemyoung.netty.rpc.core.MessageRequest;
import com.gemyoung.netty.rpc.core.MessageResponse;
import com.gemyoung.netty.rpc.core.RpcSystemConfig;
import com.gemyoung.netty.rpc.core.exception.InvokeModuleException;
import com.gemyoung.netty.rpc.core.exception.InvokeTimeoutException;
import com.gemyoung.netty.rpc.core.exception.RejectResponeException;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author weilong
 * @date 2018/3/4 下午11:22.
 */
@Data
public class MessageCallback {
    private MessageRequest messageRequest;
    private MessageResponse response;
    private Lock lock = new ReentrantLock();
    private Condition finish = lock.newCondition();


    public MessageCallback(MessageRequest messageRequest){
        this.messageRequest = messageRequest;
    }

    public Object start() {
        try {
            lock.lock();
            await();
            if (this.response != null) {
                boolean isInvokeSucc = getInvokeResult();
                if (isInvokeSucc) {
                    if (StringUtils.isEmpty(this.response.getError())) {
                        return this.response.getResult();
                    } else {
                        throw new InvokeModuleException(this.response.getError());
                    }
                } else {
                    throw new RejectResponeException("hhh");
                }
            } else {
                return null;
            }
        } finally {
            lock.unlock();
        }
    }

    public void over(MessageResponse reponse) {
        try {
            lock.lock();
            finish.signal();
            this.response = reponse;
        } finally {
            lock.unlock();
        }
    }

    private void await() {
        boolean timeout = false;
        try {
            timeout = finish.await(30000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!timeout) {
            throw new InvokeTimeoutException("time out!");
        }
    }

    private boolean getInvokeResult() {
        return true;
//        return (!this.response.getError().equals(RpcSystemConfig.FILTER_RESPONSE_MSG) &&
//                (!this.response.isReturnNotNull() || (this.response.isReturnNotNull() && this.response.getResult() != null)));
    }
}
