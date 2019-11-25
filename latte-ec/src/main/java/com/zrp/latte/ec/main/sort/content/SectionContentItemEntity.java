package com.zrp.latte.ec.main.sort.content;


public class SectionContentItemEntity {

	private int mGoodsId = 0;
	private String mGoodsName = null;
	private String mGoodsThumb = null;

	private String mGoodsSpec = null;
	private String mGoodsPrice = null;
	private String mGoodsOriginPrice = null;

	public int getGoodsId() {
		return mGoodsId;
	}

	public void setGoodsId(int mGoodsId) {
		this.mGoodsId = mGoodsId;
	}

	public String getGoodsName() {
		return mGoodsName;
	}

	public void setGoodsName(String mGoodsName) {
		this.mGoodsName = mGoodsName;
	}

	public String getGoodsThumb() {
		return mGoodsThumb;
	}

	public void setGoodsThumb(String mGoodsThumb) {
		this.mGoodsThumb = mGoodsThumb;
	}

	public String getGoodsDetail() {
		return mGoodsSpec;
	}

	public void setGoodsDetail(String mGoodsDetail) {
		this.mGoodsSpec = mGoodsDetail;
	}

	public String getGoodsPrice() {
		return mGoodsPrice;
	}

	public void setGoodsPrice(String mGoodsPrice) {
		this.mGoodsPrice = mGoodsPrice;
	}

	public String getGoodsOldPrice() {
		return mGoodsOriginPrice;
	}

	public void setGoodsOldPrice(String mGoodsOldPrice) {
		this.mGoodsOriginPrice = mGoodsOldPrice;
	}
}
