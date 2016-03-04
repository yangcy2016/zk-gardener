package com.sand.gardener.dynamic;

import com.google.common.base.Preconditions;
import com.netflix.config.DynamicPropertyFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.Executor;

/**
 *
 * @author : huanghy
 * @create : 2016/3/2 0002 上午 9:37
 * @since : ${VERSION}
 */
public class DynamicZkProperty {
    private CuratorFramework client;
    private DynamicPropertyFactory propertyFactory;

    public DynamicZkProperty(CuratorFramework client, DynamicPropertyFactory propertyFactory) {
        this.client = client;
        this.propertyFactory = propertyFactory;
    }

    /**
     * 创建指定的节点
     * @param path 节点名
     * @param value 初始化数据
     * @throws Exception
     */
    public void creatProperty(String path,String value) throws Exception {
        Preconditions.checkNotNull(path,"path cannot be null");
        Preconditions.checkNotNull(value,"value cannot be null");
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path, value.getBytes());
    }

    /**
     * 根据指定的节点删除当前节点和字节点
     * @param path
     * @throws Exception
     */
    public void deleteProperty(String path) throws Exception {
        Preconditions.checkNotNull(path,"path cannot be null");
        client.delete()
                .guaranteed()
                .deletingChildrenIfNeeded()//
                .forPath(path);
    }

    public String getStringValue(String path,String defaultValue){
        return propertyFactory
                .getStringProperty(path,defaultValue)//
                .get();
    }

    public int getIntValue(String path,int defaultValue){
        return propertyFactory
                .getIntProperty(path,defaultValue)
                .get();
    }

    public long getLongValue(String path,long defaultValue){
        return propertyFactory
                .getLongProperty(path,defaultValue)
                .get();
    }

    public boolean getBooleanValue(String path,boolean defaultValue){
        return propertyFactory
                .getBooleanProperty(path,defaultValue)//
                .get();
    }

    public float getFloatValue(String path,float defaultValue){
        return propertyFactory
                .getFloatProperty(path,defaultValue)
                .get();
    }

    public double getDoubleValue(String path,double defaultValue){
        return propertyFactory
                .getDoubleProperty(path,defaultValue)
                .get();
    }
    
    public void addPathChildrenCacheListener(PathChildrenCacheListener listener,String path) throws Exception {
        Preconditions.checkNotNull(path,"path cannot be null");
        PathChildrenCache childrenCache = new PathChildrenCache(client,path,true);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        childrenCache
                .getListenable()
                .addListener(listener);
    }

    public void addPathChildrenCacheListener(PathChildrenCacheListener listener,String path,Executor executor) throws Exception {
        Preconditions.checkNotNull(path,"path cannot be null");
        PathChildrenCache childrenCache = new PathChildrenCache(client,path,true);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        childrenCache
                .getListenable()
                .addListener(listener, executor);
    }
}
