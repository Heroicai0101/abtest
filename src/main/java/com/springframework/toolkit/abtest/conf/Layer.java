package com.springframework.toolkit.abtest.conf;

import lombok.NonNull;
import lombok.Value;

/**
 * 实验层次定义
 *
 * @author guoxiong
 * 2019/11/3 上午10:47
 */
@Value
public class Layer {

    /** 层标识 */
    @NonNull
    private String id;

    /** [非必须] 扩展数据 */
    private String data;

}
