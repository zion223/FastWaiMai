package com.zrp.latte.ec.main.personal.order;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.List;

public class OrderListAdapter extends BaseMultiItemQuickAdapter<MultipleItemEntity, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public OrderListAdapter(List<MultipleItemEntity> data) {
        super(data);
        //addItemType();
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItemEntity item) {

    }
}
