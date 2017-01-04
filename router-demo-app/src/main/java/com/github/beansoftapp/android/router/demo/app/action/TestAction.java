package com.github.beansoftapp.android.router.demo.app.action;

import com.github.beansoftapp.android.router.action.HAbstractAction;
import com.github.beansoftapp.android.router.action.HCallback;
import com.github.beansoftapp.android.router.annotation.Action;

/**
 * 测试的动作.
 * 本类也可以直接实现 HAction<String> 接口.
 * Created by beansoft on 16/12/20.
 */
@Action("action/test")
public class TestAction extends HAbstractAction<String>  {
    // 同步模式
    public String action() {// 无参数的调用应该只考虑这一个
        return "TestAction同步调用无参数";
    }

    // 同步模式+参数,参数可直接传递
    public String action(Object param) {
        return "TestAction同步调用有参数:" + param;
    }

    // 异步模式+回调
    public void action(HCallback<String> callback) {
        action(null, callback);
    }

    // 异步模式+回调+参数
    public void action(Object param, HCallback<String> callback) {
        callback.start();
        callback.ok(action(param), null);
        callback.complete();
    }
}