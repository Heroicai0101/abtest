package com.springframework.toolkit.abtest.conf;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 灰度规则测试
 *
 * @author guoxiong
 * 2019/11/3 下午4:39
 */
public class GrayRuleTest {

    @Test
    public void hitRule() throws Exception {
        GrayRule grayRule = new GrayRule("city");
        grayRule.setEnabled(true);

        List<String> exclude = new ArrayList<>();
        exclude.add("1");
        exclude.add("2");
        exclude.add("3");
        grayRule.setExclude(exclude);

        List<String> include = new ArrayList<>();
        include.add("1");
        include.add("4");
        include.add("5");
        grayRule.setInclude(include);

        grayRule.setGlobal(false);

        // 黑名单
        Assert.assertTrue(!grayRule.hitRule("2"));

        // 白名单
        Assert.assertTrue(grayRule.hitRule("4"));

        // 黑白名单都有, 黑名单优先
        Assert.assertTrue(!grayRule.hitRule("1"));

        // 不在黑白名单里面，且未全量
        Assert.assertTrue(!grayRule.hitRule("7"));
    }

    @Test
    public void hitRule_global() throws Exception {
        GrayRule grayRule = new GrayRule("city");
        grayRule.setEnabled(true);

        List<String> exclude = new ArrayList<>();
        exclude.add("1");
        exclude.add("2");
        exclude.add("3");
        grayRule.setExclude(exclude);

        List<String> include = new ArrayList<>();
        include.add("1");
        include.add("4");
        include.add("5");
        grayRule.setInclude(include);

        grayRule.setGlobal(true);

        // 已全量, 且不在黑白名单
        Assert.assertTrue(grayRule.hitRule("7"));

        // 已全量, 但在黑白名单
        Assert.assertTrue(!grayRule.hitRule("1"));
    }

}