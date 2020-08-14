package com.zrp.latte.net;

import android.content.Context;

import com.zrp.latte.net.callback.IError;
import com.zrp.latte.net.callback.IFailure;
import com.zrp.latte.net.callback.IRequest;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.net.callback.RequestCallBack;
import com.zrp.latte.net.download.DownLoadHandler;
import com.zrp.latte.net.rx.RxRestService;
import com.zrp.latte.ui.loader.LatteLoader;
import com.zrp.latte.ui.loader.LoaderStyle;

import java.io.File;
import java.util.HashMap;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RestClient {


    private final String URL;
    private final WeakHashMap<String,Object> PARAMS = RestCreator.getParams();
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final RequestBody BODY;
    private final LoaderStyle LOADER_STYLE;
    private final File FILE;
    private Context CONTEXT;


    public RestClient(String url, WeakHashMap params,
                      String download_dir, String extension, String name, IRequest request,
                      ISuccess success,
                      IFailure failure,
                      IError eroor,
                      RequestBody body,
                      LoaderStyle style,
                      File file, Context context) {
        DOWNLOAD_DIR = download_dir;
        EXTENSION = extension;
        NAME = name;
        this.FILE = file;
        this.PARAMS.putAll(params);
        this.URL = url;
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = eroor;
        this.BODY = body;
        this.LOADER_STYLE = style;
        this.CONTEXT = context;
    }

    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }

    private Observable<String> request(HttpMethod method) {
        RxRestService service = RestCreator.getRestService();
        Observable<String> call = null;

        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }

        if (LOADER_STYLE != null) {
            //LatteLoader.showLoading(CONTEXT, LOADER_STYLE);
        }
        switch (method) {
            case GET:
                call = service.get(URL, PARAMS);
                break;
            case PUT:
                call = service.put(URL, PARAMS);
                break;
            case DELETE:
                call = service.delete(URL, PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()),FILE);
                final MultipartBody.Part body = MultipartBody.Part.createFormData("file",FILE.getName(), requestBody);
                call = service.upload(URL, body);
                break;
            case POST:
                call = service.post(URL, PARAMS);
                break;
            case POST_RAW:
                final HashMap<String,Object> HEADER = new HashMap<>();
                HEADER.put("Content-Type","application/json");
                call = service.postRaw(HEADER, URL, BODY);
                break;
            case PUT_RAW:
                call = service.putRaw(URL, BODY);
                break;

            default:
                break;

        }
        return call;
//        if(call != null){
//            call.enqueue(getRequestCallback());
//        }
    }

    private Callback<String> getRequestCallback() {
        return new RequestCallBack(REQUEST, SUCCESS, FAILURE, ERROR, LOADER_STYLE);
    }

    public final Observable<String> get() {
        return request(HttpMethod.GET);
    }

    public final Observable<String> post() {
        if (BODY == null) {
            return request(HttpMethod.POST);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            return request(HttpMethod.POST_RAW);
        }
    }

    public final void put() {
        if (BODY == null) {
            request(HttpMethod.PUT);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            request(HttpMethod.PUT_RAW);
        }
    }

    public final void delete() {
        request(HttpMethod.DELETE);
    }

    public final void upload() {
        request(HttpMethod.UPLOAD);
    }

    public final void download() {
        new DownLoadHandler(
                URL,REQUEST,DOWNLOAD_DIR,EXTENSION,NAME,SUCCESS,FAILURE,ERROR
        ).handlerDownload();
    }



}
