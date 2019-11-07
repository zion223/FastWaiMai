package com.zrp.latte.ec.main.personal.discount;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.latte.latte_ec.R;

import java.util.List;

public class DiscountCardAdapter extends BaseMultiItemQuickAdapter<DiscountCardBean, BaseViewHolder> {

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public DiscountCardAdapter(List<DiscountCardBean> data) {
        super(data);
        addItemType(DiscountCardItemType.ITEM_NORMAL, R.layout.arrow_discount_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscountCardBean item) {
        switch (helper.getItemViewType()){
            case DiscountCardItemType.ITEM_NORMAL:
                helper.setText(R.id.tv_discount_name,item.getDiscountCardName());
                helper.setText(R.id.tv_discount_detail,item.getDiscountCardDetail());
                final ImageView discountImgView = helper.getView(R.id.iv_discount_img);
                Glide.with(mContext)
                        .load(item.getDiscountCardUrlId())
                        .apply(OPTIONS)
                        .into(discountImgView);

        }
    }
}
