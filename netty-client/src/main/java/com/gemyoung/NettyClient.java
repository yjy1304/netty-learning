package com.gemyoung;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * Created by 健民 on 2018/1/4.
 */
@Service
public class NettyClient{
    private final static String IP_ADDRESS = "127.0.0.1";
    private final static Integer PORT = 8083;

    private Timer timer = new HashedWheelTimer();
    @PostConstruct
    public void init() throws Exception{
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
        ChannelFuture cf = bootstrap.connect(IP_ADDRESS, PORT);
        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                MessageSendHandler messageSendHandler = future.channel().pipeline().get(MessageSendHandler.class);
                RpcServerLoader.getInstance().setMessageSendHandler(messageSendHandler);
            }
        });
    }
}
