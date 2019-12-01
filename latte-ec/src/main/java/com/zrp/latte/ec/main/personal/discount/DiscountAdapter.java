package com.zrp.latte.ec.main.personal.discount;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.latte.latte_ec.R;
import com.zrp.latte.ec.main.personal.discountcard.DiscountCardItemType;
import com.zrp.latte.ec.main.personal.discountcard.DiscountItemFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.List;

class DiscountAdapter extends BaseMultiItemQuickAdapter<MultipleItemEntity, BaseViewHolder> {


    public DiscountAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(DiscountCardItemType.ITEM_NORMAL_AVAILIABLE, R.layout.item_discount_available_card);
        addItemType(DiscountCardItemType.ITEM_NORMAL_UNAVAILIABLE, R.layout.item_discount_unavailable_text);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItemEntity item) {
        switch (helper.getItemViewType()){
            case DiscountCardItemType.ITEM_NORMAL_AVAILIABLE:
                String money = item.getField(DiscountItemFields.MONEY);
                String time = item.getField(DiscountItemFields.EXPIRE_TINE);
                helper.setText(R.id.tv_money, money.split(":")[0]);
                helper.setText(R.id.tv_per, money.split(":")[1]);
                helper.setText(R.id.tv_discount_effective_time, time);
                break;
            case DiscountCardItemType.ITEM_NORMAL_UNAVAILIABLE:
                break;
            default:
                break;
        }
    }
}
