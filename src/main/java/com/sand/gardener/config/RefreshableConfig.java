package com.sand.gardener.config;

/**
 * @author : huanghy
 * @create : 2016/3/1 0001 下午 4:01
 * @since : ${VERSION}
 */
public interface RefreshableConfig {

    /**
     * 从zkServer中读取初始化参数，
     * 生成需要托管给spring的对象，
     * 并且向spring容器注册
     */
    void registerBean();

    /**
     * 销毁注册在spring里的对象
     */
    void destroy();

    /**
     * 销毁并重新向spring注册
     */
    void refresh();
}
