package com.zrp.latte.util.timer;

import java.util.TimerTask;

public class BaseTimeTask extends TimerTask {

    private ITimeListener listener = null;
    public BaseTimeTask(ITimeListener timeListener){
        this.listener = timeListener;
    }

    @Override
    public void run() {
        if(listener != null){
            listener.onTime();
        }
    }
}
