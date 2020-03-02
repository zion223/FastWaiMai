package com.zrp.latte.delegates.bottom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.latte.latte.R;
import com.zrp.latte.delegates.LatteDelegate;

public  abstract class BottomItemDelegate extends LatteDelegate implements View.OnKeyListener{

	private long firstTime = 0;

	/**
	 * 双击两次返回键退出App
	 * @param view
	 * @param keyCode
	 * @param event
	 * @return boolean
	 */
    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if(System.currentTimeMillis() - firstTime > 2000){
                Toast.makeText(_mActivity, "再按一次退出"+getResources().getString(R.string.app_name), Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
                return true;
            }else {
                _mActivity.finish();
                if(firstTime != 0){
                    firstTime = 0;
                }
            }
        }
        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        final View rootview = getView();
        if(rootview != null){
            rootview.setFocusableInTouchMode(true);
            rootview.requestFocus();
            //注册Listenner
            rootview.setOnKeyListener(this);
        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {

    }
}
