package com.github.beansoftapp.android.router.util;

import android.app.Fragment;
import android.content.Context;

/**
 * Context cast util.
 * Created by beansoft on 17/4/21.
 */

public class ContextCast {
    /**
     * 获得真正的Context
     * @param context 可以是 Context, Activity或者Fragment
     * @return
     */
    public static final Context getContext(Object context) {
        if(context == null) return null;

        Context realContext = null;
        if (context instanceof Context) {
            realContext = (Context)context;
        } else if (context instanceof Fragment) {
            realContext = ((Fragment)context).getActivity();
        }

        return realContext;
    }
}
