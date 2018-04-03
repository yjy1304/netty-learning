package com.gemyoung.btrpc.client;

import com.google.common.reflect.Reflection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author weilong
 * @date 2018/3/16 上午11:19.
 */

/**
 * 每个@Reference注解的接口都要通过Reflection.newProxy方法生产相应的代理(代理类实现了各种interface)，故采用了工厂方法
 */
@Slf4j
public class RpcReqProxyFactoryBean implements FactoryBean, InitializingBean {
    private String rpcInterface;
    @Override
    public Object getObject() throws Exception {
        return Reflection.newProxy(getClazzByName(rpcInterface), new MessageSendProxy());
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return this.getClass().getClassLoader().loadClass(rpcInterface);
        } catch (ClassNotFoundException e) {
            System.err.println("spring analyze fail!");
        }
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private Class<?> getClazzByName(String className){
        try {
            return this.getClass().getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            log.error("failed to load class by name [{}]", className);
        }
        return null;
    }

    public void setRpcInterface(String rpcInterface) {
        this.rpcInterface = rpcInterface;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.error("wahaha~~~~");
    }
}
