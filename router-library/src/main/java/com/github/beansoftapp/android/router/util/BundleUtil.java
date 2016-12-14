package com.github.beansoftapp.android.router.util;

import android.os.Bundle;

/**
 * Bundle 调试工具
 * Created by beansoft on 16/12/14.
 */

public class BundleUtil {
    public static String bundle2string(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }
}
