package com.zrp.latte.ec.main.personal.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.latte.latte_ec.R;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;

public class OrderDelegate extends BottomItemDelegate {



    //TODO TAB切换不同状态的订单

    @Override
    public Object setLayout() {
        return R.layout.delegate_personal_order;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        super.onBindView(savedInstanceState, view);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

    }
}
