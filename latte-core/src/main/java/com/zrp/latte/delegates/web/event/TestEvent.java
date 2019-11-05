package com.zrp.latte.delegates.web.event;

import android.widget.Toast;

public class TestEvent extends Event {

	@Override
	public String execute(String params) {
		Toast.makeText(getContent(),params,Toast.LENGTH_SHORT).show();
		return null;
	}
}
