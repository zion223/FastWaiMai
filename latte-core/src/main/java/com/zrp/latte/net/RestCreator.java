package com.zrp.latte.net;

import com.zrp.latte.app.ConfigType;
import com.zrp.latte.app.Latte;

import java.util.ArrayList;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestCreator {

    private static final class ParamsHolder {
        private static final WeakHashMap<String, Object> PARAMS = new WeakHashMap<>();
    }
    public static WeakHashMap<String, Object> getParams() {
        return ParamsHolder.PARAMS;
    }

    private RestCreator(){

    }

    private static final class RetrofitHolder {
        static final String BASE_URL = (String) Latte.getConfigurations().get(ConfigType.API_HOST.name());

        static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }
    private static final class OKHttpHolder{

        static final int TIME_OUT = 60;
        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();
        private static final ArrayList<Interceptor> INTERCEPTORS = Latte.getConfiguration(ConfigType.INTERCEPTOR.name());
        private static OkHttpClient.Builder addInterceptor(){
            if(INTERCEPTORS != null && !INTERCEPTORS.isEmpty()){
                for(Interceptor interceptor:INTERCEPTORS){
                    BUILDER.addInterceptor(interceptor);
                }
            }
            return BUILDER;
        }

        static final OkHttpClient OK_HTTP_CLIENT = addInterceptor()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    private static final class RestServiceHolder{
        static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }

    public static RestService getRestService(){
        return RestServiceHolder.REST_SERVICE;
    }
}
