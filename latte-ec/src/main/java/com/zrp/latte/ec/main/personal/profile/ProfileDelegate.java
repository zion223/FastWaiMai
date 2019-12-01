package com.zrp.latte.ec.main.personal.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.latte.latte_ec.R;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;

public class ProfileDelegate extends BottomItemDelegate {

	@Override
	public Object setLayout() {
		return R.layout.delegate_profile;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
		super.onBindView(savedInstanceState, view);
	}
}
