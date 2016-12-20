package com.github.beansoftapp.android.router.action;

/**
 * 抽象的回调接口, TODO: 和Retrofit保持一致?
 */
public interface HCallback<T> {
    /** 开始回调 */
    void start();

    /** 结束回调, 放子抽象类更合适一些. */
    void complete();

    void failure(Throwable exception);

    /**
     * 返回成功
     * @param t
     * @param response 原始的数据, 如果是retrofit, 可能是 Response 对象.
     */
    void ok(T t, Object response);
}
