package com.springframework.toolkit.abtest.div;

/**
 * 分流方式
 *
 * @author guoxiong
 * 2019/11/2 上午10:23
 */
public interface DivMethod {

    /**
     * 计算分流指标: 合法值应落在[0,100)区间
     */
    int calcIndicator();

}
