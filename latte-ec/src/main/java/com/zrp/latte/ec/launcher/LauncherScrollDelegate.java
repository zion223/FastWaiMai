package com.zrp.latte.ec.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.latte.latte_ec.R;
import com.zrp.latte.app.IUserChecker;
import com.zrp.latte.app.AccountManager;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ui.launcher.ILauncherListener;
import com.zrp.latte.ui.launcher.LauncherHolderCreator;
import com.zrp.latte.ui.launcher.OnLauncherFinishTag;
import com.zrp.latte.ui.launcher.ScrollLauncherTag;
import com.zrp.latte.util.storage.LattePreference;

import java.util.ArrayList;

public class LauncherScrollDelegate extends LatteDelegate implements OnItemClickListener {

    private ConvenientBanner<Integer> mConvenientBanner = null;

    private ILauncherListener mILauncherListener = null;
    public static final ArrayList<Integer> INTEGERS = new ArrayList<Integer>();

    @Override
    public Object setLayout() {
       mConvenientBanner = new ConvenientBanner<>(getContext());
       return mConvenientBanner;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        initBanner();
    }

    void initBanner(){
        INTEGERS.add(R.mipmap.launcher_01);
        INTEGERS.add(R.mipmap.launcher_02);
        INTEGERS.add(R.mipmap.launcher_03);
        INTEGERS.add(R.mipmap.launcher_04);
        INTEGERS.add(R.mipmap.launcher_05);
        mConvenientBanner.setPages(new LauncherHolderCreator(),INTEGERS)
                .setPageIndicator(new int[]{R.drawable.dot_normal,R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(this)
                .setCanLoop(false);
    }

    /**
     * @category 点击轮播图时触发
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        if(position == INTEGERS.size()-1){
            //最后一张轮播图
            Log.d("LauncherScrollDelegate","最后一张轮播图");
            //用户已经启动过App了,设置标志位
            LattePreference.setAppFlag(ScrollLauncherTag.HAS_FIRST_LAUNCHER_APP.name(),true);
            //检查用户是否已经登录
            AccountManager.checkAccount(new IUserChecker() {
                @Override
                public void onSignIn() {
                    if (mILauncherListener != null) {
                        mILauncherListener.onLauncherFinish(OnLauncherFinishTag.SIGNED);
                    }
                }

                @Override
                public void onNotSignIn() {
                    if (mILauncherListener != null) {
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
