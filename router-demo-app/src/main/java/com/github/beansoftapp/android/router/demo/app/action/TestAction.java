package com.github.beansoftapp.android.router.demo.app.action;

import com.github.beansoftapp.android.router.action.HAbstractAction;
import com.github.beansoftapp.android.router.action.HCallback;
import com.github.beansoftapp.android.router.annotation.Action;

import static android.R.attr.action;

/**
 * 测试的动作.
 * Created by beansoft on 16/12/20.
 */
@Action(value = {"action/test"})
public class TestAction extends HAbstractAction<String>  {
    public String action() {// 无参数的调用应该只考虑这一个
//        return action((Object)null);
        return "TestAction";
    }

//    public String action(Object param) {
//        return "Action:" + param;
//    }

    public void action(HCallback<String> callback) {
        action(null, callback);
    }

    public void action(Object param, HCallback<String> callback) {
        callback.start();
        callback.ok(action(param), null);
        callback.complete();
    }
}
