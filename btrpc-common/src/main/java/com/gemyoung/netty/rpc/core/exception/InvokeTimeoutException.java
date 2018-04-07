package com.gemyoung.netty.rpc.core.exception;

/**
 * @author weilong
 * @date 2018/4/7 下午12:52.
 */
public class InvokeTimeoutException extends RuntimeException {
    public InvokeTimeoutException(String msg){
        super(msg);
    }
}
