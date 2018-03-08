package com.gemyoung;

import lombok.Data;

import java.io.Serializable;

/**
 * @author weilong
 * @date 2018/2/26 下午9:51.
 */
@Data
public class MessageRequest implements Serializable{
    private String messageId;
    private String className;
    private String methodName;
    private Class[] parameterTypes;
    private Object[] paramVals;
}
