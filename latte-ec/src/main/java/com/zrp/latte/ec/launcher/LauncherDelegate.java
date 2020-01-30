package com.zrp.latte.ec.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.app.AccountManager;
import com.zrp.latte.app.IUserChecker;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ui.launcher.ILauncherListener;
import com.zrp.latte.ui.launcher.OnLauncherFinishTag;
import com.zrp.latte.ui.launcher.ScrollLauncherTag;
import com.zrp.latte.util.storage.LattePreference;
import com.zrp.latte.util.timer.BaseTimeTask;
import com.zrp.latte.util.timer.ITimeListener;

import java.text.MessageFormat;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;


public class LauncherDelegate extends LatteDelegate implements ITimeListener {


    @Nullable
    @BindView(R2.id.tv_launcher_timer1)
    AppCompatTextView mTvTimer = null;

    private ILauncherListener mILauncherListener = null;

    private Timer mTimer = null;
    private int mCount = 5;

    @Optional
    @OnClick(R2.id.tv_launcher_timer1)
    void onClickTimerView(){
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
            checkIsShowScroll();
        }

    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_launcher;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        initTimer();
    }

    @Override
    public void onTime() {
        getProxyActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mTvTimer != null){
                    mTvTimer.setText(MessageFormat.format("跳过\n{0}s",mCount));
                    mCount--;
                    if(mCount < 0){
                        mTimer.cancel();
                        mTimer = null;
                        checkIsShowScroll();
                    }
                }
            }
        });
    }
    private void initTimer(){
        mTimer = new Timer();
        final BaseTimeTask timeTask = new BaseTimeTask(this);
        mTimer.schedule(timeTask,0,1000);
    }

    /**
     * 是否需要显示滚动界面
     */
    private void checkIsShowScroll(){
        //是否第一次启动App
        if(!LattePreference.getAppFlag(ScrollLauncherTag.HAS_FIRST_LAUNCHER_APP.name())){
            getSupportDelegate().start(new LauncherScrollDelegate(), SINGLETASK);
        }else {
            //检查用户是否登录
            AccountManager.checkAccount(new IUserChecker() {
                //用户登陆了
                @Override
                public void onSignIn() {
                    if(mILauncherListener != null){
                        mILauncherListener.onLauncherFinish(OnLauncherFinishTag.SIGNED);
                    }
                }
                //用户没有登录
                @Override
                public void onNotSignIn() {
                    if(mILauncherListener != null){
                        mILauncherListener.onLauncherFinish(OnLauncherFinishTag.NOT_SIGNED);
                    }
                }
            });
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof ILauncherListener){
            mILauncherListener = (ILauncherListener) activity;
        }
    }
}
