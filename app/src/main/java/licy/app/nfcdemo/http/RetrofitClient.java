package licy.app.nfcdemo.http;


import java.util.concurrent.TimeUnit;

import licy.app.nfcdemo.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static volatile RetrofitClient instance;
    private Retrofit retrofit;
    private OkHttpClient okHttpClient;

    private RetrofitClient() {
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    /**
     * 设置Header
     *
     * @return
     */
    private Interceptor getHeaderInterceptor() {
        return chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder();
            //添加Token
            requestBuilder.addHeader("Authorization", TokenConfig.getToken());
            Request request = requestBuilder.build();
            return chain.proceed(request);
        };
    }

    /**
     * 设置拦截器 打印日志
     *
     * @return
     */
    private Interceptor getInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //显示日志
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        return interceptor;
    }

    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient().newBuilder()
                    //设置Header
                    .callTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(getHeaderInterceptor())
                    //设置拦截器
                    .addInterceptor(getInterceptor())
                    .build();
        }

        return okHttpClient;
    }

    public ApiService getApi() {
        return create(ApiService.class);
    }


    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    //设置网络请求的Url地址
                    .baseUrl(ApiUrls.DEVELOP_ENVIRONMENT_FOR_API)
                    //设置数据解析器
                    .addConverterFactory(GsonConverterFactory.create())
                    //设置网络请求适配器，使其支持RxJava与RxAndroid
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
        return retrofit.create(service);
    }
}
