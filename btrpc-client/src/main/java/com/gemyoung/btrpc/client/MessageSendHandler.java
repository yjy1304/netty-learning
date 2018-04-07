package com.gemyoung.btrpc.client;

import com.gemyoung.netty.rpc.core.MessageRequest;
import com.gemyoung.netty.rpc.core.MessageResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author weilong
 * @date 2018/3/6 下午10:45.
 */
public class MessageSendHandler extends ChannelInboundHandlerAdapter {
    public Map<String, MessageCallback> callbackMap = new ConcurrentHashMap();
    public Channel channel = null;
    public SocketAddress socketAddress = null;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        super.channelActive(ctx);
        socketAddress = ctx.channel().remoteAddress();
    }
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws  Exception{
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        MessageResponse response = (MessageResponse) msg;
        String messageId = response.getMessageId();
        MessageCallback callBack = callbackMap.get(messageId);
        if (callBack != null) {
            callbackMap.remove(messageId);
            callBack.over(response);
        }
    }

    public MessageCallback sendRequest(MessageRequest messageRequest){
        MessageCallback callback = new MessageCallback(messageRequest);
        callbackMap.put(messageRequest.getMessageId(), callback);
        ChannelFuture channelFuture = channel.writeAndFlush(messageRequest);
        return callback;
    }
}
