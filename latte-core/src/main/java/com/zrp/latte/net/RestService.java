package com.zrp.latte.net;

import java.util.Map;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface RestService {

    @GET
    Call<String> get(@Url String url, @QueryMap Map<String, Object> params);

    @POST
    @FormUrlEncoded
    Call<String> post(@Url String url, @FieldMap Map<String,Object> params);


    //@HeaderMap注解可添加请求头
//    @POST
//    @FormUrlEncoded
//    Call<String> post(@HeaderMap Map<String,Object> headers, @Url String url, @FieldMap Map<String,Object> params);

    @POST
    Call<String> postRaw(@HeaderMap Map<String,Object> headers,@Url String url, @Body RequestBody body);

    @PUT
    @FormUrlEncoded
    Call<String> put(@Url String url, @FieldMap Map<String,Object> params);

    @DELETE
    Call<String> delete(@Url String url, @QueryMap Map<String, Object> params);

    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url, @QueryMap Map<String, Object> params);

    @Multipart
    @POST
    Call<String> upload(@Url String url, @Part MultipartBody.Part files);

    @PUT
    Call<String> putRaw(String url, @Body RequestBody body);
}
