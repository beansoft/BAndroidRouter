package com.github.beansoftapp.android.router.action;

import com.github.beansoftapp.android.router.util.JsonUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 抽象的动作类实现.
 * 子类:
 * public class AppCreateAction extends AbstractAction<Void> {
 *
 * }
 * @param <T>
 */
public abstract class HAbstractAction<T> implements HAction<T> {

    public Object action() {
        return null;
    }

    public Object action(T t) {
        return null;
    }

    public void action(HCallback hBCallback) {
    }

    public void action(T t, HCallback hBCallback) {
    }

    public Object handleParams(Map<String, String> map, HCallback hBCallback) {
        if (map != null && map.size() > 0) {
            Object fromJson = null;
            String toJson = JsonUtil.toJson(map);
            Type type = getType();
            if (type != String.class) {
                fromJson = JsonUtil.fromJson(toJson, type);
            }
            if (hBCallback == null) {
                return action((T)fromJson);
            } else {
                action((T)fromJson, hBCallback);
            }

            return null;
        } else if (hBCallback == null) {
            return action();
        } else {
            action(hBCallback);
            return null;
        }
    }

    private Type getType() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (!(genericSuperclass instanceof Class)) {
            return ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        }
        throw new RuntimeException("Missing type parameter.");
    }
}
