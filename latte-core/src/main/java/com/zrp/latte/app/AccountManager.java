package com.zrp.latte.app;

import com.zrp.latte.util.storage.LattePreference;

public class AccountManager {

    private enum SignTag {
        SIGN_TAG
    }

    //保存用户登录状态，登录后调用
    public static void setSignState(boolean state) {
        LattePreference.setAppFlag(SignTag.SIGN_TAG.name(), state);
    }

    public static void checkAccount(IUserChecker checker){
        if(isSignIn()){
            checker.onSignIn();
        }else{
            checker.onNotSignIn();
        }
    }

    private static boolean isSignIn() {
        return LattePreference.getAppFlag(SignTag.SIGN_TAG.name());
    }
}
