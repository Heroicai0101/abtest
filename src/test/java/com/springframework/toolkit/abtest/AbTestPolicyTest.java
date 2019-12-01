package com.springframework.toolkit.abtest;

import com.springframework.toolkit.abtest.div.DivMethods;
import com.springframework.toolkit.abtest.div.DivResult;
import com.springframework.toolkit.abtest.gray.GrayPoints;
import org.junit.Assert;
import org.junit.Test;

public class AbTestPolicyTest {

    @Test
    public void test_hit_layer1() throws Exception {
        String conf = "[{\"layer\":{\"id\":\"layer1\",\"data\":\"something1\"},\"grayRules\":[{\"name\":\"source\"," +
                "\"enabled\":true,\"include\":[\"2\",\"21\"],\"exclude\":[],\"global\":false},{\"name\":\"city\"," +
                "\"enabled\":true,\"include\":[\"1\",\"5\"],\"exclude\":[],\"global\":false}]," +
                "\"divRule\":{\"percent\":100}},{\"layer\":{\"id\":\"layer2\",\"data\":\"something2\"}," +
                "\"grayRules\":[{\"name\":\"source\",\"enabled\":true,\"include\":[\"1\"],\"exclude\":[]," +
                "\"global\":false},{\"name\":\"city\",\"enabled\":true,\"include\":[\"3\"],\"exclude\":[]," +
                "\"global\":false}],\"divRule\":{\"percent\":100}}]";


        AbTestFactory.AbTestFacade facade = AbTestFactory.build(conf);
        DivResult result = facade.hitGray(GrayPoints.source("2"))
                                 .peek(System.out::println)
                                 .hitGray(GrayPoints.city("1"))
                                 .peek(System.out::println)
                                 .hitDiv(DivMethods.global());

        Assert.assertTrue("layer1".equals(result.getLayerId()));
    }

    @Test
    public void test_hit_layer2() throws Exception {
        String conf = "[{\"layer\":{\"id\":\"layer1\",\"data\":\"something1\"},\"grayRules\":[{\"name\":\"source\"," +
                "\"enabled\":true,\"include\":[\"2\",\"21\"],\"exclude\":[],\"global\":false},{\"name\":\"city\"," +
                "\"enabled\":true,\"include\":[\"1\",\"5\"],\"exclude\":[],\"global\":false}]," +
                "\"divRule\":{\"percent\":100}},{\"layer\":{\"id\":\"layer2\",\"data\":\"something2\"}," +
                "\"grayRules\":[{\"name\":\"source\",\"enabled\":true,\"include\":[\"1\"],\"exclude\":[]," +
                "\"global\":false},{\"name\":\"city\",\"enabled\":true,\"include\":[\"3\"],\"exclude\":[]," +
                "\"global\":false}],\"divRule\":{\"percent\":100}}]";

        AbTestFactory.AbTestFacade facade = AbTestFactory.build(conf);
        DivResult result = facade.hitGray(GrayPoints.source("1"))
                                 .peek(System.out::println)
                                 .hitGray(GrayPoints.city("3"))
                                 .peek(System.out::println)
                                 .hitDiv(DivMethods.global());

        Assert.assertTrue("layer2".equals(result.getLayerId()));
    }


    @Test
    public void test_mis_layer() throws Exception {
        String conf = "[{\"layer\":{\"id\":\"layer1\",\"data\":\"something1\"},\"grayRules\":[{\"name\":\"source\"," +
                "\"enabled\":true,\"include\":[\"2\",\"21\"],\"exclude\":[],\"global\":false},{\"name\":\"city\"," +
                "\"enabled\":true,\"include\":[\"1\",\"5\"],\"exclude\":[],\"global\":false}]," +
                "\"divRule\":{\"percent\":100}},{\"layer\":{\"id\":\"layer2\",\"data\":\"something2\"}," +
                "\"grayRules\":[{\"name\":\"source\",\"enabled\":true,\"include\":[\"1\"],\"exclude\":[]," +
                "\"global\":false},{\"name\":\"city\",\"enabled\":true,\"include\":[\"3\"],\"exclude\":[]," +
                "\"global\":false}],\"divRule\":{\"percent\":100}}]";

        AbTestFactory.AbTestFacade facade = AbTestFactory.build(conf);
        DivResult result = facade.hitGray(GrayPoints.source("2"))
                                 .peek(System.out::println)
                                 .hitGray(GrayPoints.city("3"))
                                 .peek(System.out::println)
                                 .hitDiv(DivMethods.global());

        Assert.assertTrue(!result.isHit());
    }

    @Test
    public void test_performance() throws Exception {
        String conf = "[{\"layer\":{\"id\":\"layer1\",\"data\":\"something1\"},\"grayRules\":[{\"name\":\"source\"," +
                "\"enabled\":true,\"include\":[\"2\",\"21\"],\"exclude\":[],\"global\":false},{\"name\":\"city\"," +
                "\"enabled\":true,\"include\":[\"1\",\"5\"],\"exclude\":[],\"global\":false}]," +
                "\"divRule\":{\"percent\":100}},{\"layer\":{\"id\":\"layer2\",\"data\":\"something2\"}," +
                "\"grayRules\":[{\"name\":\"source\",\"enabled\":true,\"include\":[\"1\"],\"exclude\":[]," +
                "\"global\":false},{\"name\":\"city\",\"enabled\":true,\"include\":[\"3\"],\"exclude\":[]," +
                "\"global\":false}],\"divRule\":{\"percent\":10}}]";

        long startTime = System.currentTimeMillis();

        int hitCount = 0;
        int loop = 100000;
        for (int i = 0; i < loop; i++) {
            AbTestFactory.AbTestFacade policy = AbTestFactory.build(conf);
            DivResult result = policy.hitGray(GrayPoints.source("1"))
                                     .hitGray(GrayPoints.city("3"))
                                     .hitDiv(DivMethods.mod(i));

            if (result.isHit()) {
                hitCount += 1;
            }
        }
        long cost = System.currentTimeMillis() - startTime;
        System.out.println(String.format("loop=%s, hitCount=%s, costMs=%s", loop, hitCount, cost));
        Assert.assertTrue(hitCount <= (loop * 0.1));
    }

}