package com.zrp.latte.app;

public interface IUserChecker {

    //用户已登录
    void onSignIn();
    //用户未登录
    void onNotSignIn();
}
