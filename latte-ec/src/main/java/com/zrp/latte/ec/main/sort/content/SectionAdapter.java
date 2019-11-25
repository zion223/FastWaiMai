package com.zrp.latte.ec.main.sort.content;

import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.latte.latte_ec.R;
import com.zrp.latte.app.Latte;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
		//图片url
		final String goodsThumb = item.t.getGoodsThumb();
		//final Integer goodsId = item.t.getGoodsId();
		final String goodsName = item.t.getGoodsName();
		final String goodsDetail = item.t.getGoodsDetail();
		final String originPrice = item.t.getGoodsOldPrice();

		final String price= item.t.getGoodsPrice();


		//设置Text
		helper.setText(R.id.tv_goods_name, goodsName);
		helper.setText(R.id.tv_goods_detail, goodsDetail);
		helper.setText(R.id.tv_price, "￥" + price);
		final TextView originPriceView = helper.getView(R.id.tv_origin_price);
		//添加下划线
		originPriceView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		helper.setText(R.id.tv_origin_price, "￥" + originPrice);
		//商品图片
		final AppCompatImageView imageView = helper.getView(R.id.iv);
		Glide.with(mContext)
				.applyDefaultRequestOptions(RECYCLE_OPTIONS)
				.load(goodsThumb)
				.into(imageView);
		//加入购物车图标
		final ImageView shopCartImageView = helper.getView(R.id.iv_add_shopcart);
		shopCartImageView.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//添加到购物车
				Toast.makeText(Latte.getApplication(), goodsName + "已经加入购物车", Toast.LENGTH_SHORT).show();
			}
		});

	}

	@Override
	protected void convertHead(BaseViewHolder helper, SectionBean item) {
		//设置文字
		helper.setText(R.id.header, item.header);
		//是否可见
		helper.setVisible(R.id.more, item.isMore());
		//点击事件
		helper.addOnClickListener(R.id.more);
	}
}
