package com.zrp.latte.delegates.web.chromclient;

import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class WebChromClientImpl extends WebChromeClient {

	@Override
	public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
		return super.onJsAlert(view, url, message, result);
	}
}
