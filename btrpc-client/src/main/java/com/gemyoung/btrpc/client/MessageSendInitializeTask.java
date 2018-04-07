package com.gemyoung.btrpc.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.Callable;

/**
 * @author weilong
 * @date 2018/4/7 上午9:41.
 */
public class MessageSendInitializeTask implements Callable<Boolean> {
    private NettyClient nettyClient;

    public MessageSendInitializeTask(NettyClient nettyClient){
        this.nettyClient = nettyClient;
    }
    @Override
    public Boolean call() throws Exception {
        ChannelFuture channelFuture = nettyClient.doConnect();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    //连接成功后调用setMessageSendHandler()
                    RpcServerLoader.getInstance().setMessageSendHandler(future.channel().pipeline().get(MessageSendHandler.class));
                }
            }
        });
        return true;
    }

}
