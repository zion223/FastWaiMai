package com.zrp.latte.ec.main.cart.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ui.widget.CountDownView;
import com.zrp.latte.ui.widget.SmoothCheckBox;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class SubmitDelegate extends LatteDelegate implements SmoothCheckBox.OnSmoothCheckedChangeListener, CountDownView.OnTimeCompleteListener {


	@BindView(R2.id.ll_submit_zhifubao)
	LinearLayout mLlSubmitZhifubao;
	@BindView(R2.id.ll_submit_showzhifubao)
	LinearLayout mLlSubmitShowzhifubao;
	@BindView(R2.id.cb_submit_meituan)
	SmoothCheckBox mCbMeituanPay;
	@BindView(R2.id.cb_submit_weixin)
	SmoothCheckBox mCbWeixinPay;
	@BindView(R2.id.cb_submit_qq)
	SmoothCheckBox mCbQqPay;
	@BindView(R2.id.cb_submit_alipay)
	SmoothCheckBox mCbAlipay;
	@BindView(R2.id.tv_submit_shengyu_show)
	CountDownView mTvPayLeftTime;
	@BindView(R2.id.tv_submit_money)
	TextView mTvPayMoney;

	private ArrayList<SmoothCheckBox> payList = new ArrayList<>();

	@Override
	public Object setLayout() {
		return R.layout.delegate_submit;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
		//支付方式
		initPayCheckBox();

		//订单支付剩余时间15min开始倒计时
		initPayLeftTime();

		//支付金额
		final SpannableString paymoney = new SpannableString("￥24.8");
		paymoney.setSpan(new AbsoluteSizeSpan(20, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mTvPayMoney.setText(paymoney);
	}


	private void initPayLeftTime() {
		mTvPayLeftTime.initTime(15, 0);
		mTvPayLeftTime.start();
		mTvPayLeftTime.setOnTimeCompleteListener(this);
	}

	private void initPayCheckBox() {
		payList.add(mCbMeituanPay);
		payList.add(mCbWeixinPay);
		payList.add(mCbQqPay);
		payList.add(mCbAlipay);

		for (SmoothCheckBox tempBox : payList) {
			tempBox.setOnSmoothCheckedChangeListener(this);
		}
	}


	@OnClick(R2.id.ll_submit_showzhifubao)
	public void onViewClickedShowAlipay() {
		mLlSubmitZhifubao.setVisibility(View.VISIBLE);
		mLlSubmitShowzhifubao.setVisibility(View.GONE);
	}


	@Override
	public void onSmoothCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
		if (isChecked) {
			for (SmoothCheckBox box : payList) {
				if (box.getId() == checkBox.getId()) {
					box.setChecked(true);
				} else {
					box.setChecked(false);
				}
			}
		}
	}


	@Override
	public void onTimeComplete() {
		getSupportDelegate().pop();
	}


	@OnClick(R2.id.icon_address_back)
	public void onViewClickedBack() {
		getSupportDelegate().pop();
	}



	@OnClick(R2.id.btn_settle_submit_order)
	public void onViewClickedSubmit() {
		Toast.makeText(getContext(), "提交订单",Toast.LENGTH_SHORT).show();
	}
}
