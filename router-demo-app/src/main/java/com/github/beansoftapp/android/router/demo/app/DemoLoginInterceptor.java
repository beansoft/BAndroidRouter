package com.github.beansoftapp.android.router.demo.app;

import android.content.Context;

import com.github.beansoftapp.android.router.HRouter;
import com.github.beansoftapp.android.router.interceptor.AbstractInterceptor;

/**
 * 判断用户是否已登陆, 需要App开发者自己定制.
 */
public class DemoLoginInterceptor extends AbstractInterceptor {
    private static final String TARGET_LOGIN = "app://user/login";
    private boolean isLogined;

    public DemoLoginInterceptor(Context context) {
        super(context);
    }

    public boolean login() {
        this.isLogined = isLoginned(context);
        if (this.needLogin) {
            return this.isLogined;
        }
        return true;
    }

    public Class<?> getBridgeClass() {
        return HRouter.getActivityName(TARGET_LOGIN);
    }

    public static boolean isLoginned(Context context) {
        return true;
    }
}
