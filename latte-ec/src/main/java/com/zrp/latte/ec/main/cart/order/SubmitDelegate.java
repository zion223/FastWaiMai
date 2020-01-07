package com.zrp.latte.ec.main.cart.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ui.widget.CountDownView;
import com.zrp.latte.ui.widget.SmoothCheckBox;

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

	private SmoothCheckBox[] payBox = new SmoothCheckBox[4];

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

	}




	private void initPayLeftTime() {
		mTvPayLeftTime.initTime(15,0);
		mTvPayLeftTime.start();
		mTvPayLeftTime.setOnTimeCompleteListener(this);
	}

	private void initPayCheckBox() {
		payBox[0] = mCbMeituanPay;
		payBox[1] = mCbWeixinPay;
		payBox[2] = mCbQqPay;
		payBox[3] = mCbAlipay;
		for (SmoothCheckBox tempBox : payBox) {
			tempBox.setOnSmoothCheckedChangeListener(this);
		}
	}


	@OnClick(R2.id.ll_submit_showzhifubao)
	public void onViewClicked() {
		mLlSubmitZhifubao.setVisibility(View.VISIBLE);
		mLlSubmitShowzhifubao.setVisibility(View.GONE);
	}


	@Override
	public void onSmoothCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
		if (isChecked) {
			for (SmoothCheckBox box : payBox) {
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
}
