package com.zrp.latte.ui.refresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.latte.ui.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

public class CustomRefreshHeader extends LinearLayout implements RefreshHeader {


	private ImageView mImage;
	private AnimationDrawable pullDownAnim;
	//private AnimationDrawable refreshingAnim;
	private boolean hasSetPullDownAnim = false;

	public CustomRefreshHeader(Context context) {
		this(context, null, 0);
	}

	public CustomRefreshHeader(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomRefreshHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		View view = View.inflate(context, R.layout.widget_custom_refresh_header, this);
		mImage = (ImageView) view.findViewById(R.id.iv_refresh_header);

	}

	@NonNull
	@Override
	public View getView() {
		return this;
	}

	@NonNull
	@Override
	public SpinnerStyle getSpinnerStyle() {
		return SpinnerStyle.FixedBehind;
	}

	@Override
	public void setPrimaryColors(int... colors) {

	}

	@Override
	public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

	}

	@Override
	public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

		// 下拉的百分比小于100%时，不断调用 setScale 方法改变图片大小
		float v = Float.parseFloat("2.5");
		if (percent < 1) {
			mImage.setScaleX(percent * v);
			mImage.setScaleY(percent * v);

			//是否执行过翻跟头动画的标记
			if (hasSetPullDownAnim) {
				hasSetPullDownAnim = false;
			}
		}

		//当下拉的高度达到Header高度100%时，开始加载正在下拉的初始动画，即翻跟头
		if (percent >= 1.0) {
			mImage.setScaleX(percent * v);
			mImage.setScaleY(percent * v);

			//因为这个方法是不停调用的，防止重复
			if (!hasSetPullDownAnim) {
				mImage.setImageResource(R.drawable.anim_pull_end);
				pullDownAnim = (AnimationDrawable) mImage.getDrawable();
				pullDownAnim.setOneShot(false);
				pullDownAnim.start();

				hasSetPullDownAnim = true;
			}
		}
	}

	@Override
	public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

	}

	@Override
	public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

	}

	@Override
	public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
		// 结束动画
		if (pullDownAnim != null && pullDownAnim.isRunning()) {
			pullDownAnim.stop();
		}
//		if (refreshingAnim != null && refreshingAnim.isRunning()) {
//			refreshingAnim.stop();
//		}
		//重置状态
		hasSetPullDownAnim = false;
		return 0;
	}

	@Override
	public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

	}

	@Override
	public boolean isSupportHorizontalDrag() {
		return false;
	}

	@Override
	public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {

		switch (newState){
			case PullDownToRefresh://开始下拉
				mImage.setImageResource(R.drawable.wm_common_img_loading_list_00);
				break;
			case Refreshing: //正在刷新。只调用一次
				//状态切换为正在刷新状态时，设置图片资源为小人卖萌的动画并开始执行
//				//mImage.setImageResource(R.drawable.anim_pull_refreshing);
//				refreshingAnim = (AnimationDrawable) mImage.getDrawable();
//				refreshingAnim.start();
				break;
			case ReleaseToRefresh://松开
				break;
		}
	}

}
