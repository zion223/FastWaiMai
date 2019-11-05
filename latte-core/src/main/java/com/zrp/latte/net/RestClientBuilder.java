package com.zrp.latte.net;

import android.content.Context;

import com.zrp.latte.net.callback.IError;
import com.zrp.latte.net.callback.IFailure;
import com.zrp.latte.net.callback.IRequest;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.ui.loader.LoaderStyle;

import java.io.File;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RestClientBuilder {

    private  String mUrl = null;
    private  static final WeakHashMap<String,Object> PARAMS = RestCreator.getParams();
    private  IRequest mIRequest = null;
    private  ISuccess mISuccess = null;
    private  IFailure mIFailure = null;
    private  IError mIError = null;
    private  RequestBody mBody = null;
    private Context mContext = null;
    private LoaderStyle mStyle = null;
    private File mFile = null;
    private String mDownloadDir = null;
    private String mExtension = null;
    private String mName = null;


    RestClientBuilder(){

    }
    public final RestClientBuilder url(String url){
        this.mUrl = url;
        return this;
    }
    public final RestClientBuilder parmas(WeakHashMap<String,Object> parmas){
        PARAMS.putAll(parmas);
        return this;
    }
    public final RestClientBuilder parmas(String key,Object value){
        PARAMS.put(key, value);
        return this;
    }

    public final RestClientBuilder onRequest(IRequest iRequest) {
        this.mIRequest = iRequest;
        return this;
    }
    public final RestClientBuilder success(ISuccess iSuccess) {
        this.mISuccess = iSuccess;
        return this;
    }

    public final RestClientBuilder failure(IFailure iFailure) {
        this.mIFailure = iFailure;
        return this;
    }

    public final RestClientBuilder error(IError iError) {
        this.mIError = iError;
        return this;
    }
    public final RestClientBuilder raw(String raw) {
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final RestClientBuilder loader(Context context, LoaderStyle style){
        this.mStyle = style;
        this.mContext = context;
        return this;
    }

    public final RestClientBuilder loader(Context context){
        this.mStyle = LoaderStyle.BallClipRotatePulseIndicator;
        this.mContext = context;
        return this;
    }
    public final RestClientBuilder file(File file){
        this.mFile = file;
        return this;
    }
    public final RestClientBuilder file(String file){
        this.mFile = new File(file);
        return this;
    }
    public final RestClientBuilder name(String name) {
        this.mName = name;
        return this;
    }

    public final RestClientBuilder dir(String dir) {
        this.mDownloadDir = dir;
        return this;
    }

    public final RestClientBuilder extension(String extension) {
        this.mExtension = extension;
        return this;
    }

    public final RestClient build() {
        return new RestClient(mUrl,PARAMS,
                mDownloadDir, mExtension, mName, mIRequest, mISuccess, mIFailure,
                mIError, mBody,mStyle, mFile, mContext);
    }


}
