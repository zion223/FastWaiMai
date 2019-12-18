package com.zrp.latte.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class SplitLine extends View {

	private Paint PAINT = null;
	private Path PATH = null;
	public SplitLine(Context context) {
		super(context);
	}

	public SplitLine(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
		PAINT.setColor(Color.RED);
		PAINT.setStrokeWidth(3);
		final DashPathEffect effect = new DashPathEffect(new float[]{20, 10}, 0);
		PAINT.setPathEffect(effect);
		PATH = new Path();
	}

	public SplitLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


	@Override
	protected void onDraw(Canvas canvas) {
		int centerY = getHeight() / 2;
		setLayerType(LAYER_TYPE_SOFTWARE, null);
		canvas.drawLine(0, centerY, getWidth(), centerY, PAINT);
	}
}
