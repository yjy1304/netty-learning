package com.gemyoung.btrpc.spring;

import com.gemyoung.btrpc.client.RpcReqProxyFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author weilong
 * @date 2018/3/18 下午10:02.
 */
public class BtRpcReferenceParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String interfaceName = element.getAttribute("interfaceName");
        String id = element.getAttribute("id");
        String ipAddr = element.getAttribute("ipAddr");
        String protocolType = element.getAttribute("protocol");

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(RpcReqProxyFactoryBean.class);
        beanDefinition.setLazyInit(false);

        beanDefinition.getPropertyValues().addPropertyValue("interfaceName", interfaceName);
        beanDefinition.getPropertyValues().addPropertyValue("ipAddr", ipAddr);
        beanDefinition.getPropertyValues().addPropertyValue("protocol", protocolType);

        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
        return null;
    }
}
