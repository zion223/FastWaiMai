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
        final ArrayList<Interceptor> interceptors = new ArrayList<>();
        //首页广告数据
        interceptors.add(new DebugInterceptor("api/home",R.raw.home));
        //首页特色专区
        interceptors.add(new DebugInterceptor("api/spec",R.raw.home_spec));
        //购物车数据
        interceptors.add(new DebugInterceptor("api/shopcart",R.raw.shop_cart));
        //订单数据
        interceptors.add(new DebugInterceptor("api/order/0",R.raw.order_all));
        interceptors.add(new DebugInterceptor("api/order/1",R.raw.order_pay));
        interceptors.add(new DebugInterceptor("api/order/2",R.raw.order_receive));
        interceptors.add(new DebugInterceptor("api/order/3",R.raw.order_evaluate));
        interceptors.add(new DebugInterceptor("api/order/4",R.raw.order_after_market));
        //收货地址
        interceptors.add(new DebugInterceptor("api/address",R.raw.address));
        //商品详情
        interceptors.add(new DebugInterceptor("api/goodsdetail",R.raw.goods_detail_1));
        //购物车猜你喜欢
        interceptors.add(new DebugInterceptor("api/youlike",R.raw.youlike));
        //分页左边栏
        interceptors.add(new DebugInterceptor("api/sort",R.raw.categories));
        //个人页面数据
        //interceptors.add(new DebugInterceptor("api/personal",R.raw.personal));
        //分页右边栏
        interceptors.add(new DebugInterceptor("api/content/0",R.raw.lk001));
        interceptors.add(new DebugInterceptor("api/content/1",R.raw.lk002));
        interceptors.add(new DebugInterceptor("api/content/2",R.raw.lk003));
        //优惠券
        interceptors.add(new DebugInterceptor("api/discount/0",R.raw.avaliable_discount));
        interceptors.add(new DebugInterceptor("api/discount/1",R.raw.unavaliable_discount));
        for(int i = 3;i < 16;i ++){
            interceptors.add(new DebugInterceptor("api/content/"+i,R.raw.lk003));
        }

        Latte.init(this)
                .withApiHost("http://192.168.1.54:8082")
                .withInterceptors(interceptors)
                .withIcon(new FontAwesomeModule())
                .withJavascriptInterface("latte")
                .withWebEvent("test",new TestEvent())
                .configure();
    }
}
