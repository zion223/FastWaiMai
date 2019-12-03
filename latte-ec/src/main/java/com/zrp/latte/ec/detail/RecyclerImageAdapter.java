package com.zrp.latte.ec.detail;

import android.support.v7.widget.AppCompatImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.latte.latte_ec.R;
import com.zrp.latte.ui.recycler.ItemType;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;
import com.zrp.latte.ui.recycler.MultipleRecyclerAdapter;
import com.zrp.latte.ui.recycler.MultipleViewHolder;

import java.util.List;

public class RecyclerImageAdapter extends MultipleRecyclerAdapter {

    public RecyclerImageAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(ItemType.SINGLE_BIG_IMAGE, R.layout.item_image);
    }
    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        switch (holder.getItemViewType()){
            case ItemType.SINGLE_BIG_IMAGE:
                final AppCompatImageView view = holder.getView(R.id.image_rv_item);
                final String url = entity.getField(MultipleFields.IMAGE_URL);
                Glide.with(mContext)
                        .load(url)
                        .apply(OPTIONS)
                        .into(view);
                break;
            default:
                break;

        }
    }




}
