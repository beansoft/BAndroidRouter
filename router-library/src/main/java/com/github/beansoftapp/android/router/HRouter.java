package com.github.beansoftapp.android.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.github.beansoftapp.android.router.action.HActionExecutor;
import com.github.beansoftapp.android.router.action.HAction;
import com.github.beansoftapp.android.router.action.HActionMapping;
import com.github.beansoftapp.android.router.action.HCallback;
import com.github.beansoftapp.android.router.interceptor.LoginInterceptor;
import com.github.beansoftapp.android.router.util.DeviceHelper;

import java.util.*;

import static android.R.attr.key;
import static android.content.pm.PackageManager.MATCH_DEFAULT_ONLY;

/**
 * 初始化:
 * HRouter.setup(new String[]{"Base", "Core"});
 * @see HRouterMappingBase 由APT编译生成的类
 * @see HRouterMappingCore
 */
public class HRouter {
    private static final String TAG = "HRouter";
    public static final String TARGET = "router_target";
    public static String URL_SCHEME = "router";// 协议头
    private static List<HActionMapping> actionMappings = new ArrayList();
    private static String[] mBundlesName;
    private static List<HMapping> mappings = new ArrayList();
    private static Map<String, List<String>> nameMappings = new HashMap();


    public static synchronized void setup(String... strArr) {
        synchronized (HRouter.class) {
            mBundlesName = strArr;
            initMappings();
            initActionMappings();
        }
    }

    /**
     * 设置Router接受的Schema.
     * @param str
     */
    public static void setScheme(String str) {
        URL_SCHEME = str;
    }

    private static void initNameMappings() {
        if (nameMappings.isEmpty()) {
            initMappings();
            for (HMapping hMapping : mappings) {
                String name = hMapping.getActivity().getName();
                String format = hMapping.getFormat();
                if (format != null) {
                    List list = (List) nameMappings.get(name);
                    if (list == null || list.size() == 0) {
                        list = new ArrayList();
                        list.add(format);
                        nameMappings.put(name, list);
                    } else {
                        list.add(format);
                        nameMappings.put(name, list);
                    }
                }
            }
        }
    }

