package com.zrp.latte.ec.pay;

public interface IAlPayResultListener {
    void onPaySuccess();
    void onPayFailed();
    void onPaying();
    void onPayCancle();
    //支付网络异常
    void onPayConnectError();
}
