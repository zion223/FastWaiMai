package com.zrp.latte.ec.main.personal.p_capital.discount;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.latte.latte_ec.R;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.List;

class DiscountAdapter extends BaseMultiItemQuickAdapter<MultipleItemEntity, BaseViewHolder> {


    DiscountAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(DiscountCardItemType.ITEM_NORMAL_AVAILIABLE, R.layout.item_discount_available_card);
        addItemType(DiscountCardItemType.ITEM_NORMAL_UNAVAILIABLE, R.layout.item_discount_unavailable_text);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItemEntity item) {
        switch (helper.getItemViewType()){
            case DiscountCardItemType.ITEM_NORMAL_AVAILIABLE:
                String shopName = item.getField(DiscountItemFields.SHOP_NAME);
                helper.setText(R.id.tv_item_discount_shopname, shopName);

                String type = item.getField(DiscountItemFields.TYPE);
                helper.setText(R.id.tv_item_discount_type, type);
                String expireTime = item.getField(DiscountItemFields.EXPIRE_TIME);
                helper.setText(R.id.tv_item_discount_expiretime, "有效期至" + expireTime);

                String orimoney = item.getField(DiscountItemFields.MONEY);
                final SpannableString money = new SpannableString(orimoney);
                money.setSpan(new AbsoluteSizeSpan(13, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_item_discount_money, money);
                String condition = item.getField(DiscountItemFields.CONDITION);
                helper.setText(R.id.tv_item_discount_condition, condition);

                break;
            case DiscountCardItemType.ITEM_NORMAL_UNAVAILIABLE:
                break;
            default:
                break;
        }
    }
}
