package com.zrp.latte.ec.main.sort.content;


import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.List;

public class SectionBean {

	private List<String> headers;

	private List<List<MultipleItemEntity>> datas;

	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public List<List<MultipleItemEntity>> getDatas() {
		return datas;
	}

	public void setDatas(List<List<MultipleItemEntity>> datas) {
		this.datas = datas;
	}
}
