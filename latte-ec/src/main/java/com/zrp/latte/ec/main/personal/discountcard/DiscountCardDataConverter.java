package com.zrp.latte.ec.main.personal.discountcard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.latte_ec.R;
import com.zrp.latte.ui.recycler.DataConverter;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.LinkedList;

public class DiscountCardDataConverter extends DataConverter {


    @Override
    public LinkedList<MultipleItemEntity> convert() {
        final JSONArray discountArray = JSON.parseArray(getJsonData());
        final int discountSize = discountArray.size();
        for(int  i= 0; i < discountSize; i++){
            final JSONObject discountObject = discountArray.getJSONObject(i);
            final String name = discountObject.getString("name");
            final String detail = discountObject.getString("detail");
            final String imgUrl = discountObject.getString("img");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(DiscountCardItemType.ITEM_NORMAL)
                    .setField(DiscountItemFields.NAME, name)
                    .setField(DiscountItemFields.DETAIL, detail)
                    .setField(DiscountItemFields.IMG, R.drawable.user_9)
                    .build();

            ENTITYS.add(entity);
        }
        return ENTITYS;
    }
}
