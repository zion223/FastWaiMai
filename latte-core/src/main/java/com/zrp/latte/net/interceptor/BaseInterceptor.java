package com.zrp.latte.net.interceptor;

import java.io.IOException;
import java.util.LinkedHashMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Response;

public abstract class BaseInterceptor implements Interceptor {


    //LinkedHashMap:有序的HashMap
    protected LinkedHashMap<String,String> getUrlParmas(Chain chain){
        final HttpUrl url = chain.request().url();
        int size = url.querySize();
        final LinkedHashMap<String,String> params = new LinkedHashMap<>();
        for(int i = 0; i< size; i++){
            params.put(url.queryParameterName(i),url.queryParameterValue(i));
        }
        return params;
    }


    protected String getUrlParmas(Chain chain,String key){
        return chain.request().url().queryParameter(key);
    }

    protected LinkedHashMap<String,String> getBodyParams(Chain chain){
        final FormBody formBody = (FormBody) chain.request().body();
        final LinkedHashMap<String,String> params = new LinkedHashMap<>();
        int size = 0;
        if(formBody != null){
            size = formBody.size();
        }
        for(int i = 0; i < size; i++){
            params.put(formBody.name(i),formBody.value(i));
        }
        return params;
    }

    protected String  getBodyParams(Chain chain,String key){
        return getBodyParams(chain).get(key);
    }
}
