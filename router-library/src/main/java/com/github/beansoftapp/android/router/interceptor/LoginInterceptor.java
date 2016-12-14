package com.github.beansoftapp.android.router.interceptor;

import android.content.Context;
import android.text.TextUtils;
import com.github.beansoftapp.android.router.HRouter;

/**
 * 判断用户是否已登陆, 需要App开发者自己定制.
 */
public class LoginInterceptor extends AbstractInterceptor {
    private static final String TARGET_LOGIN = "app://client/user/login";
    private boolean isLogined;
    private boolean needLogin;

    public LoginInterceptor(Context context, boolean z) {
        super(context);
        this.needLogin = z;
        this.isLogined = isLoginned(context);
    }

    public boolean login() {
        if (this.needLogin) {
            return this.isLogined;
        }
        return true;
    }

    public Class<?> getBridgeClass() {
        return HRouter.getActivityName(TARGET_LOGIN);
    }

    public static boolean isLoginned(Context context) {
        if (TextUtils.isEmpty(context.getSharedPreferences("PreferenceUtils", 0).getString("logined_user_id", null))) {
            return false;
        }
        return true;
    }
}
