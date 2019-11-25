package com.zrp.latte.ec.main.personal.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderPageViewDelegate extends BottomItemDelegate {

    @BindView(R2.id.tl_order_status)
    TabLayout mTabLayout;
    @BindView(R2.id.vp_order)
    ViewPager mViewPager;

    @Override
    public Object setLayout() {
        return R.layout.delegate_order_viewpage;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        super.onBindView(savedInstanceState, view);
        RestClient.builder()
                .url("api/order")
                //.parmas("goodsId", mGoodsId)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //绑定数据
                        final JSONObject data = JSON.parseObject(response).getJSONObject("data");

                    }
                })
                .build()
                .get();
        final String[] mTitles = {"全部订单", "代支付", "待收货", "代评价", "售后"};

        final List<Fragment> mFragments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mFragments.add(OrderDelegate.create(i));
        }
        final OrderTabPagerAdapter adapter = new OrderTabPagerAdapter(getActivity().getSupportFragmentManager(), mTitles, mFragments);

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(5);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setBackgroundColor(Color.WHITE);
        mTabLayout.setupWithViewPager(mViewPager);
    }



    @OnClick(R2.id.icon_order_return)
    public void onViewClicked() {
        getSupportDelegate().pop();
    }
}
