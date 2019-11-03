package com.springframework.toolkit.abtest.gray;

import lombok.NonNull;

public abstract class GrayPoints {

    private static final String UID_RULE = "uid";

    private static final String PHONE_RULE = "phone";

    private static final String SOURCE_RULE = "source";

    private static final String CITY_RULE = "city";

    public static GrayPoint city(String value) {
        return new BasicGrayPoint(CITY_RULE, value);
    }

    public static GrayPoint source(String value) {
        return new BasicGrayPoint(SOURCE_RULE, value);
    }

    public static GrayPoint uid(String value) {
        return new BasicGrayPoint(UID_RULE, value);
    }

    public static GrayPoint phone(String value) {
        return new BasicGrayPoint(PHONE_RULE, value);
    }

    public static GrayPoint build(@NonNull String name, @NonNull String value) {
        return new BasicGrayPoint(name, value);
    }

    public static GrayPoint suffixGrayPoint(@NonNull String name, @NonNull String value) {
        return new SuffixGrayPoint(name, value);
    }

    private static class SuffixGrayPoint implements GrayPoint {

        private final String name;

        private final String value;

        private SuffixGrayPoint(String name, String sample) {
            this.name = name;
            this.value = sample;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getValue() {
            return value.substring(value.length() - 1, value.length());
        }
    }

}