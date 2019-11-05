package com.zrp.latte.ec.main.sort.list;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zrp.latte.ui.recycler.DataConverter;
import com.zrp.latte.ui.recycler.ItemType;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 * 左边栏分类数据转换器
 */
public class VerticalListDataConverter extends DataConverter {

	@Override
	public ArrayList<MultipleItemEntity> convert() {
		final ArrayList<MultipleItemEntity> dataList = new ArrayList<>();
		final JSONArray dataArray = JSONObject.parseObject(getJsonData()).getJSONArray("result");

		final int dataSize = dataArray.size();

		for(int i=0; i<dataSize; i++){
			final JSONObject data = dataArray.getJSONObject(i);
			final Integer categoryId = data.getIntValue("id");
			final String category = data.getString("catalog");
			final MultipleItemEntity entity = MultipleItemEntity.builder()
					.setField(MultipleFields.ITEM_TYPE, ItemType.VERTICAL_MENU_LIST)
					.setField(MultipleFields.ID, categoryId)
					.setField(MultipleFields.TEXT, category)
					.setField(MultipleFields.TAG, false)
					.build();
			dataList.add(entity);
			//第一个设置为True
			dataList.get(0).setField(MultipleFields.TAG, true);
		}

		return dataList;
	}
}
