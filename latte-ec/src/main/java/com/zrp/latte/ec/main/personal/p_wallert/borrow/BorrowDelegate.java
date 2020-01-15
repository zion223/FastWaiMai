package com.zrp.latte.ec.main.personal.p_wallert.borrow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.LatteDelegate;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BorrowDelegate extends LatteDelegate {

	Unbinder unbinder;

	@Override
	public Object setLayout() {
		return R.layout.delegate_borrow;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) throws Exception {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO: inflate a fragment view
		View rootView = super.onCreateView(inflater, container, savedInstanceState);
		unbinder = ButterKnife.bind(this, rootView);
		return rootView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	@OnClick(R2.id.iv_discount_backarrow)
	public void onViewClicked() {
		getSupportDelegate().pop();
	}
}
