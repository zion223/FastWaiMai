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
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.latte.latte_ec.R;
import com.zrp.latte.app.Latte;
import com.zrp.latte.ec.main.cart.ShopCartItemFields;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.List;

public class SectionAdapter extends BaseMultiItemQuickAdapter<MultipleItemEntity, BaseViewHolder> {

	private static final RequestOptions RECYCLE_OPTIONS =
			new RequestOptions()
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.dontAnimate();


	public SectionAdapter(List<MultipleItemEntity> data) {
		super(data);
		addItemType(ContentItemType.ITEM_NORMAL, R.layout.item_section_content);
	}

	@Override
	protected void convert(BaseViewHolder helper, MultipleItemEntity item) {
        switch (helper.getItemViewType()){
            case ContentItemType.ITEM_NORMAL:
                final String goodsName = item.getField(MultipleFields.TEXT);
                final String goodsSpec = item.getField(MultipleFields.SPEC);
                final String goodsThumb = item.getField(MultipleFields.IMAGE_URL);
                final String price = item.getField(ShopCartItemFields.PRICE);
                final String originPrice = item.getField(ShopCartItemFields.ORIGIN_PRICE);
                //设置Text
                helper.setText(R.id.tv_goods_name, goodsName);
                helper.setText(R.id.tv_goods_detail, goodsSpec);
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
                break;
            default:
                break;
        }
	}

}
