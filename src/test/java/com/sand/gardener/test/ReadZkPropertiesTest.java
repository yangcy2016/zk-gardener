package com.sand.gardener.test;

import com.sand.gardener.dynamic.DynamicZkProperty;
import com.sand.gardener.dynamic.DynamicZkPropertyFactory;
import org.junit.Test;

/**
 * @author : huanghy
 * @create : 2016/3/1 0001 下午 2:06
 * @since : ${VERSION}
 */
public class ReadZkPropertiesTest {
    @Test
    public void test() throws Exception {
        DynamicZkProperty zkProperty = DynamicZkPropertyFactory.getDynamicZkProperty("/myapp/config");
        String poolName = zkProperty.getStringValue("hikaricp.poolName","");
        System.out.println(poolName);
        int maximumPoolSize = zkProperty.getIntValue("hikaricp.maximumPoolSize",100);
        System.out.println(maximumPoolSize);
        boolean autoCommit = zkProperty.getBooleanValue("hikaricp.autoCommit",false);
        System.out.println(autoCommit);
    }
}
