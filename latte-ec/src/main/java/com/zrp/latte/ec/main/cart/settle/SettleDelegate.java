package com.zrp.latte.ec.main.cart.settle;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.app.Latte;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ec.main.personal.address.AddressDelegate;
import com.zrp.latte.ec.main.personal.address.AddressItemFields;
import com.zrp.latte.ui.datepicker.DatePickerDialog;
import com.zrp.latte.ui.datepicker.OnConfirmeListener;
import com.zrp.latte.ui.recycler.MultipleItemEntity;
import com.zrp.latte.ui.widget.PickArrivalTimeDialog;
import com.zrp.latte.ui.widget.SmoothCheckBox;
import com.zrp.latte.ui.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SettleDelegate extends LatteDelegate implements SmoothCheckBox.OnSmoothCheckedChangeListener, CompoundButton.OnCheckedChangeListener {

	@BindView(R2.id.cb_settle_weixin)
	SmoothCheckBox mCbWeixin;
	@BindView(R2.id.cb_settle_ali)
	SmoothCheckBox mCbAli;
	@BindView(R2.id.cb_settle_hua)
	SmoothCheckBox mCbHua;
	@BindView(R2.id.tv_settle_exchange_detail)
	TextView mTvExchangeDetail;
	@BindView(R2.id.sb_sellte_exchange)
	SwitchButton mSbExchange;
	@BindView(R2.id.tv_settle_exchange_money)
	TextView mTvExchangeMoney;
	@BindView(R2.id.ll_settle_exchange)
	LinearLayout mLlExchange;
	@BindView(R2.id.tv_settle_paymoney)
	TextView mTvPaymoney;
	@BindView(R2.id.ll_settle_pickaddress)
	LinearLayout mLlSettlePickAddress;
	@BindView(R2.id.tv_settle_name)
	TextView mTvAddressName;
	@BindView(R2.id.tv_settle_phone)
	TextView mTvAddressPhone;
	@BindView(R2.id.ll_settle_address)
	LinearLayout mLlSettleAddress;
	@BindView(R2.id.tv_settle_arrivaltime)
	TextView mTvArrivaltime;

	private final SmoothCheckBox[] payBox = new SmoothCheckBox[3];

	private PickArrivalTimeDialog mArrivalTimeDialog;
	private Double originalMoney = 0.0;
	public static final int PICK_ADDRESS = 10;

	@Override
	public Object setLayout() {
		return R.layout.delegate_settlement;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
		payBox[0] = mCbWeixin;
		payBox[1] = mCbAli;
		payBox[2] = mCbHua;
		for (SmoothCheckBox aPayBox : payBox) {
			aPayBox.setOnSmoothCheckedChangeListener(this);
		}
		mSbExchange.setOnCheckedChangeListener(this);
		final String oldMoney = mTvPaymoney.getText().toString();
		if (originalMoney.equals(0.0)) {
			originalMoney = Double.valueOf(oldMoney.substring(oldMoney.indexOf("￥") + 1));
		}
		mArrivalTimeDialog = new PickArrivalTimeDialog(Latte.getApplication());
		mArrivalTimeDialog.setListener(new PickArrivalTimeDialog.OnArrivalTimePickListener() {
			@Override
			public void onTimePick(String time, double money) {
				Toast.makeText(getContext(), time+money, Toast.LENGTH_LONG).show();
			}
		});

	}

	//支付方式
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

	@OnClick(R2.id.icon_address_back)
	public void onViewClickedBack() {
		getSupportDelegate().pop();
	}

	//积分兑换
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		final String detail = mTvExchangeDetail.getText().toString();
		final Double exchangeMoney = Double.valueOf(detail.substring(detail.indexOf("￥") + 1));

		mTvExchangeMoney.setText(String.format("-￥%s", exchangeMoney));
		if (isChecked) {
			mLlExchange.setVisibility(View.VISIBLE);
			Toast.makeText(getContext(), "已兑换" + exchangeMoney, Toast.LENGTH_SHORT).show();
			final double realMoney = originalMoney - exchangeMoney;
			mTvPaymoney.setText(String.format("￥%s", realMoney));
		} else {
			mLlExchange.setVisibility(View.INVISIBLE);
			mTvPaymoney.setText(String.format("￥%s", originalMoney));
			Toast.makeText(getContext(), "取消兑换" + exchangeMoney, Toast.LENGTH_SHORT).show();
		}
	}


	@OnClick(R2.id.ll_settle_pickaddress)
	public void onViewClickedPick() {
		getSupportDelegate().startForResult(new AddressDelegate(), PICK_ADDRESS);
	}

	@OnClick(R2.id.ll_settle_address)
	public void onViewClickedAddress() {
		getSupportDelegate().startForResult(new AddressDelegate(), PICK_ADDRESS);
	}


	@Override
	public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
		if (requestCode == PICK_ADDRESS && resultCode == RESULT_OK) {
			final MultipleItemEntity addressEntity = (MultipleItemEntity) data.getSerializable("address");
			final String name = addressEntity.getField(AddressItemFields.NAME);
			final String phone = addressEntity.getField(AddressItemFields.PHONE);
			final String address = addressEntity.getField(AddressItemFields.ADDRESS);
			final Boolean isDefault = addressEntity.getField(AddressItemFields.DEFAULT);
			mLlSettlePickAddress.setVisibility(View.GONE);
			mTvAddressName.setText(name);
			mTvAddressPhone.setText(phone);
			mLlSettleAddress.setVisibility(View.VISIBLE);
		}
	}


	@OnClick(R2.id.ll_settle_pick_discountcard)
	public void onViewClickedPickDiscount() {

	}


	@OnClick(R2.id.tv_settle_arrivaltime)
	public void onViewClickedArrivalTime() {
		if(mArrivalTimeDialog != null){
			mArrivalTimeDialog.show();
		}
	}

}
