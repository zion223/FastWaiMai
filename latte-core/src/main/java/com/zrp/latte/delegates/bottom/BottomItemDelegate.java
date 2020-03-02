package com.zrp.latte.delegates.bottom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.latte.latte.R;
import com.zrp.latte.delegates.LatteDelegate;

public  abstract class BottomItemDelegate extends LatteDelegate{

    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;
    private static String TAG = "BottomItemDelegate";


    /**
     * 双击退出应用
     * @return boolean
     */
    @Override
    public boolean onBackPressedSupport() {
	    Log.d(TAG, "onBackPressedSupport");
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(_mActivity, "双击退出" + getResources().getString(R.string.app_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
        final View rootview = getView();
        if(rootview != null){
            rootview.setFocusableInTouchMode(true);
            rootview.requestFocus();
            //注册Listenner
            //rootview.setOnKeyListener(this);
        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {

    }
}
