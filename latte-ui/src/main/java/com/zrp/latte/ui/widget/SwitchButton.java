package com.zrp.latte.ui.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


public class SwitchButton extends android.support.v7.widget.AppCompatCheckBox {
	/**
	 * 控件默认宽度
	 */
	private static final int DEFAULT_WIDTH = 200;
	/**
	 * 控件默认高度
	 */
	private static final int DEFAULT_HEIGHT = DEFAULT_WIDTH / 8 * 5;
	/**
	 * 画笔
	 */
	private Paint mPaint;
	/**
	 * 控件背景的矩形范围
	 */
	private RectF mRectF;
	/**
	 * 开关指示器按钮圆心 X 坐标的偏移量
	 */
	private float mButtonCenterXOffset;
	/**
	 * 颜色渐变系数
	 */
	private float mColorGradientFactor = 1;
	/**
	 * 状态切换时的动画时长
	 */
	private long mAnimateDuration = 300L;
	/**
	 * 开关未选中状态,即关闭状态时的背景颜色
	 */
	private int mBackgroundColorUnchecked = 0xFFCCCCCC;
	/**
	 * 开关选中状态,即打开状态时的背景颜色
	 */
	private int mBackgroundColorChecked = 0xFF6495ED;
	/**
	 * 开关指示器按钮的颜色
	 */
	private int mButtonColor = 0xFFFFFFFF;

	public SwitchButton(Context context) {
		this(context, null);
	}

