package com.github.beansoftapp.android.router.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.lang.reflect.Type;

public final class JsonUtil {
    private static Gson mGson;

    private JsonUtil() {
    }

    private static Gson getGson() {
        if (mGson == null) {
            mGson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        }
        return mGson;
    }

    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }

    public static String toJson(Object obj, Type type) {
        return new Gson().toJson(obj, type);
    }

    public static <T> T fromJson(String str, Class<T> cls) {
        return getGson().fromJson(str, cls);
    }

    public static <T> T fromJson(String str, Type type) {
        return getGson().fromJson(str, type);
    }

    public static <T> T fromJson(JsonReader jsonReader, Type type) {
        return getGson().fromJson(jsonReader, type);
    }
}
