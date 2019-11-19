package com.zrp.latte.ec.detail;

import android.support.v7.widget.AppCompatImageView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.latte.latte_ec.R;
import com.zrp.latte.ui.recycler.ItemType;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;
import com.zrp.latte.ui.recycler.MultipleRecyclerAdapter;

import java.util.List;

public class RecyclerImageAdapter extends MultipleRecyclerAdapter{

    public RecyclerImageAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(ItemType.SINGLE_BIG_IMAGE, R.layout.item_image);
    }

    protected void convert(BaseViewHolder helper, MultipleItemEntity item) {
        switch (helper.getItemViewType()){
            case ItemType.SINGLE_BIG_IMAGE:
                final AppCompatImageView view =helper.getView(R.id.image_rv_item);
                final String url = item.getField(MultipleFields.IMAGE_URL);
                Glide.with(mContext)
                        .load(url)
                        .into(view);
                break;
            default:
                break;

        }
    }



}
