package com.zrp.latte.net.download;

import android.os.AsyncTask;

import com.zrp.latte.net.RestCreator;
import com.zrp.latte.net.callback.IError;
import com.zrp.latte.net.callback.IFailure;
import com.zrp.latte.net.callback.IRequest;
import com.zrp.latte.net.callback.ISuccess;

import java.util.WeakHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownLoadHandler {

    private final String URL;
    private static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    private final IRequest REQUEST;
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;

    public DownLoadHandler(String url,
                           IRequest request,
                           String download_dir,
                           String extension,
                           String name,
                           ISuccess success,
                           IFailure failure,
                           IError error) {
        URL = url;
        REQUEST = request;
        DOWNLOAD_DIR = download_dir;
        EXTENSION = extension;
        NAME = name;
        SUCCESS = success;
        FAILURE = failure;
        ERROR = error;
    }


    public final void handlerDownload(){
        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }
//        RestCreator.getRestService().download(URL,PARAMS)
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if(response.isSuccessful()){
//
//                            final ResponseBody body = response.body();
//
//                            final SaveFileTask saveFileTask = new SaveFileTask(REQUEST,SUCCESS);
//                            saveFileTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,DOWNLOAD_DIR,EXTENSION,body,NAME);
//
//                            //文件是否判断下载完全
//                            if(saveFileTask.isCancelled()){
//                                if(REQUEST != null){
//                                    REQUEST.onRequestEnd();
//                                }
//                            }
//
//                        }else{
//                            if(ERROR != null){
//                                ERROR.onError(response.code(),response.message());
//                            }
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        if(FAILURE != null){
//                            FAILURE.onFailure();
//                        }
//                    }
//                });
    }
}
