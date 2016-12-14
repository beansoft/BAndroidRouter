package com.github.beansoftapp.android.router.action;

import java.util.Map;

/**
 * 动作接口
 * @param <T>
 */
public interface HAction<T> {
    /** 同步调用, 无参数 */
    Object action();
    /** 同步调用, 有参数 */
    Object action(T t);

    /**
     * 异步调用, 无参数, 有回调.
     *
     * @param hBCallback 回调接口
     */
    void action(HCallback hBCallback);
    /**
     * 异步调用, 有参数, 有回调.
     *
     * @param hBCallback 回调接口
     */
    void action(T t, HCallback hBCallback);

    /**
     * 处理参数并执行回调.
     * @param map
     * @param hBCallback
     * @return
     */
    Object handleParams(Map<String, String> map, HCallback hBCallback);
}
