package com.zrp.latte.ec.sign;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.zrp.latte.app.AccountManager;

public class SignHandler {

    //注册Handler
    public static void onSignUp(String response, ISignListener signListener){
        final JSONObject profileJson = JSON.parseObject(response).getJSONObject("data");
        final String username = profileJson.getString("username");
        //数据库插入操作


        AccountManager.setSignState(true);
        //注册成功
        if(signListener != null){
            signListener.onSignUpSuccess();
        }

    }
    //登录Handler
    public static void onSignIn(String response, ISignListener signListener){

        final JSONObject profileJson = JSON.parseObject(response).getJSONObject("data");

        AccountManager.setSignState(true);
        //登录成功
        if(signListener != null){
            signListener.onSignInSuccess();
        }
    }
}
