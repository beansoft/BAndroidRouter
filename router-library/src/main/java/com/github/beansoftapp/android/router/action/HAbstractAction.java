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

    // 禁止子类重载此行为
    public final Object handleParams(Map<String, String> map, HCallback<T> callback) {
        if (map != null && map.size() > 0) {// Map 不为空
            if (callback == null) {// 同步调用
                try {
                    // 检查当前类是否处理此方法 TODO Action 继承关系怎么处理?
                    Method m = getClass().getDeclaredMethod("action", Object.class);
                    if(m!=null) {
                        return action(map);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    return action();
                }
            } else {
                callback.start();

                try {
                    // 检查当前类是否处理此方法 TODO Action 继承关系怎么处理?
                    Method m = getClass().getDeclaredMethod("action", HCallback.class);
                    if(m!=null) {
                        action(map, callback);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    action(callback);
                }
            }
            return null;
        } else if (callback == null) {
            return action();
        } else {
            callback.start();
            action(callback);
            return null;
        }
    }

}