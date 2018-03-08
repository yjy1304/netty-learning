package com.gemyoung;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author weilong
 * @date 2018/3/6 下午10:45.
 */
public class MessageSendHandler extends ChannelInboundHandlerAdapter {
    private ConcurrentHashMap<String, MessageCallback> mapCallBack = new ConcurrentHashMap<String, MessageCallback>();

    private Channel channel;

    private SocketAddress remoteAddress;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        //父类的默认行为是传递给下一个handler
        super.channelActive(ctx);
        remoteAddress = ctx.channel().remoteAddress();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception{
        super.channelRegistered(ctx);
        channel = ctx.channel();
    }

    public MessageCallback sendRequest(MessageRequest request){
        MessageCallback callback = new MessageCallback(request);
        mapCallBack.put(request.getMessageId(), callback);
        channel.writeAndFlush(request);
        return callback;
    }
}