	public SwitchButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// 不显示 CheckBox 默认的 Button
		setButtonDrawable(null);
		// 不显示 CheckBox 默认的背景
		setBackgroundResource(0);
		// 默认 CheckBox 为关闭状态
		setChecked(false);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);

		mRectF = new RectF();
		// 点击时开始动画
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startAnimate();
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int width;
		int height;
		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			width = (getPaddingLeft() + DEFAULT_WIDTH + getPaddingRight());
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = (getPaddingTop() + DEFAULT_HEIGHT + getPaddingBottom());
		}
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 设置画笔宽度为控件宽度的 1/40,准备绘制控件背景
		mPaint.setStrokeWidth((float) getMeasuredWidth() / 40);
		// 根据是否选中的状态设置画笔颜色
		if (isChecked()) {
			// 选中状态时,背景颜色由未选中状态的背景颜色逐渐过渡到选中状态的背景颜色
			mPaint.setColor(getCurrentColor(mColorGradientFactor, mBackgroundColorUnchecked, mBackgroundColorChecked));
		} else {
			// 未选中状态时,背景颜色由选中状态的背景颜色逐渐过渡到未选中状态的背景颜色
			mPaint.setColor(getCurrentColor(mColorGradientFactor, mBackgroundColorChecked, mBackgroundColorUnchecked));
		}
		// 设置背景的矩形范围
		mRectF.set(mPaint.getStrokeWidth()
				, mPaint.getStrokeWidth()
				, getMeasuredWidth() - mPaint.getStrokeWidth()
				, getMeasuredHeight() - mPaint.getStrokeWidth());
		// 绘制圆角矩形作为背景
		canvas.drawRoundRect(mRectF, getMeasuredHeight(), getMeasuredHeight(), mPaint);

		// 设置画笔颜色,准备绘制开关按钮指示器
		mPaint.setColor(mButtonColor);
		/*
		 * 获取开关按钮指示器的半径
		 * 为了好看一点,开关按钮指示器在背景矩形中显示一点内边距,所以多减去两个画笔宽度
		 */
		float radius = (getMeasuredHeight() - mPaint.getStrokeWidth() * 4) / 2;
		float x;
		float y;
		// 根据是否选中的状态来决定开关按钮指示器圆心的 X 坐标
		if (isChecked()) {
//            // 选中状态时开关按钮指示器在右边
//            x = getMeasuredWidth() - radius - mPaint.getStrokeWidth() - mPaint.getStrokeWidth();
			// 选中状态时开关按钮指示器圆心的 X 坐标从左边逐渐移到右边
			x = getMeasuredWidth() - radius - mPaint.getStrokeWidth() - mPaint.getStrokeWidth() - mButtonCenterXOffset;
		} else {
//            // 未选中状态时开关按钮指示器在左边
//            x = radius + mPaint.getStrokeWidth() + mPaint.getStrokeWidth();
			// 未选中状态时开关按钮指示器圆心的 X 坐标从右边逐渐移到左边
			x = radius + mPaint.getStrokeWidth() + mPaint.getStrokeWidth() + mButtonCenterXOffset;
		}
		// Y 坐标就是控件高度的一半不变
		y = (float) getMeasuredHeight() / 2;
		canvas.drawCircle(x, y, radius, mPaint);
	}

	/**
	 * Description:开始开关按钮切换状态和背景颜色过渡的动画
	 */
	private void startAnimate() {
		// 计算开关指示器的半径
		float radius = (getMeasuredHeight() - mPaint.getStrokeWidth() * 4) / 2;
		// 计算开关指示器的 X 坐标的总偏移量
		float centerXOffset = getMeasuredWidth() - mPaint.getStrokeWidth() - mPaint.getStrokeWidth() - radius
				- (mPaint.getStrokeWidth() + mPaint.getStrokeWidth() + radius);

		AnimatorSet animatorSet = new AnimatorSet();
		// 偏移量逐渐变化到 0
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "buttonCenterXOffset", centerXOffset, 0);
		objectAnimator.setDuration(mAnimateDuration);
		objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				invalidate();
			}
		});

		// 背景颜色过渡系数逐渐变化到 1
		ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(this, "colorGradientFactor", 0, 1);
		objectAnimator2.setDuration(mAnimateDuration);

		// 同时开始修改开关指示器 X 坐标偏移量的动画和修改背景颜色过渡系数的动画
		animatorSet.play(objectAnimator).with(objectAnimator2);
		animatorSet.start();
	}

	/**
	 * @param fraction   当前渐变系数
	 * @param startColor 过渡开始颜色
	 * @param endColor   过渡结束颜色
	 * @return 当前颜色
	 */
	private int getCurrentColor(float fraction, int startColor, int endColor) {
		int redStart = Color.red(startColor);
		int blueStart = Color.blue(startColor);
		int greenStart = Color.green(startColor);
		int alphaStart = Color.alpha(startColor);

		int redEnd = Color.red(endColor);
		int blueEnd = Color.blue(endColor);
		int greenEnd = Color.green(endColor);
		int alphaEnd = Color.alpha(endColor);

		int redDifference = redEnd - redStart;
		int blueDifference = blueEnd - blueStart;
		int greenDifference = greenEnd - greenStart;
		int alphaDifference = alphaEnd - alphaStart;

		int redCurrent = (int) (redStart + fraction * redDifference);
		int blueCurrent = (int) (blueStart + fraction * blueDifference);
		int greenCurrent = (int) (greenStart + fraction * greenDifference);
		int alphaCurrent = (int) (alphaStart + fraction * alphaDifference);

		return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
	}

	public void setButtonCenterXOffset(float buttonCenterXOffset) {
		mButtonCenterXOffset = buttonCenterXOffset;
	}

	public void setColorGradientFactor(float colorGradientFactor) {
		mColorGradientFactor = colorGradientFactor;
	}

	public void setAnimateDuration(long animateDuration) {
		mAnimateDuration = animateDuration;
	}

	public void setBackgroundColorUnchecked(int backgroundColorUnchecked) {
		mBackgroundColorUnchecked = backgroundColorUnchecked;
	}

	public void setBackgroundColorChecked(int backgroundColorChecked) {
		mBackgroundColorChecked = backgroundColorChecked;
	}

	public void setButtonColor(int buttonColor) {
		mButtonColor = buttonColor;
	}
}