package com.zrp.latte.ec.pay;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.latte.latte_ec.R;
import com.zrp.latte.delegates.LatteDelegate;

import io.github.mayubao.pay_library.AliPayAPI;
import io.github.mayubao.pay_library.AliPayReq;
import io.github.mayubao.pay_library.PayAPI;

public class FastPay implements View.OnClickListener,AliPayReq.OnAliPayListener {


    private IAlPayResultListener mIAlPayResultListener = null;
//    private Activity mActivity = null;
//
//    private AlertDialog mAlertDialog = null;
//
//    private FastPay(LatteDelegate delegate) {
//        this.mActivity = delegate.getProxyActivity();
//        this.mAlertDialog = new AlertDialog.Builder(delegate.getContext()).create();
//    }
//
//    public static FastPay create(LatteDelegate delegate) {
//        return new FastPay(delegate);
//    }
//
//    public void beginPayDialog() {
//        mAlertDialog.show();
//        final Window window = mAlertDialog.getWindow();
//        if (window != null) {
//            window.setContentView(R.layout.dialog_pay_choose);
//            window.setGravity(Gravity.BOTTOM);
//            //设置弹出动画
//            window.setWindowAnimations(R.style.anim_choose_up_from_bottom);
//            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            //设置属性
//            final WindowManager.LayoutParams params = window.getAttributes();
//            params.width = WindowManager.LayoutParams.MATCH_PARENT;
//            //FLAG_DIM_BEHIND:窗口之后的内容变暗  FLAG_BLUR_BEHIND: 窗口之后的内容变模糊。
//            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//            window.setAttributes(params);
//            window.findViewById(R.id.btn_dialog_pay_alpay).setOnClickListener(this);
//            window.findViewById(R.id.btn_dialog_pay_cancel).setOnClickListener(this);
//            window.findViewById(R.id.btn_dialog_pay_wechat).setOnClickListener(this);
//        }
//
//    }
//
    @Override
    public void onClick(View v) {
//        int i = v.getId();
//        if (i == R.id.btn_dialog_pay_alpay) {
//            //1.创建支付宝支付配置
//            AliPayAPI.Config config = new AliPayAPI.Config.Builder()
//                    .setRsaPrivate("rsa_private") //设置私钥 (商户私钥，pkcs8格式)
//                    .setRsaPublic("rsa_public")//设置公钥(// 支付宝公钥)
//                    .setPartner("partner") //设置商户
//                    .setSeller("seller") //设置商户收款账号
//                    .create();
//
//            //2.创建支付宝支付请求
//            AliPayReq aliPayReq = new AliPayReq.Builder()
//                    .with(mActivity)//Activity实例
//                    .apply(config)//支付宝支付通用配置
//                    .setOutTradeNo("outTradeNo")//设置唯一订单号
//                    .setPrice("price")//设置订单价格
//                    .setSubject("orderSubject")//设置订单标题
//                    .setBody("orderBody")//设置订单内容 订单详情
//                    .setCallbackUrl("callbackUrl")//设置回调地址
//                    .create()//
//                    .setOnAliPayListener(this);//
//
//            //3.发送支付宝支付请求
//            PayAPI.getInstance().sendPayRequest(aliPayReq);
//
//        }else if(i == R.id.btn_dialog_pay_wechat){
//            mAlertDialog.cancel();
//        }else if(i == R.id.btn_dialog_pay_cancel){
//            mAlertDialog.cancel();
//        }
    }

    @Override
    public void onPaySuccess(String resultInfo) {

    }

    @Override
    public void onPayFailure(String resultInfo) {

    }

    @Override
    public void onPayConfirmimg(String resultInfo) {

    }

    @Override
    public void onPayCheck(String status) {

    }
}
