package com.example.latte.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.zrp.latte.delegates.LatteDelegate;

public class ExampleDelegate extends LatteDelegate {


	@Override
	public Object setLayout() {
		return R.layout.delegate_example;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) throws Exception {

	}
}
