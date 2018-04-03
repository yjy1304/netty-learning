package com.gemyoung;

import com.gemyoung.netty.rpc.core.MessageRequest;
import com.gemyoung.netty.rpc.core.MessageResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by 健民 on 2018/2/25.
 */
@Slf4j
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        log.error("发生异常,关闭连接", cause);
        ctx.close();
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        MessageRequest msgReq = (MessageRequest)msg;
        log.info("收到请求:{}", msgReq);
        final MessageResponse messageResponse = new MessageResponse(msgReq.getMessageId(),"hhhRet", null);
        ctx.writeAndFlush(messageResponse).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.info("RPC Server Send message-id respone:" + messageResponse.getMessageId());
            }
        });
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        log.info("连接已经建立");
    }
}
