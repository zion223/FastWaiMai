package com.zrp.latte.ec.main.sort.content;

/**
 * 图书详情实体类
 */
public class SectionContentItemEntity {

	private int mGoodsId = 0;
	private String mGoodsName = null;
	private String mGoodsThumb = null;

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
}
