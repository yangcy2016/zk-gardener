package com.sand.gardener.test;

import com.sand.gardener.dynamic.DynamicZkProperty;
import com.sand.gardener.dynamic.DynamicZkPropertyFactory;
import com.sand.gardener.config.impl.DynamicHikariConfig;
import com.sand.gardener.spring.SpringContainer;
import com.zaxxer.hikari.HikariConfig;
import org.junit.Before;
import org.junit.Test;

/**
 * @author : huanghy
 * @create : 2016/3/1 0001 下午 3:33
 * @since : ${VERSION}
 */
public class SpringContainerTest {
    private SpringContainer container;
    DynamicZkProperty zkProperty;
    @Before
    public void init(){
        container = SpringContainer.getInstance();
        container.initContainer();
        zkProperty = DynamicZkPropertyFactory.getDynamicZkProperty("/myapp/config");
    }

    @Test
    public void testGetBean() throws InterruptedException {
        DynamicHikariConfig resource = new DynamicHikariConfig(zkProperty);
        resource.registerBean();
        while (true){
            HikariConfig config = container.getBean("hikariConfig" ,HikariConfig.class);
            System.out.println(config);
            Thread.sleep(1000);
        }

    }

    @Test
    public void testRegisterBean(){
        container.registerBeanDefinition("hikari",HikariConfig.class);
        HikariConfig config = container.getBean("hikari",HikariConfig.class);
        System.out.println(config);
    }

    @Test
    public void testRemoveRegisterBean(){
        container.removeBeanDefinition("hikariConfig");
        HikariConfig config = container.getBean("hikariConfig" ,HikariConfig.class);
        System.out.println(config);
    }
}
