package com.zrp.latte.ec.main.detail;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.ycbjie.slide.SlideLayout;
import com.ycbjie.slide.VerticalScrollView;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;

import butterknife.BindView;

public class GoodsDetailDelegate extends LatteDelegate{


	@BindView(R2.id.slideDetailsLayout)
	SlideLayout mSlideDetailsLayout;
	@BindView(R2.id.tv_goods_title)
	TextView mTvGoodsTitle;
	@BindView(R2.id.tv_new_price)
	TextView mTvNewPrice;
	@BindView(R2.id.tv_old_price)
	TextView mTvOldPrice;
	@BindView(R2.id.tv_current_goods)
	TextView mTvCurrentGoods;
	@BindView(R2.id.ll_current_goods)
	LinearLayout mLlCurrentGoods;
	@BindView(R2.id.iv_ensure)
	ImageView mIvEnsure;
	@BindView(R2.id.tv_comment_count)
	TextView mTvCommentCount;
	@BindView(R2.id.tv_good_comment)
	TextView mTvGoodComment;
	@BindView(R2.id.iv_comment_right)
	ImageView mIvCommentRight;
	@BindView(R2.id.ll_comment)
	LinearLayout mLlComment;
	@BindView(R2.id.ll_empty_comment)
	LinearLayout mLlEmptyComment;
	@BindView(R2.id.ll_recommend)
	LinearLayout mLlRecommend;
	@BindView(R2.id.tv_bottom_view)
	TextView mTvBottomView;
	@BindView(R2.id.scrollView)
	VerticalScrollView mScrollView;

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
		initSlideDetailsLayout();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Bundle args = getArguments();
		if(args != null){
			mGoodsId = args.getInt(ARGS_GOODS_ID);
		}
	}

	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);
        mTvGoodsTitle.setText("这是第"+ mGoodsId+"件商品");
		RestClient.builder()
				.url("")
				.parmas("goodsId",mGoodsId)
				.success(new ISuccess() {
					@Override
					public void onSuccess(String response) {
						//绑定数据

					}
				})
				.build()
				.get();
	}


	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		return super.onCreateAnimation(transit, enter, nextAnim);
	}
	private void initSlideDetailsLayout() {
		mSlideDetailsLayout.setOnSlideDetailsListener(new SlideLayout.OnSlideDetailsListener() {
			@Override
			public void onStatusChanged(SlideLayout.Status status) {
				if (status == SlideLayout.Status.OPEN) {
					//当前为图文详情页
					mTvBottomView.setText("下拉回到商品详情");
				} else {
					//当前为商品详情页
					mTvBottomView.setText("继续上拉，查看图文详情");
				}
			}
		});
	}

}
