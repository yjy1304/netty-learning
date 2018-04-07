package com.gemyoung.btrpc.client;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author weilong
 * @date 2018/3/30 下午4:00.
 */
public class SpringTest {
    @Test
    public void testRpcReqProxyFactoryBean() throws Exception{
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(SpringTest.class.getPackage().getName().replace('.', '/') + "/spring-test.xml");
        ctx.start();
        try{
            RemoteService2 remoteService2 = (RemoteService2)ctx.getBean("remoteService2");
            String ret = remoteService2.call("1", "2");
            System.out.println(ret);
        } finally {
            ctx.stop();
            ctx.close();
        }
    }
}
