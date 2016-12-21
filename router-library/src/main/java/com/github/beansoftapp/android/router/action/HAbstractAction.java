package com.github.beansoftapp.android.router.action;

import com.github.beansoftapp.android.router.util.JsonUtil;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import static android.R.attr.action;

/**
 * 抽象的动作类实现.
 * 子类:
 * public class AppCreateAction extends AbstractAction<Void> {
 *
 * }
 * @param <T>
 */
public abstract class HAbstractAction<T> implements HAction<T> {

    public T action() {
        return null;
    }

    public T action(Object param) {
        return null;
    }

    public void action(HCallback<T> callback) {
    }

    public void action(Object param, HCallback<T> callback) {
    }
}