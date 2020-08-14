package com.zrp.latte.ec.sign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ec.main.EcBottomDelegate;
import com.zrp.latte.net.RestClient;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class SignInDelagate extends LatteDelegate {

	@BindView(R2.id.edit_sign_in_password)
	EditText password;
	@BindView(R2.id.btn_sign_in)
	AppCompatButton btnSignIn;
	@BindView(R2.id.tv_link_sign_up)
	AppCompatTextView tvLinkSignUp;
	@BindView(R2.id.iv_sign_in_change_password_show)
	ImageView mIvPasswordShowType;
	@BindView(R2.id.tv_sign_in_type)
	TextView mTvSignInType;
	@BindView(R2.id.ll_sign_in_password)
	LinearLayout mLlSignInPassword;
	private ISignListener mISignListener = null;


	@Override
	public Object setLayout() {
		return R.layout.delegate_sign_in;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {

	}

	@OnClick(R2.id.tv_link_sign_up)
	void onClickLink() {
		getSupportDelegate().start(new SignUpDelegate());
	}

	@SuppressLint("CheckResult")
	@OnClick(R2.id.btn_sign_in)
	void onClickSignIn() {
		//登录
		RestClient.builder()
				.url("/api/signin")
				.build()
				.get()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<String>() {
					@Override
					public void accept(String response) throws Exception {
						SignHandler.onSignIn(response, mISignListener);
					}
				});
		getSupportDelegate().startWithPop(new EcBottomDelegate());
	}

	@OnClick(R2.id.iv_sign_in_wechat)
	void onClickWeChat() {
		Toast.makeText(getContext(), "微信登录",Toast.LENGTH_SHORT).show();
	}

	@OnClick(R2.id.iv_sign_in_qq)
	void onClickQQ() {
		Toast.makeText(getContext(), "QQ登录",Toast.LENGTH_SHORT).show();
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof ISignListener) {
			mISignListener = (ISignListener) activity;
		}
	}

	@OnClick(R2.id.iv_sign_in_change_password_show)
	public void onViewClickedChangePasswordShowStatus() {
		if (password.getInputType() == 128) {
			password.setInputType(129);
			mIvPasswordShowType.setImageDrawable(getResources().getDrawable(R.drawable.sign_password_hide));
		} else {
			password.setInputType(128);
			mIvPasswordShowType.setImageDrawable(getResources().getDrawable(R.drawable.sign_password_show));
		}
		password.setSelection(password.getText().length());
	}


	@OnClick(R2.id.tv_sign_in_type)
	public void onViewClickedSignType() {
		//password
		if (mTvSignInType.getText().toString().equals(getResources().getString(R.string.verifycode))) {
			mTvSignInType.setText(getResources().getString(R.string.passwordcode));
			btnSignIn.setText(getResources().getString(R.string.login));
			mLlSignInPassword.setVisibility(View.VISIBLE);
		} else {
			//verify code
			mTvSignInType.setText(getResources().getString(R.string.verifycode));
			btnSignIn.setText(getResources().getString(R.string.queryverifycode));
			mLlSignInPassword.setVisibility(View.GONE);
		}
	}
}
