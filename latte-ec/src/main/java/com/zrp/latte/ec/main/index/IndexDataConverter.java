package com.zrp.latte.ec.main.index;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zrp.latte.ui.recycler.DataConverter;
import com.zrp.latte.ui.recycler.ItemType;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 首页数据转换器
 */
public final class IndexDataConverter extends DataConverter {


    @Override
    public LinkedList<MultipleItemEntity> convert() {

        Log.d("get",getJsonData());
        final JSONArray dataArray = JSON.parseObject(getJsonData()).getJSONArray("data");

        final List<String> banners = (List<String>) JSON.parseObject(getJsonData()).get("banners");

        final int dataSize = dataArray.size();

        //ENTITY 添加的先后顺序有影响
        if(banners != null) {
            final ArrayList<String> bannersImage = new ArrayList<>();
            //广告
            final int bannerSize = banners.size();
            for (int j = 0; j < bannerSize; j++) {
                bannersImage.add(banners.get(j));
            }
            ENTITYS.add(MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, ItemType.BANNER)
                    .setField(MultipleFields.SPAN_SIZE, 4)
                    .setField(MultipleFields.BANNERS, bannersImage)
                    .build());
        }
        for(int i = 0; i < dataSize; i++){
            final JSONObject data = dataArray.getJSONObject(i);
            final String imageUrl = data.getString("productImageBig");
            final String productName = data.getString("productName");

            final int spanSize = data.getIntValue("spanSize");
            final int id = data.getIntValue("productId");

            int type = 0;
            //根据数据类型自定义扩展
            if(imageUrl == null && productName != null){
                //单文字
				type = ItemType.TEXT;
            }else if(imageUrl != null && productName != null){
                //图文格式
                type = ItemType.TEXT_IMAGE;
            }else {
                //单图片
				type = ItemType.IMAGE;

            }
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE,type)
                    .setField(MultipleFields.TEXT,productName)
                    //TODO spanSize 需要设置
                    .setField(MultipleFields.SPAN_SIZE,2)
                    .setField(MultipleFields.ID,id)
                    .setField(MultipleFields.IMAGE_URL,imageUrl)
                    .build();

            ENTITYS.add(entity);
        }

        return ENTITYS;
    }
}
