package com.zrp.latte.ec.main.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zrp.latte.ui.recycler.DataConverter;
import com.zrp.latte.ui.recycler.ItemType;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 首页数据转换器
 */
public final class IndexDataConverter extends DataConverter {


    @Override
    public LinkedList<MultipleItemEntity> convert() {

        final JSONObject homeObject = JSON.parseObject(getJsonData()).getJSONObject("data");
        final JSONObject gifObject = homeObject.getJSONObject("home_ad");
        final String gifUrl = gifObject.getString("image_url");
        final JSONArray bannerArray = homeObject.getJSONArray("list");
        final JSONArray iconArray = bannerArray.getJSONObject(0).getJSONArray("icon_list");
        final JSONObject adObject = bannerArray.getJSONObject(1);
        final String textUrl = adObject.getString("image_url");
        final int iconSize = iconArray.size();
        MultipleItemEntity bannerEntity = null;
        final ArrayList<String> bannderList = new ArrayList();
        for(int i = 0; i < iconSize; i++){
            JSONObject iconObject = iconArray.getJSONObject(i);
            String iconUrl = iconObject.getString("icon_url");
            bannderList.add(iconUrl);
        }
        bannerEntity = MultipleItemEntity.builder()
                .setItemType(ItemType.BANNER)
                .setField(MultipleFields.BANNERS, bannderList)
                .setField(MultipleFields.BANNER_GIF, gifUrl)
                .setField(MultipleFields.BANNER_TEXT, textUrl)
                .setField(MultipleFields.SPAN_SIZE, 4)
                .build();
        ENTITYS.add(bannerEntity);
        //MultipleItemEntity sortEntity = null;
        final JSONArray sortArray = bannerArray.getJSONObject(2).getJSONArray("icon_list");

        final int sortSize = sortArray.size();
        for(int j = 0; j < sortSize; j++){
            final JSONObject sort = sortArray.getJSONObject(j);
            final String sortUrl = sort.getString("icon_url");
            final String sortText = sort.getString("name");
            final MultipleItemEntity sortEntity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, ItemType.SORT)
                    .setField(MultipleFields.TEXT, sortText)
                    .setField(MultipleFields.SPAN_SIZE, 5)
                    .setField(MultipleFields.IMAGE_URL, sortUrl)
                    .build();
            ENTITYS.add(sortEntity);
        }


        return ENTITYS;
    }
}
