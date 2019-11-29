package com.zrp.latte.ec.main.personal.discountcard;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.latte.latte_ec.R;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.LinkedList;

public class DiscountCardAdapter extends BaseMultiItemQuickAdapter<MultipleItemEntity, BaseViewHolder> implements View.OnClickListener {

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
    public DiscountCardAdapter(LinkedList<MultipleItemEntity> data) {
        super(data);
        addItemType(DiscountCardItemType.ITEM_NORMAL, R.layout.item_discount_card_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItemEntity item) {
        switch (helper.getItemViewType()){
            case DiscountCardItemType.ITEM_NORMAL:
                final String name = item.getField(DiscountItemFields.NAME);
                helper.setText(R.id.tv_discount_name, name);
                final String detail = item.getField(DiscountItemFields.DETAIL);
                helper.setText(R.id.tv_discount_more, detail);
                final ImageView discountImgView = helper.getView(R.id.iv_discount_img);
                final int imgUrl = item.getField(DiscountItemFields.IMG);
                Glide.with(mContext)
                        .load(imgUrl)
                        .apply(OPTIONS)
                        .into(discountImgView);
                final ImageView detailView = helper.getView(R.id.iv_discount_detail);
                detailView.setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {

    }
}
