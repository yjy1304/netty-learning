package com.gemyoung;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author weilong
 * @date 2018/3/8 下午4:24.
 */
public class ReconnectTask implements Runnable{
    private final static String IP_ADDRESS = "127.0.0.1";
    private final static Integer PORT = 8083;

    @Override
    public void run() {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new StringDecoder())
                                        .addLast(new StringEncoder())
                                        .addLast(new ReconnectHandler());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(IP_ADDRESS, PORT);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                boolean succ =  future.isSuccess();
                if(succ){
                    System.out.println("重连成功");
                }else{
                    System.out.println("重连失败，继续尝试");
                    future.channel().pipeline().fireChannelInactive();//重连失败，重新触发连接断开事件
                }
            }
        });
    }
}
