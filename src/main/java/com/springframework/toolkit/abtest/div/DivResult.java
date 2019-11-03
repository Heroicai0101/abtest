package com.springframework.toolkit.abtest.div;

import lombok.Value;

/**
 * 分层、分流后的最终返回值
 */
@Value
public class DivResult {

    /** 是否命中分层、分流规则 */
    private boolean hit;

    private String layerId;

    private String layerData;

}
