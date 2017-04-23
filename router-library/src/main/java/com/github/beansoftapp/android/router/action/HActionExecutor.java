package com.github.beansoftapp.android.router.action;

import java.lang.reflect.Method;

/**
 * Action执行类. 简化Action接口本身的行为. 注意不支持混淆.
 * Created by beansoft on 16/12/21.
 */
public class HActionExecutor {

//    /**
//     * 处理参数并执行回调, 如果callback为空, 则执行同步调用. 此类被Router所调用, 开发者不要覆盖它的行为.    禁止子类重载此行为
//     *
//     * @param params   键值对, 例如最终会转化成Http请求参数.
//     * @param callback 异步回调
//     * @return 同步执行时会返回执行结果, 异步时返回空.
//     * @see com.github.beansoftapp.android.router.HRouter#action(String)
//     * @see com.github.beansoftapp.android.router.HRouter#action(String, HCallback)
//     */
//    public static final Object handleParams(HAction action, Map<String, String> params, HCallback callback) {
//        if (params != null && params.size() > 0) {// Map 不为空, 有参数
//            if (callback == null) {// 同步调用
//                try {
//                    // 检查当前类是否处理此方法 TODO Action 继承关系怎么处理?
//                    Method m = action.getClass().getDeclaredMethod("action", Object.class);
//                    if (m != null) {
//                        return action.action(params);
//                    }
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                    return action.action();
//                }
//            } else {// 异步调用, 有回调
//                callback.start();
//
//                try {
//                    // 检查当前类是否处理此方法 TODO Action 继承关系怎么处理?
//                    Method m = action.getClass().getDeclaredMethod("action", Object.class, HCallback.class);
//                    if (m != null) {
//                        action.action(params, callback);
//                    }
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                    action.action(callback);
//                }
//            }
//            return null;
//        }
//        else if (callback == null) {
//            return action.action();
//        } else {
//            callback.start();
//            action.action(callback);
//            return null;
//        }
//    }

    /**
     * 处理参数并执行回调, 如果callback为空, 则执行同步调用. 此类被Router所调用, 开发者不要覆盖它的行为.    禁止子类重载此行为
     *
     * @param params   直接传递的参数.
     * @param callback 异步回调
     * @return 同步执行时会返回执行结果, 异步时返回空.
     * @see com.github.beansoftapp.android.router.HRouter#action(String)
     * @see com.github.beansoftapp.android.router.HRouter#action(String, HCallback)
     */
    public static final Object handleParams(HAction action, Object params, HCallback callback) {
        if (params != null) {// 参数不为空, 有参数
            if (callback == null) {// 同步调用
                try {
                    // 检查当前类是否处理此方法 TODO Action 继承关系怎么处理?
                    Method m = action.getClass().getDeclaredMethod("action", Object.class);
                    if (m != null) {
                        return action.action(params);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    return action.action();
                }
            } else {// 异步调用, 有回调
                callback.start();

                try {
                    // 检查当前类是否处理此方法 TODO Action 继承关系怎么处理?
                    Method m = action.getClass().getDeclaredMethod("action",  Object.class, HCallback.class);
                    if (m != null) {
                        action.action(params, callback);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    action.action(callback);
                }
            }
            return null;
        }
        else if (callback == null) {
            return action.action();
        } else {
            callback.start();
            action.action(callback);
            return null;
        }
    }
}
