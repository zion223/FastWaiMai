package com.zrp.latte.ec.main.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;

import com.example.latte.latte_ec.R;
import com.zrp.latte.delegates.LatteDelegate;

public class GoodsDetailDelegate extends LatteDelegate{

	@Override
	public Object setLayout() {
		return R.layout.delegate_goods_detail;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {

	}


	public static GoodsDetailDelegate create(){
		return new GoodsDetailDelegate();
	}

	/**
	 * 添加动画
	 * @param transit
	 * @param enter
	 * @param nextAnim
	 * @return animation
	 */
	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		return super.onCreateAnimation(transit, enter, nextAnim);
	}
}
