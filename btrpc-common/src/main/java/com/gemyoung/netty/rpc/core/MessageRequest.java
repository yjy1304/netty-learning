package com.gemyoung.netty.rpc.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author weilong
 * @date 2018/3/14 下午12:02.
 */
@Data
public class MessageRequest implements Serializable{
    private static final long serialVersionUID = 5244586178164639308L;
    private String messageId;
    private String className;
    private String methodName;
    private Class[] parameterTypes;
    private Object[] paramVals;
}
