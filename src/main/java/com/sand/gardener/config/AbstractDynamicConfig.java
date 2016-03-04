package com.sand.gardener.config;

import com.google.common.base.Preconditions;
import com.sand.gardener.dynamic.DynamicZkProperty;
import com.sand.gardener.spring.SpringContainer;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : huanghy
 * @create : 2016/3/3 0003 上午 10:10
 * @since : ${VERSION}
 */
public abstract class AbstractDynamicConfig implements RefreshableConfig{

    protected DefaultListableBeanFactory beanFactory;
    protected DynamicZkProperty zkProperty;
    public AbstractDynamicConfig(DynamicZkProperty zkProperty) {
        Preconditions.checkNotNull(zkProperty, "zkProperty can not be null");
        this.zkProperty = zkProperty;
        ClassPathXmlApplicationContext context = SpringContainer.getInstance().getContext();
        beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
    }

    public void refresh() {
        destroy();
        registerBean();
    }
}
