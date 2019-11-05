package com.zrp.latte.ec.main.index.scaner;

import android.content.Context;
import android.util.AttributeSet;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScanView extends ZBarScannerView {
	public ScanView(Context context) {
		super(context);
	}

	public ScanView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	@Override
	protected IViewFinder createViewFinderView(Context context) {
		return new LatteViewFinderView(context);

	}
}
