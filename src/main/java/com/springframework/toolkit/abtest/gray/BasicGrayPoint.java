package com.springframework.toolkit.abtest.gray;

import lombok.NonNull;
import lombok.Value;

@Value
public class BasicGrayPoint implements GrayPoint {

    @NonNull
    private String name;

    @NonNull
    private String value;

}
