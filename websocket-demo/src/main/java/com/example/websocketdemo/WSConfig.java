package com.example.websocketdemo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

@Configuration
public class WSConfig {

    @Bean
    public static BeanPostProcessor integrationDynamicWebSocketHandlerMappingWorkaround() {
        return new BeanPostProcessor() {

            @Override public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean.getClass().getSimpleName().equals("IntegrationDynamicWebSocketHandlerMapping")) {
                    ((AbstractHandlerMapping) bean).setOrder(0);
                }
                return bean;
            }
        };
    }
}
