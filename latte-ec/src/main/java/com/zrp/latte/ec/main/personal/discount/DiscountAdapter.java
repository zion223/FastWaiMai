package com.zrp.latte.ec.main.personal.discount;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.List;

class DiscountAdapter  extends BaseMultiItemQuickAdapter<MultipleItemEntity, BaseViewHolder> {


    public DiscountAdapter(List<MultipleItemEntity> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItemEntity item) {

    }
}
