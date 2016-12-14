package com.github.beansoftapp.android.router.action;

/**
 * 抽象的回调接口, TODO: 和Retrofit保持一致?
 */
public interface HCallback {
    void onComplete();

    void onFailure(HAction hBAction, Throwable exception);

    void onResponse(HAction hBAction, Object response);
}
