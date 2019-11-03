package com.springframework.toolkit.abtest.conf;

import lombok.Value;

/**
 * 分流规则
 */
@Value
public class DivRule {

    /** 分流比例 */
    private int percent;

    public boolean hitRule(int indicator) {
        if (indicator < 0 || indicator >= 100) {
            throw new IllegalStateException("Indicator should be [0,100)");
        }
        return indicator < percent;
    }

}