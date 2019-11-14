package com.zrp.latte.ec.main.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.delegates.web.WebDelegateImpl;

import butterknife.BindView;



public class ShopDetailFragment extends LatteDelegate {

    @BindView(R2.id.ll_detail)
    LinearLayout mLlDetail;
    @BindView(R2.id.scrollView)
    NestedScrollView mScrollView;


    @Override
    public Object setLayout() {
        return R.layout.include_shop_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final WebDelegateImpl webDelegate = WebDelegateImpl.create("index.html");
        webDelegate.setTopDelegate(this.getParentDelegate());
        //loadFragment
        getSupportDelegate().loadRootFragment(R.id.wb_detail_view, webDelegate);
    }



}
