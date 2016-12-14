package com.github.beansoftapp.android.router.interceptor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

public abstract class AbstractInterceptor implements Interceptor {
    private Context context;

    public abstract Class<?> getBridgeClass();

    public abstract boolean login();

    public AbstractInterceptor(Context context) {
        this.context = context;
    }

    public void openTarget(Class<?> cls, Bundle bundle) {
        Intent intent = null;
        if (getBridgeClass() != null) {
            intent = new Intent(this.context, getBridgeClass());
        }
        openTarget(cls, bundle, intent);
    }

    /**
     * 打开目标Activity.
     *
     * @param cls
     * @param bundle
     * @param intent
     */
    public void openTarget(Class<?> cls, Bundle bundle, Intent intent) {
        String name = cls.getName();
        if (TextUtils.isEmpty(name)) {
            throw new RuntimeException("Target Activity is Null, Please Contact Business Ower");
        }
        JumpInvoker jumpInvoker = new JumpInvoker(name, bundle);
        if (login() || intent == null) {
            jumpInvoker.invoke(this.context);
            return;
        }
        Toast.makeText(this.context, "请先登录", Toast.LENGTH_SHORT).show();
        intent.putExtra(Interceptor.INVOKER, jumpInvoker);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.context.startActivity(intent);
    }
}