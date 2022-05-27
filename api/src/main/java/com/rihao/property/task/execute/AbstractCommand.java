package com.rihao.property.task.execute;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 执行命令
 * @param <T>
 */
public abstract class AbstractCommand<T> {

    private T json2Obj(String jsonParams) {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return JSON.parseObject(jsonParams, actualTypeArguments[0]);
    }

    abstract boolean execute(T param);

    public boolean execute(String param) {
        return this.execute(json2Obj(param));
    }

}
