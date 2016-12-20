package com.github.beansoftapp.android.router.action;

import android.net.Uri;
import com.github.beansoftapp.android.router.HPath;

import java.util.HashMap;
import java.util.Map;

/**
 * 动作映射, 不支持 Native 跳转.
 */
public class HActionMapping {
    private Class<? extends HAction> action;
    private String format;
    private HPath formatPath;

    public HActionMapping(String str, Class<? extends HAction> cls) {
        if (str == null) {
            throw new NullPointerException("format can not be null");
        } else if (cls == null) {
            throw new NullPointerException("action can not be null");
        } else {
            this.format = str;
            this.action = cls;
            if (str.toLowerCase().startsWith("http://") || str.toLowerCase().startsWith("https://")) {
                this.formatPath = HPath.create(Uri.parse(str));
            } else {
                this.formatPath = HPath.create(Uri.parse("haction://".concat(str)));
            }
        }
    }

    public String getFormat() {
        return this.format;
    }

    public Class<? extends HAction> getAction() {
        return this.action;
    }

    public String toString() {
        return this.format + " => " + this.action;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof HActionMapping) {
            return ((HActionMapping) obj).format.equals(((HActionMapping) obj).format);
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

    public Map<String, Object> parseExtras(Uri uri) {
        Map<String, Object> hashMap = new HashMap();
        HPath next = this.formatPath.next();
        HPath nextPath = HPath.create(uri).next();
        while (next != null) {
            if (next.isArgument()) {
                hashMap.put(next.argument(), nextPath.value());
            }
            next = next.next();
            nextPath = nextPath.next();
        }
        for (String str : uri.getQueryParameterNames()) {
            hashMap.put(str, uri.getQueryParameter(str));
        }
        return hashMap;
    }
}
