package com.gemyoung.netty.rpc.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author weilong
 * @date 2018/3/4 下午11:29.
 */
@Data
public class MessageResponse implements Serializable{
    private static final long serialVersionUID = -1359840966229463702L;
    private String messageId;
    private String error;
    private Object result;

    public MessageResponse(String messageId, Object result, String error){
        this.messageId = messageId;
        this.result = result;
        this.error = error;
    }
}
