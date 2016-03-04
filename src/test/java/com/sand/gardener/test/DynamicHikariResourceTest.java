package com.sand.gardener.test;

import com.sand.gardener.dynamic.DynamicZkProperty;
import com.sand.gardener.dynamic.DynamicZkPropertyFactory;
import com.sand.gardener.config.impl.DynamicHikariConfig;
import com.sand.gardener.spring.SpringContainer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

/**
 * @author : huanghy
 * @create : 2016/3/1 0001 下午 4:47
 * @since : ${VERSION}
 */
public class DynamicHikariResourceTest {
    SpringContainer container;
    DynamicZkProperty zkProperty;
    String rootPath = "/myapp/config";
    @Before
    public void init(){
        container = SpringContainer.getInstance();
        container.initContainer();
        zkProperty = DynamicZkPropertyFactory.getDynamicZkProperty(rootPath);
    }

    @Test
    public void testInitialization(){
        DynamicHikariConfig resource = new DynamicHikariConfig(zkProperty);
        resource.registerBean();
        HikariConfig config = container.getBean("hikariConfig",HikariConfig.class);
        System.out.println(config);
        HikariDataSource dataSource  = container.getBean("dataSource",HikariDataSource.class);
        System.out.println(dataSource);
        JdbcTemplate jdbcTemplate = container.getBean("jdbcTemplate",JdbcTemplate.class);
        Map<String,Object> result =  jdbcTemplate.queryForMap("select * from acc_pay_account where acc_no=?","8888600610000043");
        System.out.println(result);
    }


    @Test
    public void testDestroy(){
        DynamicHikariConfig resource = new DynamicHikariConfig(zkProperty);
        resource.registerBean();
        HikariConfig config = container.getBean("hikariConfig",HikariConfig.class);
        System.out.println(config);
        resource.destroy();
        resource.refresh();
        config = container.getBean("hikariConfig",HikariConfig.class);
        System.out.println(config);
        HikariDataSource dataSource  = container.getBean("dataSource",HikariDataSource.class);
        System.out.println(dataSource);
        JdbcTemplate jdbcTemplate = container.getBean("jdbcTemplate",JdbcTemplate.class);
        System.out.println(jdbcTemplate);
    }
}
