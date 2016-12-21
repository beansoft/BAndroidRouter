package com.github.beansoftapp.android.router.action;

import java.lang.reflect.Method;
import java.util.Map;

public class HActionExecutor {
    // 禁止子类重载此行为
    public static final Object handleParams(HAction action, Map<String, String> map, HCallback callback) {
        if(action == null) {return null;}
        if (map != null && map.size() > 0) {// Map 不为空
            if (callback == null) {// 同步调用
                try {
                    // 检查当前类是否处理此方法 TODO 继承关系怎么处理?
                    Method m = action.getClass().getDeclaredMethod("action", Object.class);
                    if(m!=null) {
                        return action.action(map);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    return action.action();
                }
            } else {
                callback.start();

                try {
                    // 检查当前类是否处理此方法 TODO 继承关系怎么处理?
                    Method m = action.getClass().getDeclaredMethod("action", HCallback.class);
                    if(m!=null) {
                        action.action(map, callback);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    action.action(callback);
                }
            }
            return null;
        } else if (callback == null) {
            return action.action();
        } else {
            callback.start();
            action.action(callback);
            return null;
        }
    }
}