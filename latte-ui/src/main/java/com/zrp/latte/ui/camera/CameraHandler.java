package com.zrp.latte.ui.camera;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.example.latte.latte.R;
import com.blankj.utilcode.util.FileUtils;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.util.file.FileUtil;

import java.io.File;

public class CameraHandler implements View.OnClickListener {

	private final AlertDialog DIALOG;
	private final LatteDelegate DELEGATE;

	CameraHandler(LatteDelegate delegate){
		this.DELEGATE = delegate;
		this.DIALOG = new AlertDialog.Builder(DELEGATE.getContext()).create();
	}
	final void beginCameraDialog(){
		DIALOG.show();
		final Window window = DIALOG.getWindow();
		if(window != null){
			window.setGravity(Gravity.BOTTOM);
			window.setContentView(R.layout.dialog_camera_panel);
			window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
			window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

			final WindowManager.LayoutParams params = window.getAttributes();
			params.width = WindowManager.LayoutParams.MATCH_PARENT;
			params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
			params.dimAmount = 0.5f;
			window.setAttributes(params);

			window.findViewById(R.id.photodialog_btn_cancel).setOnClickListener(this);
			window.findViewById(R.id.photodialog_btn_take).setOnClickListener(this);
			window.findViewById(R.id.photodialog_btn_native).setOnClickListener(this);
		}
	}

	private String getPhotoName() {
		return FileUtil.getFileNameByTime("IMG", "jpg");
	}


	//拍摄图片
	private void takePhoto() {
		final String currentPhotoName = getPhotoName();
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		final File tempFile = new File(FileUtil.CAMERA_PHOTO_DIR, currentPhotoName);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			final ContentValues contentValues = new ContentValues(1);
			contentValues.put(MediaStore.Images.Media.DATA, tempFile.getPath());
			final Uri uri = DELEGATE.getContext().getContentResolver().
					insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
			//需要将Uri路径转化为实际路径
			final File realFile =
					FileUtils.getFileByPath(FileUtil.getRealFilePath(DELEGATE.getContext(), uri));
			final Uri realUri = Uri.fromFile(realFile);
			CameraImageBean.getInstance().setPath(realUri);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		} else {
			final Uri fileUri = Uri.fromFile(tempFile);
			CameraImageBean.getInstance().setPath(fileUri);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		}
		DELEGATE.startActivityForResult(intent, CameraRequestCodes.TAKE_PHOTO);

	}

	//本地选取图片
	private void pickPhoto() {
		final Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		DELEGATE.startActivityForResult
				(Intent.createChooser(intent, "选择获取图片的方式"), CameraRequestCodes.PICK_PHOTO);
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if(id == R.id.photodialog_btn_cancel){
			DIALOG.dismiss();
		}else if(id == R.id.photodialog_btn_native){
			pickPhoto();
		}else if(id == R.id.photodialog_btn_take){
			takePhoto();
		}
	}
}
