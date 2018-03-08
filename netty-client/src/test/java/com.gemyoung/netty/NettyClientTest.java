package netty;

import com.gemyoung.Bootstrap;
import com.gemyoung.MessageSendProxy;
import com.gemyoung.RemoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Proxy;

/**
 * @author weilong
 * @date 2018/3/8 下午8:10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Bootstrap.class)
@SpringBootTest
public class NettyClientTest {
    @Test
    public void testMessageSendProxy(){
        RemoteService remoteService = (RemoteService)Proxy.newProxyInstance(RemoteService.class.getClassLoader(), new Class[]{RemoteService.class}, new MessageSendProxy(RemoteService.class));
        remoteService.call("111", "222");
    }

}
