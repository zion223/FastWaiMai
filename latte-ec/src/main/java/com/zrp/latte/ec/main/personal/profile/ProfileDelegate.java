package com.zrp.latte.ec.main.personal.profile;

import android.Manifest;
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
import android.widget.Toast;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.joanzapata.iconify.widget.IconTextView;
import com.zrp.latte.app.Latte;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.ui.datepicker.DatePickerDialog;
import com.zrp.latte.ui.datepicker.OnConfirmeListener;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;


public class ProfileDelegate extends BottomItemDelegate implements View.OnClickListener {

	private static final String ARGS_PROFILE_NAME = "ARGS_PROFILE_NAME";
	private static final String ARGS_PROFILE_PHONE = "ARGS_PROFILE_PHONE";
	private static final String ARGS_PROFILE_SEX = "ARGS_PROFILE_SEX";
	private static final String ARGS_PROFILE_BIRTH = "ARGS_PROFILE_BIRTH";
	private static final String ARGS_PROFILE_AVATAR = "ARGS_PROFILE_AVATAR";

	@BindView(R2.id.iv_profile_photo)
	CircleImageView mIvAvatar;
	@BindView(R2.id.tv_profile_nickname)
	TextView mTvNickname;
	@BindView(R2.id.tv_profile_gender)
	TextView mTvGender;
	@BindView(R2.id.tv_profile_birth)
	TextView mTvBirth;
	@BindView(R2.id.tv_profile_phone)
	TextView mTvPhone;
	@BindView(R2.id.icon_profile_photo_edit)
	IconTextView iconProfilePhotoEdit;

	private AlertDialog mGenderDialog;

	@Override
	public Object setLayout() {
		return R.layout.delegate_profile;
	}

	public static ProfileDelegate create(int avatar, String nickName, String sex, String birth, String phone) {
		final Bundle args = new Bundle();
		args.putString(ARGS_PROFILE_NAME, nickName);
		args.putInt(ARGS_PROFILE_AVATAR, avatar);
		args.putString(ARGS_PROFILE_SEX, sex);
		args.putString(ARGS_PROFILE_BIRTH, birth);
		args.putString(ARGS_PROFILE_PHONE, phone);
		final ProfileDelegate delegate = new ProfileDelegate();
		delegate.setArguments(args);
		return delegate;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
		super.onBindView(savedInstanceState, view);
		mGenderDialog = new AlertDialog.Builder(getContext()).create();
		final Bundle profile = getArguments();
		//个人头像
		final int avatar = profile.getInt(ARGS_PROFILE_AVATAR);
		mIvAvatar.setImageResource(avatar);
		//昵称
		final String name = profile.getString(ARGS_PROFILE_NAME);
		mTvNickname.setText(name);
		// 性别
		final String sex = profile.getString(ARGS_PROFILE_SEX);
		mTvGender.setText(sex);
		//生日
		final String birth = profile.getString(ARGS_PROFILE_BIRTH);
		mTvBirth.setText(birth);
		//手机号码
		final String phone = profile.getString(ARGS_PROFILE_PHONE);
		String phoneNumber = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
		mTvPhone.setText(phoneNumber);
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
			mTvGender.setText("帅哥");
			mGenderDialog.dismiss();
		} else if (id == R.id.btn_dialog_profile_female) {
			mTvGender.setText("美女");
			mGenderDialog.dismiss();
		}
	}


	@OnClick(R2.id.tv_profile_birth)
	public void onViewClickedBirth() {
		new DatePickerDialog("请选择日期", getContext(), 1991, 2019, new OnConfirmeListener() {
			@Override
			public void result(String date) {
				mTvBirth.setText(date);
			}
		}).show();
	}


	@OnClick(R2.id.icon_profile_photo_edit)
	public void onViewClickedChoosePhoto() {
		String[] perms = {Manifest.permission.CAMERA};
		//EasyPermission中请求的权限需要在Manifest中申请
		if (EasyPermissions.hasPermissions(Latte.getApplication(), perms)) {
			//
		} else {
			EasyPermissions.requestPermissions(this, "请打开相关权限", 1, perms);
		}
	}


	@OnClick(R2.id.tv_prpfile_edit_save)
	public void onViewClickedSave() {
		Toast.makeText(Latte.getApplication(), "此功能暂未实现",Toast.LENGTH_SHORT).show();
	}
}
