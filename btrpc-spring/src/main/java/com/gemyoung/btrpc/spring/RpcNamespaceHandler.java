package com.gemyoung.btrpc.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author weilong
 * @date 2018/3/18 下午9:59.
 */
public class RpcNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("service", new BtRpcServiceParser());
        registerBeanDefinitionParser("registry", new BtRpcRegisteryParser());
        registerBeanDefinitionParser("reference", new BtRpcReferenceParser());
    }
}
