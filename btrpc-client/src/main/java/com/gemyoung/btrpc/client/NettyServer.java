package com.gemyoung.btrpc.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author weilong
 * @date 2017/12/15 下午5:07.
 */
@SpringBootApplication
public class NettyServer {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(NettyServer.class, args);
    }
}
