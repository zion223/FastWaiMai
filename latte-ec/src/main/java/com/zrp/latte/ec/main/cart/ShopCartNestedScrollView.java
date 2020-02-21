package com.zrp.latte.ec.main.cart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.latte.latte_ec.R;


public class ShopCartNestedScrollView extends NestedScrollView implements NestedScrollingParent2 {

	private static final String TAG = "ShopCartScrollView";
	private int likeDataHeight;

	public ShopCartNestedScrollView(@NonNull Context context) {
		this(context, null);
	}

	public ShopCartNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}
	/**
	 * 即将开始嵌套滑动，此时嵌套滑动尚未开始，由子控件的 startNestedScroll 方法调用
	 *
	 * @param child  嵌套滑动对应的父类的子类(因为嵌套滑动对于的父控件不一定是一级就能找到的，可能挑了两级父控件的父控件，child的辈分>=target)
	 * @param target 具体嵌套滑动的那个子类
	 * @param axes   嵌套滑动支持的滚动方向
	 * @param type   嵌套滑动的类型，有两种ViewCompat.TYPE_NON_TOUCH fling效果,ViewCompat.TYPE_TOUCH 手势滑动
	 * @return true 表示此父类开始接受嵌套滑动，只有true时候，才会执行下面的 onNestedScrollAccepted 等操作
	 */
	@Override
	public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
		likeDataHeight = child.findViewById(R.id.rv_you_like).getTop();
		Log.e(TAG, "猜你喜欢的RecyclerView高度" + likeDataHeight);
		return true;
	}
	/**
	 * 当onStartNestedScroll返回为true时，也就是父控件接受嵌套滑动时，该方法才会调用
	 *
	 * @param child
	 * @param target
	 * @param axes
	 * @param type
	 */
	@Override
	public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
		//super.onNestedScrollAccepted(child, target, axes);
	}

	@Override
	public void onStopNestedScroll(@NonNull View target, int type) {
		//super.onStopNestedScroll(target);
	}

	/**
	 * 在子控件开始滑动之前，会先调用父控件的此方法，由父控件先消耗一部分滑动距离，并且将消耗的距离存在consumed中，传递给子控件
	 * 在嵌套滑动的子View未滑动之前
	 * ，判断父view是否优先与子view处理(也就是父view可以先消耗，然后给子view消耗）
	 *
	 * @param target   具体嵌套滑动的那个子类
	 * @param dx       水平方向嵌套滑动的子View想要变化的距离
	 * @param dy       垂直方向嵌套滑动的子View想要变化的距离 dy<0向下滑动 dy>0 向上滑动
	 * @param consumed 这个参数要我们在实现这个函数的时候指定，回头告诉子View当前父View消耗的距离
	 *                 consumed[0] 水平消耗的距离，consumed[1] 垂直消耗的距离 好让子view做出相应的调整
	 * @param type     滑动类型，ViewCompat.TYPE_NON_TOUCH fling效果,ViewCompat.TYPE_TOUCH 手势滑动
	 */
	@Override
	public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
		//当滑动距离（dy）大于0，并且当前Y轴小于RecyclerView开始滚动的位置就让NestedScrollView滚动，滑动距离消费不完则由RecyclerView消费
		if (dy > 0 && getScrollY() < likeDataHeight) {
			Log.e(TAG, "由NestedScrollView滑动" + dy);
			scrollBy(0, dy);
			consumed[1] = dy;
		}
	}
	/**
	 * 在 onNestedPreScroll 中，父控件消耗一部分距离之后，剩余的再次给子控件，
	 * 子控件消耗之后，如果还有剩余，则把剩余的再次还给父控件
	 *
	 * @param target       具体嵌套滑动的那个子类
	 * @param dxConsumed   水平方向嵌套滑动的子控件滑动的距离(消耗的距离)
	 * @param dyConsumed   垂直方向嵌套滑动的子控件滑动的距离(消耗的距离)
	 * @param dxUnconsumed 水平方向嵌套滑动的子控件未滑动的距离(未消耗的距离)
	 * @param dyUnconsumed 垂直方向嵌套滑动的子控件未滑动的距离(未消耗的距离)
	 * @param type     滑动类型，ViewCompat.TYPE_NON_TOUCH fling效果,ViewCompat.TYPE_TOUCH 手势滑动
	 */
	@Override
	public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
		super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
	}
}
