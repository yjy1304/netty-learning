package com.gemyoung;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author weilong
 * @date 2018/2/26 下午10:15.
 */
@Slf4j
public class MessageSendProxy<T> implements InvocationHandler{
    private Class<T> cls;

    public MessageSendProxy(Class<T> cls){
        this.cls = cls;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessageId(UUID.randomUUID().toString());
        messageRequest.setClassName(method.getDeclaringClass().getName());
        messageRequest.setMethodName(method.getName());
        messageRequest.setParameterTypes(method.getParameterTypes());
        messageRequest.setParamVals(args);
        log.info("RPC调用方法，请求数据:{}", messageRequest);
        MessageCallback callback = RpcServerLoader.getInstance().getMessageSendHandler().sendRequest(messageRequest);
        return callback.start();
    }
}
