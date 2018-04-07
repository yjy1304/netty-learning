package com.gemyoung.btrpc.client;

import com.gemyoung.netty.rpc.core.MessageRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author weilong
 * @date 2018/2/26 下午10:15.
 */
public class MessageSendProxy implements InvocationHandler{
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessageId(UUID.randomUUID().toString());
        messageRequest.setClassName(method.getDeclaringClass().getName());
        messageRequest.setMethodName(method.getName());
        messageRequest.setParameterTypes(method.getParameterTypes());
        messageRequest.setParamVals(args);
        MessageCallback callback = RpcServerLoader.getInstance().getMessageSendHandler().sendRequest(messageRequest);
        return callback.start();
    }
}
