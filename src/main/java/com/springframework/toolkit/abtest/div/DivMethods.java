package com.springframework.toolkit.abtest.div;

import java.util.Objects;
import java.util.Random;

/**
 * 分流方式工厂类
 */
public abstract class DivMethods {

    public static DivMethod global() {
        return GlobalDiv.INSTANCE;
    }

    public static DivMethod mod(long input) {
        return new Mod100Div(input);
    }

    public static DivMethod hashMod(Object input) {
        return new HashMod100Div(input);
    }

    public static DivMethod randomMod(Object input) {
        return new RandomMod100Div(input);
    }

    private final static class GlobalDiv implements DivMethod {

        private static final DivMethod INSTANCE = new GlobalDiv();

        @Override
        public int calcIndicator() {
            return 0;
        }

    }

    /**
     * 直接模除100：注意输入是数值型
     */
    private final static class Mod100Div implements DivMethod {

        private final long input;

        private Mod100Div(Long input) {
            this.input = input;
        }

        @Override
        public int calcIndicator() {
            return (int) Math.abs(input) % 100;
        }
    }

    /**
     * 哈希模除100
     */
    private final static class HashMod100Div implements DivMethod {

        private final Object input;

        private HashMod100Div(Object input) {
            this.input = input;
        }

        @Override
        public int calcIndicator() {
            int indicator = Objects.hashCode(input);
            return Math.abs(indicator) % 100;
        }
    }

    /**
     * 随机模除100
     */
    private final static class RandomMod100Div implements DivMethod {

        private final Object input;

        private RandomMod100Div(Object input) {
            this.input = input;
        }

        @Override
        public int calcIndicator() {
            // hash(input) as a random seed
            int indicator = new Random(Objects.hashCode(input)).nextInt();
            return Math.abs(indicator) % 100;
        }
    }

}