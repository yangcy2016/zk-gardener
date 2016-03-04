package com.sand.gardener.spring;

import com.sand.gardener.config.RefreshableConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : huanghy
 * @create : 2016/3/1 0001 下午 3:17
 * @since : ${VERSION}
 */
public class SpringContainer {
    private static final SpringContainer instance = new SpringContainer();
    private static final String DEFAULT_CONFIG_FILE = "classpath:spring-config.xml";
    private static Map<String,RefreshableConfig> registedDynamicResources = new ConcurrentHashMap<String, RefreshableConfig>(10);
    private ClassPathXmlApplicationContext context;
    private SpringContainer(){}

    public static SpringContainer getInstance(){
        return instance;
    }

    public SpringContainer addDynamicConfig(String listenerPath,RefreshableConfig resource){
        registedDynamicResources.put(listenerPath,resource);
        return instance;
    }

    public void registerConfig(){
        for(Iterator<String> keys = registedDynamicResources.keySet().iterator();keys.hasNext();){
            String key = keys.next();
            registedDynamicResources.get(key).registerBean();
        }
    }

    public RefreshableConfig getDynamicConfig(String listenerPath){
        return registedDynamicResources.get(listenerPath);
    }

    public SpringContainer initContainer(){
        context = new ClassPathXmlApplicationContext(DEFAULT_CONFIG_FILE);
        context.start();
        return instance;
    }
    public ClassPathXmlApplicationContext getContext(){
        if(context==null){
            throw new IllegalStateException("container is not init");
        }
        return context;
    }

    public <T> T getBean(String beanName,Class<T> clazz){

        return getContext().getBean(beanName, clazz);
    }

    public Object getBean(String beanName){
        return getContext().getBean(beanName);
    }

    public void registerBeanDefinition(String beanName,Class clazz){
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)getContext().getBeanFactory();
        beanFactory.registerBeanDefinition(beanName,builder.getBeanDefinition());
    }
    public void registerBeanDefinitionWithInit(String beanName,Class clazz,String initMethod){
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        builder.setInitMethodName(initMethod);
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)getContext().getBeanFactory();
        beanFactory.registerBeanDefinition(beanName,builder.getBeanDefinition());
    }
    public void registerBeanDefinition(String beanName,Class clazz,String initMethod,String destroyMethod){
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        builder.setInitMethodName(initMethod);
        builder.setDestroyMethodName(destroyMethod);
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)getContext().getBeanFactory();
        beanFactory.registerBeanDefinition(beanName,builder.getBeanDefinition());
    }
    public void registerBeanDefinitionWithDestroy(String beanName,Class clazz,String destroyMethod){
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        builder.setDestroyMethodName(destroyMethod);
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)getContext().getBeanFactory();
        beanFactory.registerBeanDefinition(beanName,builder.getBeanDefinition());
    }
    public void removeBeanDefinition(String beanName){
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)getContext().getBeanFactory();
        if(beanFactory.containsBean(beanName)) {
            beanFactory.destroyBean(beanName, this.getBean(beanName));
            beanFactory.removeBeanDefinition(beanName);
        }
    }
}
