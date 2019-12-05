package com.zrp.latte.ec.main.index.scaner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ui.camera.CameraRequestCodes;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScannerDelegate extends LatteDelegate implements ZBarScannerView.ResultHandler {

	private ScanView mScanView = null;
	@Override
	public Object setLayout() {
		return mScanView;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("ScannerDelegate", "onCreate");
		if(mScanView == null){
			mScanView = new ScanView(getContext());
		}
		//自动对焦
		mScanView.setAutoFocus(true);
		mScanView.setResultHandler(this);
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {

	}

	@Override
	public void handleResult(Result result) {
		final Bundle bundle = new Bundle();
		bundle.putString("SCAN_RESULT", result.getContents());
		setFragmentResult(CameraRequestCodes.SCAN, bundle);
		Toast.makeText(getContext(),result.toString(),Toast.LENGTH_SHORT).show();
		//扫描器出栈
		getSupportDelegate().pop();


	}

	@Override
	public void onResume() {
		super.onResume();
		if(mScanView != null){
			//恢复
			mScanView.startCamera();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if(mScanView != null){
			//停止预览
			mScanView.stopCameraPreview();
			//停止相机
			mScanView.stopCamera();
		}
	}
}
