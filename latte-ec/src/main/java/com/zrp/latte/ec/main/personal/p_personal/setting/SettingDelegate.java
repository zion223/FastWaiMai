package com.zrp.latte.ec.main.personal.p_personal.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.app.Latte;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.util.storage.DataCleanManager;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingDelegate extends LatteDelegate {

	@BindView(R2.id.tv_setting_cache)
	TextView mTvSettingCache;

	@Override
	public Object setLayout() {
		return R.layout.delegate_setting;
	}


	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view){
		final String totalCacheSize;
		try {
			totalCacheSize = DataCleanManager.getTotalCacheSize(Latte.getApplication());
			mTvSettingCache.setText(String.format("%sMB", totalCacheSize));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);
	}


	@OnClick(R2.id.iv_setting_backarrow)
	public void onViewClickedBack(View view) {
		getSupportDelegate().pop();
	}

	@OnClick(R2.id.ll_setting_clear_cache)
	public void onViewClickedClearCache(View view){
		DataCleanManager.clearAllCache(Latte.getApplication());
		Toast.makeText(getContext(), "缓存已清理",Toast.LENGTH_SHORT).show();
		mTvSettingCache.setText(getResources().getString(R.string.zerocachesize));
	}
}
