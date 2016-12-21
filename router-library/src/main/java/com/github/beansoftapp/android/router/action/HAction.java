package com.github.beansoftapp.android.router.action;

import java.util.Map;

/**
 * 动作接口
 *
 * @param <T> 所返回的值对象类型
 * @see HActionExecutor#handleParams(HAction, Map, HCallback)
 */
public interface HAction<T> {
    /**
     * 同步调用, 无参数, 调用例子: haction://actionxx
     */
    T action();

    /**
     * 同步调用, 参数 params 可为空, 调用例子: haction://actionxx?key=value&key1=value1
     * 如果子类未定义此方法, 则转而调用 action() 方法.
     */
    T action(Object param);

    /**
     * 异步调用, 有参数, 有回调. 调用例子: haction://actionxx?key=value&key1=value1
     *
     * @param callback 回调接口
     */
    void action(HCallback<T> callback);

    /**
     * 异步调用, 有参数, 有回调. 调用例子: haction://actionxx?key=value&key1=value1
     * 如果子类未定义此方法, 则转而调用 action(HCallback) 方法.
     *
     * @param callback 回调接口
     */
    void action(Object param, HCallback<T> callback);
}