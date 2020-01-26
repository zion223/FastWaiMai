package com.zrp.latte.ec.main.personal.p_function.address;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zrp.latte.ui.recycler.DataConverter;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.LinkedList;

public class AddressDataConverter extends DataConverter {

    @Override
    public LinkedList<MultipleItemEntity> convert() {
        final JSONArray addressArray = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int addressSize = addressArray.size();
        for(int i = 0; i < addressSize; i++){
            final JSONObject address = addressArray.getJSONObject(i);

            final int id = address.getIntValue("id");
            final String name = address.getString("name");
            // 0 : 先生
            // 1 : 女士
            final Integer gender = address.getIntValue("gender");
            final String phone = address.getString("phone");
            //地址前缀
            final String preAddress = address.getString("preaddress");
            //地址后缀
            final String sufAddress = address.getString("sufaddress");
            //门牌号
            final String houseNumber = address.getString("housenumber");
            final Boolean isDefault = address.getBoolean("default");
            final Integer tag = address.getIntValue("tag");

            final MultipleItemEntity addressEntity = MultipleItemEntity.builder()
                    .setItemType(AddressItemType.ITEM_ADDRESS)
                    .setField(MultipleFields.ID, id)
                    .setField(AddressItemFields.SURNAME, name)
                    .setField(AddressItemFields.GENDER, gender)
                    .setField(AddressItemFields.PHONE, phone)
                    .setField(AddressItemFields.ADDRESS_PREFIX, preAddress)
                    .setField(AddressItemFields.ADDRESS_SUFFIX, sufAddress)
                    .setField(AddressItemFields.HOUSE_NUMBER, houseNumber)
                    .setField(AddressItemFields.ADDRESS_TAG, tag)
                    .setField(AddressItemFields.DEFAULT, isDefault)
                    .build();
            ENTITYS.add(addressEntity);
        }
        return ENTITYS;
    }
}
