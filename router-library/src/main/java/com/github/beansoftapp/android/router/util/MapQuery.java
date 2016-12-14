package com.github.beansoftapp.android.router.util;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 对URL中的参数进行转码.
 */
public class MapQuery {

    public static Map<String, String> splitQuery(String str) throws UnsupportedEncodingException {
        Map<String, String> linkedHashMap = new LinkedHashMap();
        for (String str2 : str.split("&")) {
            int indexOf = str2.indexOf("=");
            linkedHashMap.put(URLDecoder.decode(str2.substring(0, indexOf), "UTF-8"), URLDecoder.decode(str2.substring(indexOf + 1), "UTF-8"));
        }
        return linkedHashMap;
    }

    public static String urlEncodeUTF8(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Throwable e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static String urlEncodeUTF8(Map<?, ?> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry entry : map.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(String.format("%s=%s", new Object[]{urlEncodeUTF8(entry.getKey().toString()), urlEncodeUTF8(entry.getValue().toString())}));
        }
        return stringBuilder.toString();
    }
}
