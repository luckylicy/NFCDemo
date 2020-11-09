package licy.app.nfcdemo.http.gson;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/EasyHttp
 *    time   : 2020/05/05
 *    desc   : int / Integer 解析适配器 {@link com.google.gson.internal.bind.TypeAdapters#INTEGER}
 */
public class IntegerTypeAdapter extends DoubleTypeAdapter {

    @Override
    public Number read(JsonReader in) throws IOException {
        Number number = super.read(in);
        if (number != null) {
            return number.intValue();
        }
        return null;
    }
}