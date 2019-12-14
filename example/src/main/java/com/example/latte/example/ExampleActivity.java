package com.example.latte.example;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.zrp.latte.activities.ProxyActivity;
import com.zrp.latte.app.Latte;
import com.zrp.latte.delegates.LatteDelegate;

import com.zrp.latte.ec.main.EcBottomDelegate;
import com.zrp.latte.ec.sign.ISignListener;
import com.zrp.latte.ec.sign.SignInDelagate;
import com.zrp.latte.ui.launcher.ILauncherListener;
import com.zrp.latte.ui.launcher.OnLauncherFinishTag;

import qiu.niorgai.StatusBarCompat;


public class ExampleActivity extends ProxyActivity implements ISignListener, ILauncherListener {

    @Override
    public LatteDelegate setRootDelegate() {
        return new EcBottomDelegate();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Latte.getConfigurator().withActivity(this);
        StatusBarCompat.translucentStatusBar(this, true);
    }

    @Override
    public void onSignInSuccess() {
        Toast.makeText(Latte.getApplication(),"登陆成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignUpSuccess() {
        Toast.makeText(Latte.getApplication(),"注册成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLauncherFinish(OnLauncherFinishTag tag) {
        switch (tag){
            case SIGNED:
                Toast.makeText(this, "启动结束用户登陆了", Toast.LENGTH_SHORT).show();
                //进入首页
                //getSupportDelegate().startWithPop(new EcBottomDelegate());
                break;
            case NOT_SIGNED:
                //跳去登录页
                Toast.makeText(this, "启动结束用户未登录", Toast.LENGTH_SHORT).show();
                getSupportDelegate().start(new SignInDelagate());
                break;
            default:
                break;
        }

    }
}
