package com.gemyoung.btrpc.client;

import com.oracle.webservices.internal.api.message.PropertySet;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * Created by 健民 on 2018/1/4.
 */
@ChannelHandler.Sharable
public class NettyClient extends ChannelInboundHandlerAdapter implements TimerTask{
    private Bootstrap bootstrap;
    private String ipAddr = "127.0.0.1";
    private Integer port = 8082;

    private Timer timer = new HashedWheelTimer();

    public NettyClient(String ipAddr, Integer port){
        this.ipAddr = ipAddr;
        this.port = port;
    }

    public ChannelFuture doConnect() throws InterruptedException {
        bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                    .addLast(new LengthFieldPrepender(4))
                                    .addLast(new ObjectEncoder())
                                    .addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())))
                                    .addLast(new MessageSendHandler());
                    }
                });
        return bootstrap.connect(ipAddr, port);
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
