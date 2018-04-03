package com.gemyoung.btrpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import java.util.concurrent.TimeUnit;

/**
 * Created by 健民 on 2018/1/4.
 */
@ChannelHandler.Sharable
public class NettyClient extends ChannelInboundHandlerAdapter implements TimerTask, ChannelHandlerHolder{
    private Bootstrap bootstrap;
    private final static String IP_ADDRESS = "127.0.0.1";
    private final static Integer PORT = 8083;

    private Timer timer = new HashedWheelTimer();
    public static void main(String[] args) throws Exception{
        NettyClient nettyClient = new NettyClient();
        nettyClient.doConnect();
    }

    public ChannelFuture doConnect() throws InterruptedException {
        bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(handlers());
                    }
                });
        return bootstrap.connect(IP_ADDRESS, PORT);
    }

    @Override
    public ChannelHandler[] handlers() {
        return new ChannelHandler[]{new StringDecoder(), new StringDecoder(), this};
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        ChannelFuture future = null;
        try{
            future = this.doConnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        future.addListener(new ChannelFutureListener() {
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

    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        System.out.println("连接已经断开，尝试重连中");
        timer.newTimeout(this, 2, TimeUnit.SECONDS);
        ctx.fireChannelInactive();//向其后的handler传递该事件
    }
}
