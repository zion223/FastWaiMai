package com.zrp.latte.ec.main.personal.discount;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.latte.latte_ec.R;
import com.zrp.latte.ec.main.personal.discountcard.DiscountCardItemType;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.List;

class DiscountAdapter extends BaseMultiItemQuickAdapter<MultipleItemEntity, BaseViewHolder> {


    public DiscountAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(DiscountCardItemType.ITEM_NORMAL, R.layout.item_discount);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItemEntity item) {
        switch (helper.getItemViewType()){
            case DiscountCardItemType.ITEM_NORMAL:
                String money = item.getField("");
                helper.setText(R.id.tv_money, money);
                break;
            default:
                break;
        }
    }
}
