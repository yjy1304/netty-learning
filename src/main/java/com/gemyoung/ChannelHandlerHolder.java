package com.gemyoung;

import io.netty.channel.ChannelHandler;

/**
 * Created by 健民 on 2018/2/25.
 */
public interface ChannelHandlerHolder {

    ChannelHandler[] handlers();
}
