package com.zrp.latte.net.callback;


import com.zrp.latte.ui.loader.LatteLoader;
import com.zrp.latte.ui.loader.LoaderStyle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestCallBack implements Callback<String> {

    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final LoaderStyle LOADER_STYLE;

    public RequestCallBack(IRequest request, ISuccess success, IFailure failure, IError eroor, LoaderStyle loader_style) {
        REQUEST = request;
        SUCCESS = success;
        FAILURE = failure;
        ERROR = eroor;
        LOADER_STYLE = loader_style;
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if(response.isSuccessful()){
            if(call.isExecuted()){
                if(SUCCESS != null){
                    SUCCESS.onSuccess(response.body());
                }
            }
        }else{
            if(ERROR != null){
                ERROR.onError(response.code(), response.message());
            }
        }
        if(LOADER_STYLE != null){
            LatteLoader.stopLoading();
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        if(FAILURE != null){
            FAILURE.onFailure();
        }
        if(REQUEST != null){
            REQUEST.onRequestEnd();
        }
        LatteLoader.stopLoading();
    }

}
