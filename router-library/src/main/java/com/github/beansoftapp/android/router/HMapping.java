package com.github.beansoftapp.android.router;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.Set;

/**
 * 映射类
 */
public class HMapping {
    private Class<? extends Activity> activity;
    private String format;
    private HPath formatPath;
    private boolean isPublic;
    private boolean needLogin;
    private String postExecute;
    private String preExecute;// 打开Activity之前需要执行的动作
    private String version;

    public HMapping(String str, Class<? extends Activity> cls,
                    boolean needLogin, String version, boolean isPublic, String preExecute, String postExecute) {
        if (str == null) {
            throw new NullPointerException("format can not be null");
        } else if (cls == null) {
            throw new NullPointerException("activity can not be null");
        } else {
            this.format = str;
            this.activity = cls;
            this.needLogin = needLogin;
            this.version = version;
            this.isPublic = isPublic;
            this.preExecute = preExecute;
            this.postExecute = postExecute;
            if (str.toLowerCase().startsWith("http://") || str.toLowerCase().startsWith("https://")) {
                this.formatPath = HPath.create(Uri.parse(str));
            } else {
                this.formatPath = HPath.create(Uri.parse(HRouter.URL_SCHEME + "://".concat(str)));
            }
        }
    }

    public Class<? extends Activity> getActivity() {
        return this.activity;
    }

    public String getFormat() {
        return this.format;
    }

    public boolean isNeedLogin() {
        return this.needLogin;
    }

    public String getVersion() {
        return this.version;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public String getPreExecute() {
        return this.preExecute;
    }

    public String getPostExecute() {
        return this.postExecute;
    }

    public String toString() {
        return this.format + " => " + this.activity;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof HMapping) {
            return ((HMapping) obj).format.equals(((HMapping) obj).format);
        }
        return false;
    }

    public int hashCode() {
        return this.format.hashCode();
    }

    public boolean match(HPath hPath) {
        if (this.formatPath.isHttp()) {
            return HPath.match(this.formatPath, hPath);
        }
        return HPath.match(this.formatPath.next(), hPath.next());
    }

    public Bundle parseExtras(Uri uri) {
        Bundle bundle = new Bundle();
        HPath next = this.formatPath.next();
        HPath next2 = HPath.create(uri).next();
        while (next != null) {
            if (next.isArgument()) {
                put(bundle, next.argument(), next2.value());
            }
            next = next.next();
            next2 = next2.next();
        }
        for (String str : uri.getQueryParameterNames()) {
            put(bundle, str, uri.getQueryParameter(str));
        }
        return bundle;
    }

    /**
     * 解析并复制一份新Bundle, 暂时未用. 旧调用在 HRouter中: pareseBundle = hMapping.pareseBundle(bundle);
     * @param bundle
     * @return
     */
    public Bundle pareseBundle(Bundle bundle) {
        Set<String> keySet = bundle.keySet();
        Bundle bundleCopy = new Bundle();
        for (String str : keySet) {
            Object obj = bundle.get(str);
            if (!(obj == null || TextUtils.isEmpty(obj.toString()))) {
                putObject(bundleCopy, str, obj);
            }
        }

        Bundle resultBundle = new Bundle();
        for (String bundleKey : bundleCopy.keySet()) {
            Object bundleValue = bundleCopy.get(bundleKey);

            putObject(resultBundle, bundleKey, bundleValue);
        }
        return resultBundle;
    }

    private void putObject(Bundle bundle, String str, Object obj) {
        if (obj instanceof String) {
            bundle.putString(str, obj.toString());
        } else if (obj instanceof Serializable) {
            bundle.putSerializable(str, (Serializable) obj);
        } else if (obj instanceof Parcelable) {
            bundle.putParcelable(str, (Parcelable) obj);
        } else if (obj instanceof Boolean) {
            bundle.putBoolean(str, ((Boolean) obj).booleanValue());
        } else if (obj instanceof Integer) {
            bundle.putInt(str, ((Integer) obj).intValue());
        } else {
            bundle.putString(str, obj.toString());
        }
    }

    private void put(Bundle bundle, String key, String value) {
        bundle.putString(key, value);
    }
}
