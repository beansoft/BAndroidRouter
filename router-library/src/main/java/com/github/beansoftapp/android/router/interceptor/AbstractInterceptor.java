package com.github.beansoftapp.android.router.interceptor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.github.beansoftapp.android.router.util.ContextCast;

public abstract class AbstractInterceptor implements Interceptor {
    protected Object context;// 支持Fragment
    protected Context realContext;
    protected boolean needLogin;

    /**
     * 获取登录的具体Activity名称.
     * @return
     */
    public abstract Class<?> getBridgeClass();

    /**
     * 是否需要登录
     * @return 返回 false 时需要登陆.
     */
    public abstract boolean login();

    /** 设置登录状态 */
    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    public AbstractInterceptor(Object context) {
        setContext(context);
    }

    public void setContext(Object context) {
        this.context = context;
        this.realContext = ContextCast.getContext(context);
    }

    public void openTarget(Class<?> cls, Bundle bundle) {
        openTarget(cls, bundle, -1);
    }

    /**
     * 执行打开目标的动作
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void openTarget(Class<?> cls, Bundle bundle, int requestCode) {
        openTarget(cls, bundle, getLoginIntent(), requestCode);
    }

    // 获取登录时应该打开的界面
    public Intent getLoginIntent() {
        Intent intent = null;
        if (getBridgeClass() != null) {
            intent = new Intent(ContextCast.getContext(this.context), getBridgeClass());
        }

        return intent;
    }

    /**
     * 打开目标Activity.
     *
     * @param cls
     * @param bundle
     * @param loginIntent
     */
    public void openTarget(Class<?> cls, Bundle bundle, Intent loginIntent) {
        openTarget(cls, bundle, loginIntent, -1);
    }

    /**
     * 打开目标Activity.
     *
     * @param cls
     * @param bundle
     * @param loginIntent
     */
    public void openTarget(Class<?> cls, Bundle bundle, Intent loginIntent, int requestCode) {
        String name = cls.getName();
        if (TextUtils.isEmpty(name)) {
            throw new RuntimeException("Target Activity is Null, Please Contact Business Ower");
        }
        JumpInvoker jumpInvoker = new JumpInvoker(name, bundle);
        if (login() || loginIntent == null) {// 登录页面为空时也不能进行跳转
            jumpInvoker.setRequestCode(requestCode);
            jumpInvoker.invoke(this.context);
            return;
        }
        Toast.makeText(realContext, "请先登录", Toast.LENGTH_SHORT).show();
        loginIntent.putExtra(Interceptor.INVOKER, jumpInvoker);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.realContext.startActivity(loginIntent);
    }
}