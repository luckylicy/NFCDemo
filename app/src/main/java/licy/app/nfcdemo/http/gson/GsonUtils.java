package licy.app.nfcdemo.http.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.TypeAdapters;

import java.util.List;

/**
 * GsonUtils
 * description: TODO
 *
 * @author : Licy
 * @date : 2019/7/23
 * email ：licy3051@qq.com
 */
public final class GsonUtils {

    private GsonUtils() {
    }

    private static Gson sGson= new GsonBuilder()
            .registerTypeAdapterFactory(TypeAdapters.newFactory(String.class, new StringTypeAdapter()))
            .registerTypeAdapterFactory(TypeAdapters.newFactory(boolean.class, Boolean.class, new BooleanTypeAdapter()))
            .registerTypeAdapterFactory(TypeAdapters.newFactory(int.class, Integer.class, new IntegerTypeAdapter()))
            .registerTypeAdapterFactory(TypeAdapters.newFactory(long.class, Long.class, new LongTypeAdapter()))
            .registerTypeAdapterFactory(TypeAdapters.newFactory(float.class, Float.class, new FloatTypeAdapter()))
            .registerTypeAdapterFactory(TypeAdapters.newFactory(double.class, Double.class, new DoubleTypeAdapter()))
            .registerTypeHierarchyAdapter(List.class, new ListTypeAdapter())
            .disableHtmlEscaping()
            .serializeNulls()
            .create();

    public static Gson getInstance() {
        return sGson;
    }

    public static Gson getGson() {
        return sGson;
    }
}
