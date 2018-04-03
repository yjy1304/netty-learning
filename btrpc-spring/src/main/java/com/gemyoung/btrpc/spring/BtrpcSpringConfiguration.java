package com.gemyoung.btrpc.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author weilong
 * @date 2018/3/15 下午11:44.
 */
@Configuration
public class BtrpcSpringConfiguration {
    @Bean
    public BtrpcSpringStarter btrpcSpringStarter(){
        return new BtrpcSpringStarter();
    }
}
