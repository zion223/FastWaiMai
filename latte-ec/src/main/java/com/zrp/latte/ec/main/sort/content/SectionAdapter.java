package com.zrp.latte.ec.main.sort.content;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.latte.latte_ec.R;

import java.util.List;

public class SectionAdapter extends BaseSectionQuickAdapter<SectionBean, BaseViewHolder> {

	private static final RequestOptions RECYCLE_OPTIONS =
			new RequestOptions()
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.dontAnimate();
	/**
	 * Same as QuickAdapter#QuickAdapter(Context,int) but with
	 * some initialization data.
	 *
	 * @param layoutResId      The layout resource id of each item.
	 * @param sectionHeadResId The section head layout id for each item
	 * @param data             A new list is created out of this one to avoid mutable list
	 */
	public SectionAdapter(int layoutResId, int sectionHeadResId, List<SectionBean> data) {
		super(layoutResId, sectionHeadResId, data);
	}

	@Override
	protected void convert(BaseViewHolder helper, SectionBean item) {
		final Integer goodsId = item.t.getGoodsId();
		final String goodsName = item.t.getGoodsName();
		//图片url
		final String goodsThumb = item.t.getGoodsThumb();

		//设置Text
		helper.setText(R.id.tv,goodsName);

		final AppCompatImageView imageView = helper.getView(R.id.iv);
		Glide.with(mContext)
				.applyDefaultRequestOptions(RECYCLE_OPTIONS)
				.load(goodsThumb)
				.into(imageView);
	}

	@Override
	protected void convertHead(BaseViewHolder helper, SectionBean item) {
		//设置文字
		helper.setText(R.id.header,item.header);
		//是否可见
		helper.setVisible(R.id.more,item.isMore());
		//点击事件
		helper.addOnClickListener(R.id.more);
	}
}
