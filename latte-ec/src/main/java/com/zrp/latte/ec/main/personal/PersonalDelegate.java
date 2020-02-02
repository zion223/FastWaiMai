package com.zrp.latte.ec.main.personal;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.ec.main.personal.p_function.address.AddressDelegate;
import com.zrp.latte.ec.main.personal.p_capital.discount.DiscountDelegate;
import com.zrp.latte.ec.main.personal.p_personal.profile.ProfileDelegate;
import com.zrp.latte.ec.main.personal.p_personal.setting.SettingDelegate;
import com.zrp.latte.ec.main.personal.p_wallert.borrow.BorrowDelegate;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalDelegate extends BottomItemDelegate {

	@BindView(R2.id.tv_personal_hongbao)
	TextView mTvRedEnvelope;
	@BindView(R2.id.tv_personal_discount)
	TextView mTvDiscount;
	@BindView(R2.id.tv_personal_bounty)
	TextView mTvBounty;
	@BindView(R2.id.tv_personal_payment)
	TextView mTvPayment;
	@BindView(R2.id.tv_personal_benefit)
	TextView mTvBenefit;
	@BindView(R2.id.center_appbar_layout)
	AppBarLayout mAppbarLayout;
	@BindView(R2.id.rl_personal_toolbar_setting)
	LinearLayout mLlToorBarSetting;



	/**
	 * 我的资产
	 * tv_personal_hongbao  0个未使用
	 * tv_personal_discount 12张未使用
	 * tv_personal_bounty 20元可叠加满减
	 * <p>
	 * 我的钱包
	 * tv_personal_payment 28元
	 * tv_personal_benefit 165元
	 * <p>
	 * <p>
	 * 个人(p_personal)
	 *      个人资料(ProfileDelegate)、设置(SettingDelegate) 消息
	 * <p>
	 * 我的资产(p_capital)
	 *      红包、代金券(DiscountDelegate)、津贴
	 * 我的钱包(p_wallert)
	 *      借钱(BorrowDelegate)、买单、享优惠
	 * 我的功能(p_function)
	 *      我的地址  (AddressDelegate)
	 *      我的足迹
	 *      收藏的店
	 *      答谢记录
	 *      我的评价
	 * 我的服务(p_service)
	 *
	 */

	@Override
	public Object setLayout() {
		return R.layout.delegate_personal;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {

		initView();
		final CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) mLlToorBarSetting.getLayoutParams();
		mAppbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
			@Override
			public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
				//TotalScrollRange : 72
				int range = mAppbarLayout.getTotalScrollRange();
				layoutParams.topMargin = Math.abs(verticalOffset) / 2;

				if(verticalOffset != 0&& Math.abs(verticalOffset) != range){
					mLlToorBarSetting.setLayoutParams(layoutParams);
				}
			}
		});
	}

	private void initView() {
		final SpannableString redEnvelope = new SpannableString("0个未使用");
		redEnvelope.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		redEnvelope.setSpan(new AbsoluteSizeSpan(17, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		redEnvelope.setSpan(new AbsoluteSizeSpan(13, true), 1, redEnvelope.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		final SpannableString discount = new SpannableString("12张未使用");
		discount.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		discount.setSpan(new AbsoluteSizeSpan(17, true), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		discount.setSpan(new AbsoluteSizeSpan(13, true), 2, discount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		final SpannableString bounty = new SpannableString("20元可叠加满减");
		bounty.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		bounty.setSpan(new AbsoluteSizeSpan(17, true), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		bounty.setSpan(new AbsoluteSizeSpan(13, true), 2, bounty.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


		mTvRedEnvelope.setText(redEnvelope);
		mTvDiscount.setText(discount);
		mTvBounty.setText(bounty);

		final SpannableString payment = new SpannableString("28元");
		payment.setSpan(new AbsoluteSizeSpan(17, true), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		bounty.setSpan(new AbsoluteSizeSpan(9, true), 2, payment.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		final SpannableString benefit = new SpannableString("165元");
		benefit.setSpan(new AbsoluteSizeSpan(17, true), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		bounty.setSpan(new AbsoluteSizeSpan(9, true), 3, benefit.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		mTvPayment.setText(payment);
		mTvBenefit.setText(benefit);
	}

	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {


	}


	@OnClick(R2.id.tv_personal_name)
	public void onViewClickedEditDetail() {
		getParentDelegate().getSupportDelegate().start(
				ProfileDelegate.create(R.drawable.personal_avatar, "趁微风不燥", "美女", "1997年10月10日", "15078383333"));
	}

	@OnClick(R2.id.rl_personal_address)
	public void onClickEditAddress() {
		getParentDelegate().getSupportDelegate().start(new AddressDelegate());
	}

	@OnClick(R2.id.rl_personal_discount)
	public void onViewClickedDiscount() {
		getParentDelegate().getSupportDelegate().start(new DiscountDelegate());
	}

	@OnClick(R2.id.iv_personal_setting)
	public void onViewClickedSetting() {
		getParentDelegate().getSupportDelegate().start(new SettingDelegate());
	}


	@OnClick(R2.id.rl_personal_borrow)
	public void onViewClickedBorrow() {
		getParentDelegate().getSupportDelegate().start(new BorrowDelegate());
	}
}
