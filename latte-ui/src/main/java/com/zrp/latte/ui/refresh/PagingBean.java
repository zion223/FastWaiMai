package com.zrp.latte.ui.refresh;

/**
 * 分页时使用的Bean数据封装
 */
public final class PagingBean {
	//当前页码
	private int mPageIndex = 0;
	//总数
	private int mTotal = 0;
	//当前数量
	private int mCurrentCount = 0;
	//一页显示几条数据
	private int mPageSize = 0;

	public int getPageSize() {
		return mPageSize;
	}

	public void setPageSize(int mPageSize) {
		this.mPageSize = mPageSize;
	}

	private int mDelayed = 0;

	public int getPageIndex() {
		return mPageIndex;
	}

	public void setPageIndex(int mPageIndex) {
		this.mPageIndex = mPageIndex;
	}

	public int getTotal() {
		return mTotal;
	}

	public void setTotal(int mTotal) {
		this.mTotal = mTotal;
	}

	public int getCurrentCount() {
		return mCurrentCount;
	}

	public void setCurrentCount(int mCurrentCount) {
		this.mCurrentCount = mCurrentCount;
	}

	public int getDelayed() {
		return mDelayed;
	}

	public void setDelayed(int mDelayed) {
		this.mDelayed = mDelayed;
	}

	PagingBean addIndex() {
		mPageIndex++;
		return this;
	}
}
