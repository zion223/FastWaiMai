package com.zrp.latte.net.download;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.zrp.latte.app.Latte;
import com.zrp.latte.net.callback.IRequest;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.util.file.FileUtil;

import java.io.File;
import java.io.InputStream;

import okhttp3.ResponseBody;

public class SaveFileTask extends AsyncTask<Object, Void, File> {

    private final IRequest REQUEST;
    private final ISuccess SUCCESS;

    public SaveFileTask(IRequest request, ISuccess success) {
        REQUEST = request;
        SUCCESS = success;
    }


    @Override
    protected File doInBackground(Object... objects) {
        String downloaDir = (String) objects[0];
        String extension = (String) objects[1];
        final ResponseBody body = (ResponseBody) objects[2];
        String name = (String) objects[3];
        final InputStream is = body.byteStream();
        if(downloaDir == null || downloaDir.equals("")){
            downloaDir = "down_loads";
        }
        if(extension == null || extension.equals("")){
            extension = "";
        }
        if(name == null){
            return FileUtil.writeToDisk(is, downloaDir,extension.toUpperCase(),extension);
        }else{
            return FileUtil.writeToDisk(is, downloaDir,name);
        }

    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if(SUCCESS != null){
            SUCCESS.onSuccess(file.getPath());
        }
        if(REQUEST != null){
            REQUEST.onRequestEnd();
        }

    }

    /**
     * 安装下载好的APK文件
     * @param file
     */
    private void autoInstallApk(File file){
        if(FileUtil.getExtension(file.getPath()).equals("apk")){
            final Intent installIntent = new Intent();
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.setAction(Intent.ACTION_VIEW);
            installIntent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
            Latte.getApplication().startActivity(installIntent);

        }
    }

}
