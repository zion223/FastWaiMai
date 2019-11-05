package com.zrp.latte.ec.main.index.scaner;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import me.dm7.barcodescanner.core.ViewFinderView;

public class LatteViewFinderView extends ViewFinderView {
	public LatteViewFinderView(Context context) {
		super(context);
	}

	public LatteViewFinderView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		//正方形
		mSquareViewFinder = true;
		//边框的颜色
		mBorderPaint.setColor(Color.YELLOW);
		mLaserPaint.setColor(Color.YELLOW);
	}
}
