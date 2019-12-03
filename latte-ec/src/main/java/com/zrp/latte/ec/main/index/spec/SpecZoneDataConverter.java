package com.zrp.latte.ec.main.index.spec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SpecZoneDataConverter {

    public final List<SpecZoneBean> convert(String json){
        final List<SpecZoneBean> dataList = new ArrayList<>();
        final JSONArray specArray = JSON.parseObject(json).getJSONArray("special_zone");
        final int specSize = specArray.size();
        final SpecZoneBean specBean = new SpecZoneBean(true, "特色专区");
        dataList.add(specBean);
        for (int i = 0; i < specSize; i++) {
            final JSONObject specObj = specArray.getJSONObject(i);
            final String title = specObj.getString("title");
            final String subtitle = specObj.getString("subtitle");
            final String subImg1 = specObj.getString("imageOne");
            final String subImg2 = specObj.getString("imageTwo");

            final SpecZoneItemEntity specItemEntity = new SpecZoneItemEntity();
            specItemEntity.setTitle(title);
            specItemEntity.setSubTitle(subtitle);
            specItemEntity.setImgOne(subImg1);
            specItemEntity.setImgTwo(subImg2);
            dataList.add(new SpecZoneBean(specItemEntity));
        }
        return dataList;
    }

}
