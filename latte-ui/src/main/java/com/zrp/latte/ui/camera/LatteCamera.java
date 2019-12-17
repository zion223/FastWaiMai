package com.zrp.latte.ui.camera;

import android.net.Uri;

import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.util.file.FileUtil;

public class LatteCamera {
	public static Uri createCropFile() {
		return Uri.parse
				(FileUtil.createFile("crop_image",
						FileUtil.getFileNameByTime("IMG", "jpg")).getPath());
	}

	public static void start(LatteDelegate delegate) {
		new CameraHandler(delegate).beginCameraDialog();
	}
}
