package com.zrp.latte.ec.main.cart.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ec.main.personal.p_function.address.AddressDelegate;
import com.zrp.latte.ui.recycler.MultipleItemEntity;
import com.zrp.latte.ui.widget.PickArrivalTimeDialog;
import com.zrp.latte.ui.widget.SwitchButton;
import com.zrp.latte.util.timer.DateUtil;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

import butterknife.OnClick;

public class SettleDelegate extends LatteDelegate implements CompoundButton.OnCheckedChangeListener {


	@BindView(R2.id.sb_sellte_receipt)
	SwitchButton mSbReceipt;
	@BindView(R2.id.tv_settle_paymoney)
	TextView mTvPaymoney;
	@BindView(R2.id.ll_settle_pickaddress)
	LinearLayout mLlSettlePickAddress;
	@BindView(R2.id.tv_settle_address_detail)
	TextView mTvAddress;
	@BindView(R2.id.ll_settle_address)
	RelativeLayout mLlSettleAddress;
	@BindView(R2.id.tv_settle_arrivaltype)
	TextView mTvArrivalType;
	@BindView(R2.id.tv_settle_arrivaltime)
	TextView mTvArrivaltime;
	@BindView(R2.id.tv_delivery_money)
	TextView mTvDeliveryMoney;
	@BindView(R2.id.tv_settle_exchange_money_show)
	TextView mtvExchangeMoneyShow;
	@BindView(R2.id.ll_settle_peisong)
	LinearLayout mLlSettlePeisong;
	@BindView(R2.id.fl_settle_ziqu)
	FrameLayout mFlSettleZiqu;
	@BindView(R2.id.tv_settle_pickpeisong)
	TextView mTvSettlePickpeisong;
	@BindView(R2.id.tv_settle_pickziqu)
	TextView mTvSettlePickziqu;


	private List<MultipleItemEntity> entities = null;
	private PickArrivalTimeDialog mArrivalTimeDialog;
	private Double originalMoney = 0.0;
	//是否开具发票
	private boolean invoice = false;
	private static final int PICK_ADDRESS = 10;

	@Override
	public Object setLayout() {
		return R.layout.delegate_settlement;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {



		mSbReceipt.setOnCheckedChangeListener(this);
		final String originalPayMoney = mTvPaymoney.getText().toString();
		if (originalMoney.equals(0.0)) {
			originalMoney = Double.valueOf(originalPayMoney.substring(originalPayMoney.indexOf("￥") + 1));
		}
		initArrivalTime();
	}





	private void initArrivalTime() {
		mTvArrivaltime.setText(DateUtil.getImmediatelyArrivalTime());
		mArrivalTimeDialog = new PickArrivalTimeDialog(Objects.requireNonNull(getContext()));
		//送达时间
		mArrivalTimeDialog.setmListener(new PickArrivalTimeDialog.OnArrivalTimePickListener() {
			@Override
			public void onTimePick(String dayType, String time, double money) {
				mTvArrivalType.setText(dayType);
				mTvArrivaltime.setText(time);

				mTvDeliveryMoney.setText(String.format("￥%s", money));
				calculatePrice();
			}
		});
	}

	@OnClick(R2.id.icon_address_back)
	public void onViewClickedBack() {
		getSupportDelegate().pop();
	}


	//开具发票
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		invoice = isChecked;
	}

	//选择收货地址
	@OnClick(R2.id.ll_settle_pickaddress)
	public void onViewClickedPick() {
		getSupportDelegate().startForResult(new AddressDelegate(), PICK_ADDRESS);
	}

	//选择收货地址
	@OnClick(R2.id.ll_settle_address)
	public void onViewClickedAddress() {
		getSupportDelegate().startForResult(new AddressDelegate(), PICK_ADDRESS);
	}


	//收货地址回调
	@Override
	public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
		if (requestCode == PICK_ADDRESS && resultCode == RESULT_OK) {
			final String detail = data.getString("p_address");
			mLlSettlePickAddress.setVisibility(View.GONE);
			mTvAddress.setText(detail);
			mLlSettleAddress.setVisibility(View.VISIBLE);
		}
	}

	//送达时间
	@OnClick(R2.id.tv_settle_arrivaltime)
	public void onViewClickedArrivalTime() {
		if (mArrivalTimeDialog != null) {
			mArrivalTimeDialog.show();
		}
	}


	@OnClick(R2.id.tv_settle_pickpeisong)
	public void onViewClickedPeisong(View view) {
		mTvSettlePickziqu.setBackgroundColor(Color.parseColor("#FFEFD5"));
		mTvSettlePickziqu.setTextColor(Color.GRAY);
		mFlSettleZiqu.setVisibility(View.GONE);
		mTvSettlePickpeisong.setTextColor(Color.BLACK);
		mLlSettlePeisong.setBackgroundColor(Color.WHITE);
		mLlSettlePeisong.setVisibility(View.VISIBLE);
	}

	@OnClick(R2.id.tv_settle_pickziqu)
	public void onViewClickZiqu(View view) {
		mTvSettlePickziqu.setBackgroundColor(Color.WHITE);
		mTvSettlePickziqu.setTextColor(Color.BLACK);
		mFlSettleZiqu.setVisibility(View.VISIBLE);
		mTvSettlePickpeisong.setTextColor(Color.GRAY);
		mLlSettlePeisong.setBackgroundColor(Color.parseColor("#FFEFD5"));
		mLlSettlePeisong.setVisibility(View.GONE);
	}


	private void calculatePrice() {
		final String deliveryText = mTvDeliveryMoney.getText().toString();
		final Double deliveryMoney = Double.valueOf(deliveryText.substring(deliveryText.indexOf("￥") + 1));
		mTvPaymoney.setText(String.format("￥%s", originalMoney + deliveryMoney));
	}



	//进入支付界面 条件
	@OnClick(R2.id.btn_settle_submit_order)
	public void onViewClickedSubmit() {
		if(mLlSettlePickAddress.getVisibility() == View.VISIBLE){
			Toast.makeText(getContext(), "请选择收货地址",Toast.LENGTH_SHORT).show();
		}else{
			if(invoice){
				Toast.makeText(getContext(), "需要开具发票", Toast.LENGTH_SHORT).show();
			}
			getSupportDelegate().start(new SubmitDelegate());
		}

	}
}
