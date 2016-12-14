package com.github.beansoftapp.android.router.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class DeviceHelper {
    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "unknown";
        }
    }
}
