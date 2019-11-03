package com.springframework.toolkit.abtest.gray;

/**
 * 灰度切入点
 */
public interface GrayPoint {

    /**
     * 灰度规则名称, eg: "uid"、"city"
     */
    String getName();

    /**
     * 灰度规则待校验是否匹配的值
     */
    String getValue();

}