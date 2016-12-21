package com.github.beansoftapp.android.router;

import android.net.Uri;

/**
 * HPath 只用于映射规则的匹配, 因此会忽略其中的查询参数.
 * eg: haction://aa/bb?momentId=2 最终只会留下 haction://aa/bb
 * 查询参数的获取通过下列方式进行:
 * <code>
 * Bundle parseExtras = hMapping.parseExtras(path.getUri());
 * </code>
 */
public class HPath {
    private static Uri mUri;
    private HPath next;
    private String value;

    private HPath(String str) {
        this.value = str;
    }

    /**
     * 比较两个Path是否匹配
     * @param hPath
     * @param anotherPath
     * @return
     */
    public static boolean match(HPath hPath, HPath anotherPath) {
        if (hPath.length() != anotherPath.length()) {
            return false;
        }
        while (hPath != null) {
            if (!hPath.match(anotherPath)) {
                return false;
            }
            hPath = hPath.next;
            anotherPath = anotherPath.next;
        }
        return true;
    }

    public static HPath create(Uri uri) {
        if (uri.getScheme() == null) {
            throw new IllegalArgumentException("You must special scheme on uri");
        }
        HPath hPath = new HPath(uri.getScheme().concat("://"));// root path is schema

        mUri = uri;
        String path = uri.getPath();
        if (path == null) {
            path = "";
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        parse(hPath, uri.getHost() + path);
        return hPath;
    }



    public Uri getUri() {
        return mUri;
    }

    private static void parse(HPath hPath, String str) {
        String[] split = str.split("/");
        int length = split.length;
        int i = 0;
        while (i < length) {
            HPath nextPath = new HPath(split[i]);
            hPath.next = nextPath;
            i++;
            hPath = nextPath;
        }
    }

    public HPath next() {
        return this.next;
    }

    /**
     * 获取path的长度
     * @return
     */
    public int length() {
        int i = 1;
        HPath next = this.next;
        while (next != null && next.next != null) {
            i++;
            next = next.next;
        }
        return i;
    }

    private boolean match(HPath hPath) {
        return isArgument() || this.value.equals(hPath.value);
    }

    /**
     * 是否为参数, :// 这样的就会被处理为相同类型
     * @return
     */
    public boolean isArgument() {
        return this.value.startsWith(":");
    }

    public String argument() {
        return this.value.substring(1);
    }

    public String value() {
        return this.value;
    }

    public boolean isHttp() {
        String toLowerCase = this.value.toLowerCase();
        return toLowerCase.startsWith("http://") || toLowerCase.startsWith("https://");
    }

    @Override
    public String toString() {
        return "HPath{ value='" + value + '\'' +
                '}';
    }

}