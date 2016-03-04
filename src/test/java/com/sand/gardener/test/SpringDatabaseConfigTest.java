package com.sand.gardener.test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * @author : huanghy
 * @create : 2016/2/29 0029 下午 5:47
 * @since : ${VERSION}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config.xml")
public class SpringDatabaseConfigTest extends AbstractJUnit4SpringContextTests{
    @Test
    public void testBuildDataSource(){
        System.out.println(applicationContext.getBean(HikariDataSource.class));
    }

    @Test
    public void testBuildJdbcTemplate(){
        System.out.println(applicationContext.getBean(JdbcTemplate.class));
    }
    @Test
    public void testQuery(){
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        List<Map<String, Object>> results = jdbcTemplate.queryForList("select * from acc_pay_account");
        System.out.println(results);
    }

    @Test
    public void test(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(HikariConfig.class);
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)context.getBeanFactory();
        beanFactory.registerBeanDefinition("testConfig",beanDefinitionBuilder.getBeanDefinition());
        System.out.println(context.getBean("testConfig"));
    }
}
