package licy.app.nfcdemo.http;


import licy.app.nfcdemo.bean.BaseObjectBean;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * ApiService
 * description: 请求服务
 *
 * @author : Licy
 */
public interface ApiService {

    @POST(ApiUrls.NET_URL_GET_ALL_DICT_BY_TYPE)
    Call<BaseObjectBean<Object>> getDicts(@Body RequestBody info);
}