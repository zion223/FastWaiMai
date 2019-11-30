package com.zrp.latte.ec.main.personal.discount;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zrp.latte.ec.main.personal.discountcard.DiscountCardItemType;
import com.zrp.latte.ec.main.personal.discountcard.DiscountItemFields;
import com.zrp.latte.ui.recycler.DataConverter;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.LinkedList;

public class DiscountDataConverter extends DataConverter {

    @Override
    public LinkedList<MultipleItemEntity> convert() {
        final JSONObject discountObject = JSON.parseObject(getJsonData());
        final JSONArray discountArray = discountObject.getJSONArray("data");
        final int size = discountArray.size();
        ENTITYS.clear();
        if(size == 0){
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(DiscountCardItemType.ITEM_NORMAL_UNAVAILIABLE)
                    .build();
            ENTITYS.add(entity);
        }
        for(int i = 0; i < size; i++){
            final JSONObject dis = discountArray.getJSONObject(i);
            final int id = dis.getIntValue("id");
            final String discountMoney = dis.getString("disocuntmoney");
            final String expireTime = dis.getString("expiretime");
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(DiscountCardItemType.ITEM_NORMAL_AVAILIABLE)
                    .setField(MultipleFields.ID, id)
                    .setField(DiscountItemFields.MONEY, discountMoney)
                    .setField(DiscountItemFields.EXPIRE_TINE, expireTime)
                    .build();
            ENTITYS.add(entity);
        }
        return ENTITYS;
    }
}
