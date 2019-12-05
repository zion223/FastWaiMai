package com.zrp.latte.net.interceptor;

import com.zrp.latte.util.file.FileUtil;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DebugInterceptor extends BaseInterceptor {


    private final String DEBUG_URL;
    private final int DEBUG_RAW_ID;

    public DebugInterceptor(String url, int id) {
        DEBUG_URL = url;
        DEBUG_RAW_ID = id;
    }

    private Response getResponse(Chain chain,String json){
        return new Response.Builder()
                .code(200)
                .addHeader("ContentType","application/json")
                .body(ResponseBody.create(MediaType.parse("application/json"), json))
                .message("OK")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .build();

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final String url = chain.request().url().toString();
        if(url.contains(DEBUG_URL)){
            return debugResponse(chain, DEBUG_RAW_ID);
        }
        return chain.proceed(chain.request());
    }

    private Response debugResponse(Chain chain, int rawId) {
        final String jsonString = FileUtil.getRawFile(rawId);
        return getResponse(chain,jsonString);
    }
}
