package com.zrp.latte.ec.main.cart.like;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.latte.latte_ec.R;
import com.zrp.latte.ec.main.cart.ShopCartItemFields;
import com.zrp.latte.ec.main.cart.ShopCartItemType;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;
import com.zrp.latte.ui.recycler.MultipleRecyclerAdapter;
import com.zrp.latte.ui.recycler.MultipleViewHolder;

import java.util.List;

public class ShopCartLikeAdapter extends BaseMultiItemQuickAdapter<MultipleItemEntity,BaseViewHolder> {


    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    public ShopCartLikeAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(ShopCartItemType.SHOP_YOU_LIKE, R.layout.item_you_like);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItemEntity entity) {
        switch (helper.getItemViewType()) {
            case ShopCartItemType.SHOP_YOU_LIKE:
                final String name = entity.getField(MultipleFields.TEXT);
                final String spec = entity.getField(MultipleFields.SPEC);
                final String price = entity.getField(ShopCartItemFields.PRICE);
                final String originPrice = entity.getField(ShopCartItemFields.ORIGIN_PRICE);
                final String thumb = entity.getField(MultipleFields.IMAGE_URL);
                helper.setText(R.id.tv_like_name, name);
                helper.setText(R.id.tv_like_spec, spec);
                helper.setText(R.id.tv_like_price, price);
                helper.setText(R.id.tv_like_origin_price, originPrice);
                final ImageView imageView = helper.getView(R.id.iv_like_img);
                Glide.with(mContext)
                        .load(thumb)
                        .apply(OPTIONS)
                        .into(imageView);
                break;
            default:
                break;
        }
    }
}
