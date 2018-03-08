package com.gemyoung;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by 健民 on 2018/1/2.
 */
public class TestInboundHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        System.out.println(msg);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("ddd");
    }
}
