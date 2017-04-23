package com.github.beansoftapp.android.router.interceptor;

/**
 * 调用器.
 */
public interface Invoker {
    /**
     * 执行跳转逻辑, context 可以是 Context, Activity或者Fragment
     * @param context
     */
    void invoke(Object context);
}
