package com.gemyoung.btrpc.client;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author weilong
 * @date 2018/3/30 下午4:00.
 */
public class SpringTest {
    @Test
    public void testRpcReqProxyFactoryBean(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(SpringTest.class.getPackage().getName().replace('.', '/') + "/spring-test.xml");
        ctx.start();
        try{
            RemoteService remoteService = (RemoteService)ctx.getBean("rpcReqProxy");
            remoteService.call("1", "2");
        } finally {
            ctx.stop();
            ctx.close();
        }
    }
}
