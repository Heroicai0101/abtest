package com.springframework.toolkit.abtest.div;

import com.springframework.toolkit.abtest.conf.DivRule;
import org.junit.Assert;
import org.junit.Test;

public class DivMethodsTest {

    @Test
    public void global() throws Exception {
        // 分流比例为0%
        DivRule zero = new DivRule(0);
        DivMethod global = DivMethods.global();

        Assert.assertTrue(!zero.hitRule(global.calcIndicator()));

        // 分流比例为1%
        DivRule one = new DivRule(1);
        Assert.assertTrue(one.hitRule(global.calcIndicator()));
    }

    @Test
    public void mod() throws Exception {
        long input = -1214;
        DivMethod modMethod = DivMethods.mod(input);

        DivRule ten = new DivRule(10);
        Assert.assertTrue(!ten.hitRule(modMethod.calcIndicator()));

        DivRule twenty = new DivRule(20);
        Assert.assertTrue(twenty.hitRule(modMethod.calcIndicator()));
    }

    @Test
    public void hashMod() throws Exception {
        int loop = 10000;
        int count = 0;
        for (int i = 0; i < loop; i++) {
            String uid = "abc" + i;
            DivMethod modMethod = DivMethods.hashMod(uid);
            int indicator = modMethod.calcIndicator();
            if (indicator >= 0 && indicator < 100) {
                count++;
            }
        }
        Assert.assertTrue(count == loop);
    }

    @Test
    public void randomMod() throws Exception {
        int loop = 10000;
        int count = 0;
        for (int i = 0; i < loop; i++) {
            String uid = "abc" + i;
            DivMethod modMethod = DivMethods.randomMod(uid);
            int indicator = modMethod.calcIndicator();
            if (indicator >= 0 && indicator < 100) {
                count++;
            }
        }
        Assert.assertTrue(count == loop);
    }

}