package com.springframework.toolkit.abtest.conf;

import lombok.Data;

import java.util.List;

/**
 * AB实验配置类, eg:
 * <pre>
 *     [{"layer":{"id":"layer1","data":"something1"},"grayRules":[{"name":"source","enabled":true,"include":["2",
 *     "21"],"exclude":[],"global":false},{"name":"city","enabled":true,"include":["1","5"],"exclude":[],
 *     "global":false}],"divRule":{"percent":100}},{"layer":{"id":"layer2","data":"something2"},
 *     "grayRules":[{"name":"source","enabled":true,"include":["1"],"exclude":[],"global":false},{"name":"city",
 *     "enabled":true,"include":["3"],"exclude":[],"global":false}],"divRule":{"percent":100}}]
 * </pre>
 *
 * @author guoxiong
 * 2019/11/2 上午11:07
 */
@Data
public class AbTestConfig {

    /** 实验层次标识 */
    private Layer layer;

    /** 分层规则 */
    private List<GrayRule> grayRules;

    /** 分流规则 */
    private DivRule divRule;

}
