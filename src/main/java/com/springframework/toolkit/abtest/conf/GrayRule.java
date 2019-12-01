package com.springframework.toolkit.abtest.conf;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * 灰度规则规范
 * eg: {"name":"uid","enabled":true,"include":[],"exclude":[],"global":100}
 */
@Data
public class GrayRule {

    /** 规则名称, eg: "uid"、"city" */
    @NonNull
    private String name;

    /** 规则是否启用 */
    private boolean enabled;

    /** 黑名单 */
    private List<String> exclude;

    /** 白名单 */
    private List<String> include;

    /** 全量标识 */
    private boolean global;

    /**
     * 校验是否命中灰度规则
     * 1、先看是否在黑名单中，若在，则永远不会命中灰度，即使开全量也不行
     * 2、再看是否在白名单中，若在，则必定命中灰度
     * 3、若未命中黑、白名单，则看是否已全量
     */
    public boolean hitRule(String value) {
        if (exclude != null && exclude.size() > 0) {
            if (exclude.contains(value)) {
                return false;
            }
        }

        if (include != null && include.size() > 0) {
            if (include.contains(value)) {
                return true;
            }
        }
        return global;
    }

}