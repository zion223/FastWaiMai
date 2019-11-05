package com.example.latte.example;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zrp.latte.app.Latte;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.IError;
import com.zrp.latte.net.callback.IFailure;
import com.zrp.latte.net.callback.ISuccess;

import butterknife.OnClick;

public class ExampleDelegate extends LatteDelegate {



    @Override
    public Object setLayout() {
        return R.layout.delegate_example;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View view) {
        //testClient();

    }

    @OnClick(R.id.btn_test)
    void onlickTest(){
        testClient();


        //PendingIntent pi = PendingIntent.getBroadcast(Latte.getApplication(),0,intent,0);
        //PendingIntent pi = PendingIntent.getActivity(Latte.getApplication(),0,intent,0);
        NotificationManager manager = (NotificationManager) Latte.getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification.Builder(Latte.getApplication())
                .setContentTitle("This is content title")
                .setContentText("This is content text")
                .setSmallIcon(R.drawable.fragmentation_ic_right)
                //.setContentIntent(pi)
                .setAutoCancel(true)
                .build();
       manager.notify(1, notification);
    }

    private void testClient(){
        RestClient.builder()
                .url("http://news.baidu.com/")
                //.url("http://127.0.0.1/index")
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Toast.makeText(getContext(),"onFailure",Toast.LENGTH_LONG).show();
                    }
                })
                .build()
                .get();
    }

}
