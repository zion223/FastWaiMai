package com.zrp.latte.ec.main.personal.address;

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
            final String gender = address.getString("gender");
            final String phone = address.getString("phone");
            final String preAddress = address.getString("preaddress");
            final String sufAddress = address.getString("sufaddress");
            final Boolean isDefault = address.getBoolean("default");

            final MultipleItemEntity addressEntity = MultipleItemEntity.builder()
                    .setItemType(AddressItemType.ITEM_ADDRESS)
                    .setField(MultipleFields.ID, id)
                    .setField(AddressItemFields.SURNAME, name)
                    .setField(AddressItemFields.GENDER, gender)
                    .setField(AddressItemFields.PHONE, phone)
                    .setField(AddressItemFields.ADDRESS_PREFIX, preAddress)
                    .setField(AddressItemFields.ADDRESS_SUFFIX, sufAddress)
                    .setField(AddressItemFields.DEFAULT, isDefault)
                    .build();
            ENTITYS.add(addressEntity);
        }
        return ENTITYS;
    }
}