    private static void initActionMappings() {
        long currentTimeMillis = System.currentTimeMillis();
        if (actionMappings.isEmpty() && mBundlesName != null) {
            for (String str : mBundlesName) {
                try {
                    Class.forName("com.github.beansoftapp.android.router.HRouterMapping" + str).getMethod("mapAction", new Class[0]).invoke(null, new Object[0]);
                } catch (Throwable e) {
                    e.printStackTrace();
                    Log.e(TAG, "Cannt fond com.github.beansoftapp.android.router.HRouterMapping" + str, e);
                }
            }
            Log.d(TAG, "Routers HAction cost " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        }
    }

    private static void initMappings() {
        long currentTimeMillis = System.currentTimeMillis();
        if (mappings.isEmpty() && mBundlesName != null) {
            for (String str : mBundlesName) {
                try {
                    Class.forName("com.github.beansoftapp.android.router.HRouterMapping" + str).getMethod("map", new Class[0]).invoke(null, new Object[0]);
                } catch (Throwable e) {
                    e.printStackTrace();
                    Log.e(TAG, "Cannt fond com.github.beansoftapp.android.router.HRouterMapping" + str, e);
                }
            }
            Log.d(TAG, "Routers cost " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        }
    }

    public static void mapAction(String str, Class<? extends HAction> cls) {
        actionMappings.add(new HActionMapping(str, cls));
    }

    public static void map(String str, Class<? extends Activity> cls) {
        map(str, cls, false);
    }

    public static void map(String str, Class<? extends Activity> cls, boolean needLogin) {
        map(str, cls, needLogin, "4.2.0");
    }

    public static void map(String str, Class<? extends Activity> cls, boolean needLogin, String version) {
        map(str, cls, needLogin, version, true);
    }

    public static void map(String str, Class<? extends Activity> cls, boolean needLogin, String version, boolean z2) {
        map(str, cls, needLogin, version, true, BuildConfig.VERSION_NAME, BuildConfig.VERSION_NAME);
    }

    public static void map(String str, Class<? extends Activity> cls,
                           boolean needLogin, String version, boolean isPublic, String preExecute, String postExecute) {
        mappings.add(new HMapping(str, cls, needLogin, version, isPublic, preExecute, postExecute));
    }

    public static void sort() {
        Collections.sort(mappings, new Comparator<HMapping>() {

            public int compare(HMapping lhs, HMapping rhs) {
                return lhs.getFormat().compareTo(rhs.getFormat()) * -1;
            }
        });
    }

    /**
     * 执行 HAction 动作
     * @param str
     * @return
     */
    public static Object action(String str) {
        try {
            initActionMappings();
            Uri parse = Uri.parse(str);
            HPath create = HPath.create(parse);
            for (HActionMapping hActionMapping : actionMappings) {
                if (hActionMapping.match(create)) {
                    Log.i(TAG, "Hit HAction命中路由表: " + hActionMapping.toString());
                    return HActionExecutor.handleParams(hActionMapping.getAction().newInstance(), hActionMapping.parseExtras(parse), null );
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 执行 HAction 动作
     * @param str
     * @param callback, 带泛型的方法
     * @return
     */
    public static <T> void action(String str, HCallback<T> callback) {
        try {
            initActionMappings();
            Uri parse = Uri.parse(str);
            HPath create = HPath.create(parse);
            for (HActionMapping hActionMapping : actionMappings) {
                if (hActionMapping.match(create)) {
                    Log.i(TAG, "Hit HAction命中路由表: " + hActionMapping.toString());
                    HActionExecutor.handleParams(hActionMapping.getAction().newInstance(), hActionMapping.parseExtras(parse), callback );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到给定路径.
     * @param context
     * @param str
     * @return
     */
    public static boolean open(Context context, String str) {
        return open(context, Uri.parse(str));
    }

    /**
     * 带额外Bundle参数的跳转
     * @param context
     * @param str
     * @param bundle
     * @return
     */
    public static boolean open(Context context, String str, Bundle bundle) {
        return open(context, Uri.parse(str), bundle);
    }

    public static boolean open(Context context, Uri uri) {
        return open(context, uri, null);
    }

    public static boolean open(Context context, Uri uri, Bundle bundle) {
        try {
            initMappings();
            HPath create = HPath.create(uri);
            if (checkOpenApp(context, create)) {
                return true;
            }
            for (HMapping hMapping : mappings) {
                if (hMapping.match(create)) {
                    Bundle pareseBundle;
                    Log.i(TAG, "Hit 命中路由表: " + hMapping.toString());
                    Bundle parseExtras = hMapping.parseExtras(create.getUri());
                    parseExtras.putString(TARGET, hMapping.getFormat());
                    if (bundle != null) {
                        pareseBundle = hMapping.pareseBundle(bundle);
                        pareseBundle.putAll(parseExtras);
                    } else {
                        pareseBundle = parseExtras;
                    }
                    if (!checkVersion(context, pareseBundle.getString("ver", BuildConfig.VERSION_NAME))) {
                        return true;
                    }
                    if (TextUtils.isEmpty(hMapping.getPreExecute())) {
                        new LoginInterceptor(context, hMapping.isNeedLogin()).openTarget(hMapping.getActivity(), pareseBundle);
                        return true;
                    }
                    action(hMapping.getPreExecute());
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查 App 是否已经运行, 如果没有载入完毕则返回, 否则先启动App的默认View入口:
     * <action android:name="android.intent.action.VIEW"/>
     * @param context
     * @param hPath
     * @return
     */
    private static boolean checkOpenApp(Context context, HPath hPath) {
        try {
            String uri = hPath.getUri().toString();
            if (TextUtils.isEmpty(uri) || uri.startsWith(URL_SCHEME) || uri.startsWith("http") ||
                    uri.startsWith("https") || uri.startsWith("about")) {
                return false;
            }
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(hPath.getUri());
            if (context.getPackageManager().resolveActivity(intent, MATCH_DEFAULT_ONLY) == null) {
                return false;
            }
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 检查版本更新, 需要开发者自行实现
    private static boolean checkVersion(Context context, String str) {
        if (TextUtils.isEmpty(str) || DeviceHelper.getAppVersion(context).compareTo(str) >= 0) {
            return true;
        }
        action("haction://action/show_update");
        return false;
    }

    /**
     * 获取目标类所对应的路径列表.
     * @param obj
     * @return
     */
    public static List<String> getTarget(Object obj) {
        try {
            initNameMappings();
            return (List) nameMappings.get(obj.getClass().getName());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 打开映射所对应的Activity类.
     * @param str
     * @return
     */
    public static Class<?> getActivityName(String str) {
        HPath create = HPath.create(Uri.parse(str));
        for (HMapping hMapping : mappings) {
            if (hMapping.match(create)) {
                return hMapping.getActivity();
            }
        }
        return null;
    }


    public static String getString(Bundle bundle, String key, String defaultValue) {
        if (bundle == null) {
            return defaultValue;
        }
        Object obj = bundle.get(key);
        if (obj != null) {
            return obj + BuildConfig.VERSION_NAME;
        }
        return defaultValue;
    }
}
