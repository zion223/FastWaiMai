package com.example.latte.example;

import android.support.multidex.MultiDexApplication;

import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.zrp.latte.app.Latte;
import com.zrp.latte.delegates.web.event.TestEvent;
import com.zrp.latte.net.interceptor.DebugInterceptor;

import java.util.ArrayList;

import okhttp3.Interceptor;


public class ExampleApp extends MultiDexApplication {

    /**
     * 访问本机地址须使用IP地址访问无法使用localhost形式
     */
    @Override
    public void onCreate() {
        super.onCreate();

        //Retrofit拦截器
        ArrayList<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(new DebugInterceptor("api/sort_content",R.raw.sort_content_data));
        interceptors.add(new DebugInterceptor("api/sort",R.raw.sort_list_data));
        interceptors.add(new DebugInterceptor("api/books",R.raw.books));
        interceptors.add(new DebugInterceptor("api/shop_cart",R.raw.shop_cart));

        Latte.init(this)
                //.withApiHost("http://192.168.1.54:8082/bookstore/api/")
                .withApiHost("http://192.168.1.54:8082/bookstore/")
                .withInterceptors(interceptors)
                .withIcon(new FontAwesomeModule())
                .withJavascriptInterface("latte")
                .withWebEvent("test",new TestEvent())
                .configure();
    }
}
