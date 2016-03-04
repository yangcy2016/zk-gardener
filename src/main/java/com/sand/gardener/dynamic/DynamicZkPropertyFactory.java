package com.sand.gardener.dynamic;

import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicWatchedConfiguration;
import com.netflix.config.source.ZooKeeperConfigurationSource;
import com.sand.gardener.CuratorHelper;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DynamicZkProperty的工厂类，提供初始化和装配
 * @author : huanghy
 * @create : 2016/2/29 0029 上午 11:05
 * @since : ${VERSION}
 */
public class DynamicZkPropertyFactory {
    private static Logger logger = LoggerFactory.getLogger(DynamicZkPropertyFactory.class);
    public static DynamicZkProperty getDynamicZkProperty(String rootPath){
        CuratorFramework startedClient = CuratorHelper.getCuratorFramework();
        ZooKeeperConfigurationSource zkConfigSource = new ZooKeeperConfigurationSource(startedClient,rootPath);
        try {
            zkConfigSource.start();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("init ZkProperty exception cause:{}",e.getStackTrace());
        }
        DynamicWatchedConfiguration zkDynamicConfig = new DynamicWatchedConfiguration(zkConfigSource);
        ConfigurationManager.install(zkDynamicConfig);
        DynamicPropertyFactory propertyFactory = DynamicPropertyFactory.getInstance();
        return new DynamicZkProperty(startedClient,propertyFactory);
    }
}
