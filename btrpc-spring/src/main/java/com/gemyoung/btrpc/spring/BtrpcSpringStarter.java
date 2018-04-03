package com.gemyoung.btrpc.spring;

import javax.annotation.PostConstruct;

/**
 * @author weilong
 * @date 2018/3/16 上午12:01.
 */
public class BtrpcSpringStarter {
    @PostConstruct
    public void init(){
        new NettyClient();
    }
}
