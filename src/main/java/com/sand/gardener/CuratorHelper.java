package com.sand.gardener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : huanghy
 * @create : 2009/1/1 0001 上午 12:58
 * @since : ${VERSION}
 */
public class CuratorHelper {
    private static CuratorFramework client;
    private static String connectString="192.168.94.135:2181,192.168.94.135:3181,192.168.94.135:4181";
    private static int baseSleepMs=1000;
    private static int maxRetryTimes = 3;
    private static int timeouts = 3000;

    private static Logger logger = LoggerFactory.getLogger(CuratorHelper.class);
    private CuratorHelper(){
    }

    public static synchronized CuratorFramework getCuratorFramework(){
        if(client==null){
            client = CuratorFrameworkFactory.builder()
                    .connectString(connectString)
                    .retryPolicy(new ExponentialBackoffRetry(baseSleepMs, maxRetryTimes))
                    .connectionTimeoutMs(timeouts)
                    .build();
            client.start();
            logger.info("start zookeeper connectString:{},timeouts:{}",connectString,timeouts);
        }
        return client;
    }


    public CuratorFramework getStartedCuratorFramework(){
        return client;
    }

}
