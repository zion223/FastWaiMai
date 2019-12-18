package com.zrp.latte.ec.main.cart.settle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.latte.latte_ec.R;
import com.zrp.latte.delegates.LatteDelegate;

public class SettleDelegate extends LatteDelegate {

	@Override
	public Object setLayout() {
		return R.layout.delegate_settlement;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {

	}
}
