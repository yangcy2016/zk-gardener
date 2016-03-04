package com.sand.gardener.config.impl;

import com.sand.gardener.config.AbstractDynamicConfig;
import com.sand.gardener.dynamic.DynamicZkProperty;
import com.sand.gardener.spring.SpringContainer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Properties;

/**
 * @author : huanghy
 * @create : 2016/3/1 0001 下午 4:05
 * @since : ${VERSION}
 */
public class DynamicHikariConfig extends AbstractDynamicConfig {
    private String poolName;
    private boolean autoCommit;
    private String connectionTestQuery;
    private long connectionTimeout;
    private long idleTimeout;
    private long maxLifetime;
    private int maximumPoolSize;
    private int minimumIdle;
    private String dataSourceClassName;
    private Properties dataSourceProperties;
    public DynamicHikariConfig(DynamicZkProperty zkProperty) {
        super(zkProperty);
        poolName = this.zkProperty.getStringValue("hikaricp.poolName","defaultPoolName");
        autoCommit =this.zkProperty.getBooleanValue("hikaricp.autoCommit",false);
        connectionTestQuery = this.zkProperty.getStringValue("hikaricp.connectionTestQuery","select * from dual");
        connectionTimeout = this.zkProperty.getLongValue("hikaricp.connectionTimeout",5000);
        idleTimeout = this.zkProperty.getLongValue("hikaricp.idleTimeout",8000);
        maxLifetime = this.zkProperty.getLongValue("hikaricp.maxLifetime",180000);
        maximumPoolSize = this.zkProperty.getIntValue("hikaricp.maximumPoolSize",50);
        minimumIdle = this.zkProperty.getIntValue("hikaricp.minimumIdle",10);
        dataSourceClassName = this.zkProperty.getStringValue("hikaricp.dataSourceClassName","");
        dataSourceProperties = new Properties();
        dataSourceProperties.setProperty("url",this.zkProperty.getStringValue("hikaricp.dataSource.url",""));
        dataSourceProperties.setProperty("user",this.zkProperty.getStringValue("hikaricp.dataSource.username",""));
        dataSourceProperties.setProperty("password",this.zkProperty.getStringValue("hikaricp.dataSource.password",""));
    }
    public void registerBean() {
        //注册hikariConfig对象
        ClassPathXmlApplicationContext context = SpringContainer.getInstance().getContext();
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)context.getBeanFactory();
        BeanDefinitionBuilder hikariConfigBuilder = BeanDefinitionBuilder.rootBeanDefinition(HikariConfig.class);
        hikariConfigBuilder.addPropertyValue("poolName", poolName);
        hikariConfigBuilder.addPropertyValue("autoCommit", autoCommit);
        hikariConfigBuilder.addPropertyValue("connectionTestQuery", connectionTestQuery);
        hikariConfigBuilder.addPropertyValue("connectionTimeout", connectionTimeout);
        hikariConfigBuilder.addPropertyValue("idleTimeout", idleTimeout);
        hikariConfigBuilder.addPropertyValue("maxLifetime", maxLifetime);
        hikariConfigBuilder.addPropertyValue("maximumPoolSize", maximumPoolSize);
        hikariConfigBuilder.addPropertyValue("minimumIdle", minimumIdle);
        hikariConfigBuilder.addPropertyValue("dataSourceClassName", dataSourceClassName);
        hikariConfigBuilder.addPropertyValue("dataSourceProperties", dataSourceProperties);
        beanFactory.registerBeanDefinition("hikariConfig", hikariConfigBuilder.getBeanDefinition());
        HikariConfig config = beanFactory.getBean("hikariConfig",HikariConfig.class);
        //注册HikariDataSource
        BeanDefinitionBuilder hikariDataSourceBuilder = BeanDefinitionBuilder.rootBeanDefinition(HikariDataSource.class);
        hikariDataSourceBuilder.addConstructorArgValue(config);
        beanFactory.registerBeanDefinition("dataSource",hikariDataSourceBuilder.getBeanDefinition());
        BeanDefinitionBuilder jdbcTemplateBuilder = BeanDefinitionBuilder.rootBeanDefinition(JdbcTemplate.class);
        HikariDataSource dataSource = beanFactory.getBean("dataSource",HikariDataSource.class);
        jdbcTemplateBuilder.addPropertyValue("dataSource",dataSource);
        beanFactory.registerBeanDefinition("jdbcTemplate",jdbcTemplateBuilder.getBeanDefinition());
    }

    public void destroy() {
        ClassPathXmlApplicationContext context = SpringContainer.getInstance().getContext();
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)context.getBeanFactory();
        if(beanFactory.containsBean("jdbcTemplate")){
            beanFactory.removeBeanDefinition("jdbcTemplate");
        }
        if(beanFactory.containsBean("dataSource")){
            HikariDataSource dataSource = beanFactory.getBean("dataSource",HikariDataSource.class);
            beanFactory.destroyBean("dataSource",dataSource);
            beanFactory.removeBeanDefinition("dataSource");
        }
        if(beanFactory.containsBean("hikariConfig")){
            beanFactory.removeBeanDefinition("hikariConfig");
        }
    }
}
