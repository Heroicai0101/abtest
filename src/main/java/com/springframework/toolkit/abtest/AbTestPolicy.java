package com.springframework.toolkit.abtest;

import com.springframework.toolkit.abtest.conf.Layer;
import com.springframework.toolkit.abtest.div.DivMethod;
import com.springframework.toolkit.abtest.div.DivResult;
import com.springframework.toolkit.abtest.gray.GrayPoint;

import java.util.function.Consumer;

/**
 * AB实验接口规范
 *
 * @author guoxiong
 * 2019/11/2 上午9:10
 */
public interface AbTestPolicy {

    /**
     * 层标识
     */
    default Layer layer() {
        return new Layer("None", "");
    }

    /**
     * 按灰度分层
     */
    AbTestPolicy hitGray(GrayPoint grayPoint);

    /**
     * 分流
     */
    DivResult hitDiv(DivMethod divMethod);

    /**
     * 供系统调试用
     */
    default AbTestPolicy peek(Consumer<AbTestPolicy> action) {
        action.accept(this);
        return this;
    }

    AbTestPolicy NULL = new AbTestPolicy() {

        @Override
        public AbTestPolicy hitGray(GrayPoint grayPoint) {
            return NULL;
        }

        @Override
        public DivResult hitDiv(DivMethod divMethod) {
            return new DivResult(false, layer().getId(), layer().getData());
        }

        @Override
        public String toString() {
            return "NullableAbTestPolicy{}";
        }
    };

}
