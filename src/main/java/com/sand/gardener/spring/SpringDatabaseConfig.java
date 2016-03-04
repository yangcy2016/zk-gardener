package com.sand.gardener.spring;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Properties;

/**
 * @author : huanghy
 * @create : 2016/2/29 0029 下午 5:02
 * @since : ${VERSION}
 */
public class SpringDatabaseConfig {

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

    private void init(){
        poolName = "abacusMasterPool";
        autoCommit = false;
        connectionTestQuery = "select 1 from dual";
        connectionTimeout = 30000;
        idleTimeout = 600000;
        maxLifetime = 1800000;
        dataSourceClassName = "oracle.jdbc.pool.OracleDataSource";
        maximumPoolSize = 50;
        minimumIdle = 10;
        dataSourceProperties = new Properties();
        dataSourceProperties.setProperty("url","jdbc:oracle:thin:@172.28.250.122:1521:devdb");
        dataSourceProperties.setProperty("user","accuser_dev");
        dataSourceProperties.setProperty("password","oracle");
    }

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource(){
        HikariDataSource dataSource = new HikariDataSource(hikariConfig());
        return  dataSource;
    }
    @Bean(name = "hikariConfig")
    public HikariConfig hikariConfig(){
        init();
        HikariConfig config = new HikariConfig();
        config.setPoolName(poolName);
        config.setAutoCommit(autoCommit);
        config.setConnectionTestQuery(connectionTestQuery);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumIdle);
        config.setDataSourceClassName(dataSourceClassName);
        config.setDataSourceProperties(dataSourceProperties);
        return config;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }
}
