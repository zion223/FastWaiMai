package com.zrp.latte.ec.main.personal.profile;

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
import android.widget.TextView;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.ui.datepicker.DatePickerDialog;
import com.zrp.latte.ui.datepicker.OnConfirmeListener;

import butterknife.BindView;
import butterknife.OnClick;



public class ProfileDelegate extends BottomItemDelegate implements View.OnClickListener {


	@BindView(R2.id.tv_profile_gender)
	TextView tvProfileGender;
	@BindView(R2.id.tv_profile_birth)
	TextView tvProfileBirth;



	private AlertDialog mGenderDialog;

	@Override
	public Object setLayout() {
		return R.layout.delegate_profile;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
		super.onBindView(savedInstanceState, view);
		mGenderDialog = new AlertDialog.Builder(getContext()).create();
	}


	@OnClick(R2.id.icon_address_edit_return)
	public void onViewClickedReturn(View view) {
		getSupportDelegate().pop();
	}

	@OnClick(R2.id.tv_profile_gender)
	public void onViewClickedGender(View view) {
		mGenderDialog.show();
		final Window window = mGenderDialog.getWindow();
		if (window != null) {
			window.setContentView(R.layout.dialog_profile_sex);
			window.setGravity(Gravity.BOTTOM);
			//设置弹出动画
			window.setWindowAnimations(R.style.anim_choose_up_from_bottom);
			window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			//设置属性
			final WindowManager.LayoutParams params = window.getAttributes();
			params.width = WindowManager.LayoutParams.MATCH_PARENT;
			//FLAG_DIM_BEHIND:窗口之后的内容变暗  FLAG_BLUR_BEHIND: 窗口之后的内容变模糊。
			params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
			window.setAttributes(params);
			window.findViewById(R.id.btn_dialog_profile_male).setOnClickListener(this);
			window.findViewById(R.id.btn_dialog_profile_female).setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_dialog_profile_male) {
			tvProfileGender.setText("帅哥");
			mGenderDialog.dismiss();
		} else if (id == R.id.btn_dialog_profile_female) {
			tvProfileGender.setText("美女");
			mGenderDialog.dismiss();
		}
	}


	@OnClick(R2.id.tv_profile_birth)
	public void onViewClickedBirth() {
		new DatePickerDialog("请选择日期", getContext(), 1991, 2019, new OnConfirmeListener() {
			@Override
			public void result(String date) {
				tvProfileBirth.setText(date);
			}
		}).show();
	}


	@OnClick(R2.id.icon_profile_photo)
	public void onViewClickedChoosePhoto() {

	}
}
