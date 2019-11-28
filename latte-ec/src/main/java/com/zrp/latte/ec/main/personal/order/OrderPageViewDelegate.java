package com.zrp.latte.ec.main.personal.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.ui.tab.TabPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderPageViewDelegate extends BottomItemDelegate {


    private static final String ARGS_CURRENT_ITEM = "ARGS_CURRENT_ITEM";

    @BindView(R2.id.tl_order_status)
    TabLayout mTabLayout;
    @BindView(R2.id.vp_order)
    ViewPager mViewPager;

    public static OrderPageViewDelegate create(int item) {
        final Bundle args = new Bundle();
        args.putInt(ARGS_CURRENT_ITEM, item);
        final OrderPageViewDelegate delegate = new OrderPageViewDelegate();
        delegate.setArguments(args);
        return delegate;
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_order_viewpage;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        super.onBindView(savedInstanceState, view);
        final String[] mTitles = {"全部订单", "待支付", "待收货", "待评价", "售后"};

        final List<Fragment> mFragments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mFragments.add(OrderDelegate.create(i));
        }
        final TabPagerAdapter adapter = new TabPagerAdapter(getActivity().getSupportFragmentManager(), mTitles, mFragments);

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(5);
        final Bundle arg = getArguments();
        final int item = arg.getInt(ARGS_CURRENT_ITEM);
        //设置当前item位置
        mViewPager.setCurrentItem(item);

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setBackgroundColor(Color.WHITE);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    @OnClick(R2.id.icon_order_return)
    public void onViewClicked() {
        getSupportDelegate().pop();
    }
}
