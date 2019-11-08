package com.zrp.latte.ec.main.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;

import com.example.latte.latte_ec.R;
import com.zrp.latte.delegates.LatteDelegate;

public class GoodsDetailDelegate extends LatteDelegate{

	private static final String ARGS_GOODS_ID = "ARGS_GOODS_ID";

	private int mGoodsId = -1;
	public static GoodsDetailDelegate create(int goodsId){
		final Bundle args = new Bundle();
		args.putInt(ARGS_GOODS_ID, goodsId);
		final GoodsDetailDelegate delegate = new GoodsDetailDelegate();
		delegate.setArguments(args);

		return delegate;
	}
	@Override
	public Object setLayout() {
		return R.layout.delegate_goods_detail;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {

	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Bundle args = getArguments();
		if(args != null){
			mGoodsId = args.getInt(ARGS_GOODS_ID);
		}
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
