package com.zrp.latte.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.latte.ui.R;

public class DiscountDivideLine extends View {

	//分割线默认颜色
	private final int DIVIDELINE_DEFAULT_COLOR = 0xFF999999;
	//两端半圆默认颜色
	private final int PORTSHAPE_DEFAULT_COLOR = 0xFFE6E6E6;
	//两端半圆默认高度(直径)
	private final int PORTSHAPE_DEFAULT_DIAMETER = dp2px(20);
	//分割线默认长度
	private final int DIVIDELINE_DEFAULT_LENGTH = dp2px(3);
	//分割线默认间隔长度
	private final int DIVIDELINE_DEFAULT_INTERVAL = dp2px(3);
	//分割线默认方向orientation
	private final int DIVIDELINE_DEFAULT_ORIENTATION = 1;

	//分割线颜色
	private int mDivideLineColor;
	//两端半圆颜色
	private int mPortShapeColor;
	//两端半圆高度(直径)
	private int mPortShapeDiameter;
	//两端半圆半径
	private int mPortShapeRadius;
	//分割线长度
	private int mDivideLineLength;
	//分割线间隔长度
	private int mDivideLineInterval;
	//分割线方向
	private int mDivideLineOrientation;

	private Paint mPaint;

	public DiscountDivideLine(Context context) {
		this(context, null);
	}

	public DiscountDivideLine(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DiscountDivideLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DiscountDivideLine);
		mDivideLineColor = ta.getColor(R.styleable.DiscountDivideLine_divid_line_color, DIVIDELINE_DEFAULT_COLOR);
		mPortShapeColor = ta.getColor(R.styleable.DiscountDivideLine_port_shape_color, PORTSHAPE_DEFAULT_COLOR);
		mPortShapeDiameter = (int) ta.getDimension(R.styleable.DiscountDivideLine_port_shape_diameter, PORTSHAPE_DEFAULT_DIAMETER);
		mDivideLineLength = (int) ta.getDimension(R.styleable.DiscountDivideLine_divide_line_length, DIVIDELINE_DEFAULT_LENGTH);
		mDivideLineInterval = (int) ta.getDimension(R.styleable.DiscountDivideLine_divide_line_interval, DIVIDELINE_DEFAULT_INTERVAL);
		mDivideLineOrientation = ta.getInt(R.styleable.DiscountDivideLine_line_orientation, DIVIDELINE_DEFAULT_ORIENTATION);
		mPortShapeRadius = mPortShapeDiameter / 2;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(dp2px(1));
		ta.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthVal = measureWidth(widthMeasureSpec);
		int heightVal = measureHeight(heightMeasureSpec);
		setMeasuredDimension(widthVal, heightVal);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(mPortShapeColor);
		if (mDivideLineOrientation == 1) {
			//画左端半圆,半圆是通过一个正方矩形构造出来的，画的时候需要画布移动半个矩形画出来才没有类似padding的空白
			RectF rectF = new RectF(0, 0, mPortShapeDiameter, mPortShapeDiameter);
			canvas.translate(-mPortShapeRadius, getMeasuredHeight() / 2 - mPortShapeRadius);
			canvas.drawArc(rectF, 270, 180, true, mPaint);//从270度开始画，画180度圆弧。
			canvas.restore();
			//画虚线
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setColor(mDivideLineColor);
			DashPathEffect effect = new DashPathEffect(new float[]{mDivideLineLength, mDivideLineInterval}, 0);
			mPaint.setPathEffect(effect);
			Path path = new Path();
			path.moveTo(mPortShapeRadius + 3, getMeasuredHeight() / 2);
			path.lineTo(getMeasuredWidth() - mPortShapeRadius - 3, getMeasuredHeight() / 2);
			//虚线两端偏移3个像素
			canvas.drawPath(path, mPaint);
			//画右端半圆,,半圆是通过一个正方矩形构造出来的，画的时候需要画布移动半个矩形画出来才没有类似padding的空白
			mPaint.setStyle(Paint.Style.FILL);
			mPaint.setColor(mPortShapeColor);
			rectF = new RectF(getMeasuredWidth() - mPortShapeDiameter, 0, getMeasuredWidth(), mPortShapeDiameter);
			canvas.translate(mPortShapeRadius, getMeasuredHeight() / 2 - mPortShapeRadius);
			canvas.drawArc(rectF, 90, 180, true, mPaint);//从90度开始画，画180度圆弧。
		} else {
			//画左端半圆,半圆是通过一个正方矩形构造出来的，画的时候需要画布移动半个矩形画出来才没有类似padding的空白
			RectF rectF = new RectF(0, 0, mPortShapeDiameter, mPortShapeDiameter);
			canvas.translate(getMeasuredWidth() / 2 - mPortShapeRadius, -mPortShapeRadius);
			canvas.drawArc(rectF, 0, 180, true, mPaint);//从0度开始画，画180度圆弧。
			canvas.restore();
			//画虚线
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setColor(mDivideLineColor);
			DashPathEffect effect = new DashPathEffect(new float[]{mDivideLineLength, mDivideLineInterval}, 0);
			mPaint.setPathEffect(effect);
			Path path = new Path();
			path.moveTo(getMeasuredWidth() / 2, mPortShapeRadius + 3);
			path.lineTo(getMeasuredWidth() / 2, getMeasuredHeight() - mPortShapeRadius - 3);
			//虚线两端偏移3个像素
			canvas.drawPath(path, mPaint);
			//画右端半圆,,半圆是通过一个正方矩形构造出来的，画的时候需要画布移动半个矩形画出来才没有类似padding的空白
			mPaint.setStyle(Paint.Style.FILL);
			mPaint.setColor(mPortShapeColor);
			rectF = new RectF(0, 0, mPortShapeDiameter, mPortShapeDiameter);
			canvas.translate(getMeasuredWidth() / 2 - mPortShapeRadius, getMeasuredHeight() - mPortShapeRadius);
			canvas.drawArc(rectF, 180, 180, true, mPaint);//从180度开始画，画180度圆弧。
		}
	}

	/**
	 * 测量控件宽度
	 *
	 * @param widthMeasureSpec
	 * @return
	 */
	private int measureWidth(int widthMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		int result = 0;
		if (mode == MeasureSpec.EXACTLY) {
			result = width;
		} else if (mode == MeasureSpec.AT_MOST) {
			result = getPaddingLeft() + getPaddingRight() + mPortShapeDiameter;
		}
		return result;
	}

	/**
	 * 测量控件高度
	 *
	 * @param heightMeasureSpec
	 * @return
	 */
	private int measureHeight(int heightMeasureSpec) {
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int mode = MeasureSpec.getMode(heightMeasureSpec);
		int result = 0;
		if (mode == MeasureSpec.EXACTLY) {
			result = height;
		} else if (mode == MeasureSpec.AT_MOST) {
			result = getPaddingTop() + getPaddingBottom() + mPortShapeDiameter;
		}
		return result;
	}

	/**
	 * dp 转 px
	 *
	 * @param dp
	 * @return
	 */
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}

}
