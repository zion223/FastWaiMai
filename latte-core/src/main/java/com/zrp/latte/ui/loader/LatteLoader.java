package com.zrp.latte.ui.loader;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.latte.latte.R;
import com.wang.avi.AVLoadingIndicatorView;
import com.zrp.latte.util.dimen.DimenUtil;

import java.util.ArrayList;

public class LatteLoader {

    public static final int LOADER_SIZE_SCALE = 8;

    public static final int LOADER_OFFSIZE = 10;
    public static final ArrayList<AppCompatDialog> LOADERS = new ArrayList<>();

    public static final String DEFAULT_LOADER = LoaderStyle.BallClipRotatePulseIndicator.name();


    public static void showLoading(Context context,String type){

        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.dialog);
        final AVLoadingIndicatorView avLoadingIndicatorView = LoaderCreator.create(type, context);
        dialog.setContentView(avLoadingIndicatorView);

        int deviceWidth = DimenUtil.getScreenWidth();
        int deviceHeigth = DimenUtil.getScreenHeight();

        final Window dialogWindow = dialog.getWindow();
        if(dialogWindow != null){
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = deviceWidth/LOADER_SIZE_SCALE;
            lp.height = deviceHeigth/LOADER_SIZE_SCALE;
            lp.height = lp.height + LOADER_OFFSIZE;
            lp.gravity = Gravity.CENTER;

        }
        LOADERS.add(dialog);
        dialog.show();
    }
    public static void showLoading(Context context){
        showLoading(context,DEFAULT_LOADER);
    }
    public static void showLoading(Context context,Enum<LoaderStyle> style){
        showLoading(context, style.name());
    }
    public static void stopLoading(){
        for(AppCompatDialog dialog:LOADERS){
            if(dialog != null){
                if(dialog.isShowing()){
                    //cancel可以执行回调
                    dialog.cancel();

                    //dialog.dismiss();
                }
            }
        }
    }



}
