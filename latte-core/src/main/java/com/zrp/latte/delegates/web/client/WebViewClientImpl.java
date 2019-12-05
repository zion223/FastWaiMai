package com.zrp.latte.delegates.web.client;

import android.webkit.WebView;

import com.zrp.latte.delegates.web.WebDelegate;
import com.zrp.latte.delegates.web.route.Router;


public class WebViewClientImpl extends android.webkit.WebViewClient {

	private final WebDelegate DELEGATE;

	public WebViewClientImpl(WebDelegate delegate) {
		DELEGATE = delegate;
	}

	/**
	 * 接管要加载的 URL
	 * @param view
	 * @param url
	 * @return
	 */
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		return Router.getInstance().handleWebUrl(DELEGATE, url);
	}


}
