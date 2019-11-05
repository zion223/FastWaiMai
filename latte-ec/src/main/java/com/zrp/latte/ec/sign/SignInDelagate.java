package com.zrp.latte.ec.sign;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.joanzapata.iconify.widget.IconTextView;
import com.zrp.latte.app.Latte;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Login Dealegate
 */
public class SignInDelagate extends LatteDelegate {

    @BindView(R2.id.edit_sign_in_email)
    TextInputEditText email;
    @BindView(R2.id.edit_sign_in_password)
    TextInputEditText password;
    @BindView(R2.id.btn_sign_in)
    AppCompatButton btnSignIn;
    @BindView(R2.id.tv_link_sign_up)
    AppCompatTextView tvLinkSignUp;
    @BindView(R2.id.icon_sign_in_wechat)
    IconTextView iconSignInWechat;
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

    @OnClick(R2.id.btn_sign_in)
    void onClickSignIn() {
        //登录
        RestClient.builder()
                .url("")
                .parmas("", email)
                .parmas("", password)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        SignHandler.onSignIn(response, mISignListener);
                    }
                })
                .build()
                .post();
    }
    @OnClick(R2.id.icon_sign_in_wechat)
    void onClickWeChat() {
        //微信登录
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener) {
            mISignListener = (ISignListener) activity;
        }
    }
}
