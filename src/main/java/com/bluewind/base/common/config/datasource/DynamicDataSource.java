package com.bluewind.base.common.config.datasource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author liuxingyu01
 * @date 2021-09-15-11:38
 * @description 动态数据源（数据源切换）
 * 使用方法 1、在方法上加上注解@DataSource("slaveDataSource")
 *         2、代码中手动切换 DynamicDataSource.setDataSource("pickDataSource");//设置数据源
 *            使用完成后记得清除数据源 DynamicDataSource.clearDataSource(); //重置数据源
 **/
public class DynamicDataSource extends AbstractRoutingDataSource {
    private final static Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSource = getDataSource();
        if (StringUtils.isNotBlank(dataSource)) {
            logger.info("当前数据源已切换至:" + dataSource);
        }
        return dataSource;
    }

    /**
     * 设置数据源
     *
     * @param dataSource
     */
    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public static String getDataSource() {
        String dataSource = contextHolder.get();
        return dataSource;
    }

    /**
     * 清除数据源
     */
    public static void clearDataSource() {
        if (logger.isInfoEnabled()) {
            logger.info("已清空数据源设置");
        }
        contextHolder.remove();
    }

}
