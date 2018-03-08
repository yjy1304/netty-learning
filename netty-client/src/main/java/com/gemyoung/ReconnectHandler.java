package com.gemyoung;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * @author weilong
 * @date 2018/3/8 下午4:06.
 */
public class ReconnectHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        super.channelInactive(ctx);
        ctx.executor().schedule(new ReconnectTask(), 2, TimeUnit.SECONDS);
    }
}
