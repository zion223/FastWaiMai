package com.zrp.latte.ec.main.cart.like;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zrp.latte.ec.main.cart.ShopCartItemFields;
import com.zrp.latte.ec.main.cart.ShopCartItemType;
import com.zrp.latte.ui.recycler.DataConverter;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ShopCartLikeConverter extends DataConverter {

    @Override
    public LinkedList<MultipleItemEntity> convert() {
        final JSONArray productArray = JSON.parseObject(getJsonData())
                .getJSONObject("data")
                .getJSONArray("product_list");
        final int size = productArray.size();
        for(int i = 0; i < size; i++ ){
            final JSONObject data = productArray.getJSONObject(i);
            final String price = data.getString("price");
            final String name = data.getString("name");
            final String originPrice = data.getString("origin_price");
            final String thumb = data.getString("small_image");
            final String spec = data.getString("spec");
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(ShopCartItemType.SHOP_YOU_LIKE)
                    //.setField(MultipleFields.ID, Integer.valueOf(id))
                    .setField(MultipleFields.TEXT, name)
                    .setField(MultipleFields.IMAGE_URL, thumb)
                    .setField(MultipleFields.SPEC, spec)
                    .setField(ShopCartItemFields.PRICE, price)
                    .setField(ShopCartItemFields.ORIGIN_PRICE, originPrice)
                    .build();
            ENTITYS.add(entity);
        }

        return ENTITYS;
    }
}
