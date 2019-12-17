package com.zrp.latte.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


public class CircleTextView extends AppCompatTextView {

    private Paint PAINT = null;
    private PaintFlagsDrawFilter FILTER = null;

    public CircleTextView(Context context) {
        super(context);
    }
    public CircleTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        PAINT = new Paint();
        FILTER = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        PAINT.setColor(Color.RED);
        PAINT.setAntiAlias(true);
    }

    public final void setCircleBackground(@ColorInt int color){
        PAINT.setColor(color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = getMeasuredWidth();
        final int height = getHeight();
        final int max = Math.max(width, height);
        setMeasuredDimension(max, max);
    }

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

    @Override
    public void draw(Canvas canvas) {
        canvas.setDrawFilter(FILTER);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2,
                Math.max(getWidth(), getHeight()) / 2, PAINT);
        super.draw(canvas);
    }


}
