package com.sand.gardener.test;

import com.sand.gardener.config.RefreshableConfig;
import com.sand.gardener.config.impl.DynamicHikariConfig;
import com.sand.gardener.dynamic.DynamicZkProperty;
import com.sand.gardener.dynamic.DynamicZkPropertyFactory;
import com.sand.gardener.spring.SpringContainer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : huanghy
 * @create : 2016/3/2 0002 上午 10:20
 * @since : ${VERSION}
 */
public class ZkPropertyChangeTest {
    private String rootPath = "/myapp/config";
    private SpringContainer container;
    private DynamicZkProperty zkProperty;

    @Before
    public void init(){
        container = SpringContainer.getInstance();
        zkProperty = DynamicZkPropertyFactory.getDynamicZkProperty(rootPath);
        container.initContainer().addDynamicConfig(rootPath,
                new DynamicHikariConfig(zkProperty))
                .registerConfig();
    }

    @Test
    public void test() throws Exception {
        final RefreshableConfig r = container.getDynamicConfig(rootPath);
        ExecutorService executor = Executors.newFixedThreadPool(3);
        zkProperty.addPathChildrenCacheListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()){
                    case CHILD_UPDATED:
                        r.refresh();
                        System.out.println("DbConfig changed...");
                        System.out.println("Thread["+Thread.currentThread().getName()+"],refresh");
                }
            }
        },rootPath,executor);

        while (true){
            JdbcTemplate jdbcTemplate = container.getBean("jdbcTemplate", JdbcTemplate.class);
            System.out.println("Thread[" + Thread.currentThread().getName() + "],"+jdbcTemplate);
           Thread.sleep(1000);
        }
    }
}
