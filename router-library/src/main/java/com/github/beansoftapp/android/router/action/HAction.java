package com.github.beansoftapp.android.router.action;

import java.util.Map;


/**
 * 动作接口
 * @param <T> 所接受的参数类型
 */
public interface HAction<T> {
    /** 同步调用, 无参数, 调用例子: haction://actionxx */
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
     * @param callback 回调接口
     */
    void action(Object param, HCallback<T> callback);

    /**
     * 处理参数并执行回调, 如果callback为空, 则执行同步调用. 此类被Router所调用, 开发者不要覆盖它的行为.
     * @see HAbstractAction#handleParams(Map, HCallback)
     * @see com.github.beansoftapp.android.router.HRouter#action(String)
     * @param params 键值对, 例如最终会转化成Http请求参数.
     * @param callback 异步回调
     * @return 同步执行时会返回执行结果, 异步时返回空.
     */
    Object handleParams(Map<String, String> params, HCallback<T> callback);
}