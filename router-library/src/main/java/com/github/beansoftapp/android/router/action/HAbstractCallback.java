package com.github.beansoftapp.android.router.action;

/**
 * 抽象的回调类.
 * Created by beansoft on 16/12/19.
 */

public abstract class HAbstractCallback<T> implements HCallback<T> {
    @Override
    public void start() {
    }

    @Override
    public void complete() {

    }

    @Override
    public void failure(Throwable exception) {
        complete();
    }

    @Override
    public void ok(T t, Object response) {
        complete();
    }
}
