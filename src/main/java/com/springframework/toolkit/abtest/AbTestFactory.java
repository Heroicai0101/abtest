package com.springframework.toolkit.abtest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.springframework.toolkit.abtest.conf.AbTestConfig;
import com.springframework.toolkit.abtest.conf.DivRule;
import com.springframework.toolkit.abtest.conf.GrayRule;
import com.springframework.toolkit.abtest.conf.Layer;
import com.springframework.toolkit.abtest.div.DivMethod;
import com.springframework.toolkit.abtest.div.DivResult;
import com.springframework.toolkit.abtest.gray.GrayPoint;
import lombok.NonNull;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * AB实验工厂类
 *
 * @author guoxiong
 * 2019/11/2 上午11:02
 */
public abstract class AbTestFactory {

    private static final NullableAbTestPolicy NULLABLE_AB_TEST_POLICY = new NullableAbTestPolicy();

    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public static AbTestFacade build(@NonNull String abTestConfigs) {
        Type type = new TypeToken<List<AbTestConfig>>() {
        }.getType();
        List<AbTestConfig> configs = gson.fromJson(abTestConfigs, type);
        return build(configs);
    }

    public static AbTestFacade build(@NonNull List<AbTestConfig> abTestConfigs) {
        List<AbTestPolicy> policyList = abTestConfigs.stream()
                                                     .map(x -> build(x.getLayer(), x.getGrayRules(), x.getDivRule()))
                                                     .collect(Collectors.toList());
        return new AbTestFacade(policyList);
    }

    private static AbTestPolicy build(Layer layer, List<GrayRule> grayRules, DivRule divRule) {
        return new BasicAbTestPolicy(layer, grayRules, divRule);
    }

    /**
     * 组合策略类，当做入口类对外暴露
     */
    public static class AbTestFacade implements AbTestPolicy {

        private final List<AbTestPolicy> policyList;

        private AbTestFacade(@NonNull List<AbTestPolicy> policyList) {
            this.policyList = policyList;
        }

        /**
         * 分层
         */
        @Override
        public AbTestPolicy hitGray(GrayPoint grayPoint) {
            AbTestPolicy curPolicy = AbTestFactory.NULLABLE_AB_TEST_POLICY;
            for (AbTestPolicy policy : policyList) {
                curPolicy = policy.hitGray(grayPoint);
                if (curPolicy.getClass() == BasicAbTestPolicy.class) {
                    break;
                }
            }
            return curPolicy;
        }

        /**
         * 分流
         */
        @Override
        public DivResult hitDiv(DivMethod divMethod) {
            throw new UnsupportedOperationException("Facade not support 'hitDiv' method.");
        }

    }

    /**
     * 校验灰度规则和分流的实际执行类
     */
    private static class BasicAbTestPolicy implements AbTestPolicy {

        /** 层标识 */
        private final Layer layer;

        /** 灰度规则 */
        private final List<GrayRule> grayRules;

        /** 分流规则 */
        private final DivRule divRule;

        private BasicAbTestPolicy(@NonNull Layer layer, @NonNull List<GrayRule> grayRules, @NonNull DivRule divRule) {
            this.layer = layer;
            this.grayRules = grayRules;
            this.divRule = divRule;
        }

        @Override
        public Layer layer() {
            return layer;
        }

        @Override
        public AbTestPolicy hitGray(GrayPoint grayPoint) {
            Optional<GrayRule> optRule = grayRules.stream()
                                                  .filter(rule -> rule.isEnabled()
                                                          && rule.getName().equals(grayPoint.getName()))
                                                  .findFirst();
            if (!optRule.isPresent()) {
                return AbTestFactory.NULLABLE_AB_TEST_POLICY;
            }

            boolean hitRule = optRule.get().hitRule(grayPoint.getValue());
            if (!hitRule) {
                return AbTestFactory.NULLABLE_AB_TEST_POLICY;
            }

            // 返回自身,做到可链式调用
            return this;
        }

        @Override
        public DivResult hitDiv(DivMethod divMethod) {
            boolean b = divRule.hitRule(divMethod.calcIndicator());
            return new DivResult(b, layer.getId(), layer.getData());
        }

        @Override
        public String toString() {
            return "BasicAbTestPolicy{" +
                    "layer=" + layer +
                    ", grayRules=" + grayRules +
                    ", divRule=" + divRule +
                    '}';
        }

    }

    private static class NullableAbTestPolicy implements AbTestPolicy {

        @Override
        public AbTestPolicy hitGray(GrayPoint grayPoint) {
            return AbTestFactory.NULLABLE_AB_TEST_POLICY;
        }

        @Override
        public DivResult hitDiv(DivMethod divMethod) {
            return new DivResult(false, layer().getId(), layer().getData());
        }

        @Override
        public String toString() {
            return "NullableAbTestPolicy{}";
        }
    }

}