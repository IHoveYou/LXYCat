package com.tany.base.base;

import java.io.Serializable;

/**
 * 作者: Tany
 * 时间: 2016/8/9 10:05
 * 描述:实体类基类
 */
public class BaseBean<T> implements Serializable {
    public String msg;
    public int error;
    public T data;

    @Override
    public String toString() {
        return "BaseBean{" +
                "msg='" + msg + '\'' +
                ", error=" + error +
                ", data=" + data +
                '}';
    }
}
