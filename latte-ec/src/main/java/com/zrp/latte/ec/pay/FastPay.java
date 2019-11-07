package com.zrp.latte.ec.pay;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.latte.latte_ec.R;
import com.joanzapata.iconify.widget.IconTextView;
import com.zrp.latte.delegates.LatteDelegate;

import butterknife.BindView;
import butterknife.OnClick;

public class FastPay implements View.OnClickListener {


    private IAlPayResultListener mIAlPayResultListener = null;
    private Activity mActivity = null;

    private AlertDialog mAlertDialog = null;

    private FastPay(LatteDelegate delegate) {
        this.mActivity = delegate.getProxyActivity();
        this.mAlertDialog = new AlertDialog.Builder(delegate.getContext()).create();
    }

    public static FastPay create(LatteDelegate delegate) {
        return new FastPay(delegate);
    }

    public void beginPayDialog() {
        mAlertDialog.show();
        final Window window = mAlertDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_pay_choose);
            window.setGravity(Gravity.BOTTOM);
            //设置弹出动画
            window.setWindowAnimations(R.style.anim_choose_up_from_bottom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            //FLAG_DIM_BEHIND:窗口之后的内容变暗  FLAG_BLUR_BEHIND: 窗口之后的内容变模糊。
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(params);
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_dialog_pay_alpay) {
            
        }else if(i == R.id.btn_dialog_pay_wechat){
            mAlertDialog.cancel();
        }else if(i == R.id.btn_dialog_pay_cancel){
            mAlertDialog.cancel();
        }
    }

}
