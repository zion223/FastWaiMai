package com.zrp.latte.ec.main.personal.discount;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.app.Latte;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.ui.tab.TabPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DiscountPageViewDelegate extends BottomItemDelegate {

	@BindView(R2.id.tv_discount_exchange_code)
	TextInputEditText mTvExchangeCode;
	@BindView(R2.id.tl_discount_status)
	TabLayout mTabLayout;
	@BindView(R2.id.vp_discount_pager)
	ViewPager mViewPager;


	@Override
	public Object setLayout() {
		return R.layout.delegate_discount;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
		super.onBindView(savedInstanceState, view);
		final String[] mTitles = {"可使用的优惠券(2)", "不可使用的优惠券(0)"};

		final List<Fragment> mFragments = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			mFragments.add(DiscountDelegate.create(i));
		}
		final TabPagerAdapter adapter = new TabPagerAdapter(getActivity().getSupportFragmentManager(), mTitles, mFragments);

		mViewPager.setAdapter(adapter);
		mViewPager.setOffscreenPageLimit(2);
		mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
		mTabLayout.setBackgroundColor(Color.WHITE);
		mTabLayout.setupWithViewPager(mViewPager);
	}


	@OnClick(R2.id.icon_discount_back)
	public void onViewClickedReturn() {
		getSupportDelegate().pop();
	}



	@OnClick(R2.id.btn_discount_exchange)
	public void onViewClickedExchange() {
		Toast.makeText(Latte.getApplication(), "已经兑换了",Toast.LENGTH_SHORT).show();
	}
}
