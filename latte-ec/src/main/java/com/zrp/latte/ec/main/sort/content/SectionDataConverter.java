package com.zrp.latte.ec.main.sort.content;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zrp.latte.ec.main.cart.ShopCartItemFields;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.List;

public class SectionDataConverter {

	final SectionBean convert(String json) {

		final SectionBean sectionBeanNew = new SectionBean();

		final JSONArray dataArray = JSON.parseObject(json)
				.getJSONObject("data")
				.getJSONArray("cate");
		final List<String> header = new ArrayList<>();
		final List<List<MultipleItemEntity>> data = new ArrayList<>();

		final int size = dataArray.size();
		for(int i = 0; i < size; i++){
			final JSONObject dataObject = dataArray.getJSONObject(i);
			//dataObject.getString("id");
			final String name = dataObject.getString("name");
			header.add(name);
			final JSONArray products = dataObject.getJSONArray("products");
			final int productSize = products.size();
			final List<MultipleItemEntity> entities = new ArrayList<>();
			for(int j = 0;j < productSize; j++) {

				final JSONObject good = products.getJSONObject(j);
				final String goodsThumb = good.getString("small_image");
				final String goodsName = good.getString("name");
				final String goodsSpec = good.getString("spec");
				final String price = good.getString("price");
				final String originPrice = good.getString("origin_price");
				final MultipleItemEntity entity = MultipleItemEntity.builder()
						.setItemType(ContentItemType.ITEM_NORMAL)
						.setField(MultipleFields.TEXT, goodsName)
						.setField(MultipleFields.SPEC, goodsSpec)
						.setField(MultipleFields.IMAGE_URL, goodsThumb)
						.setField(ShopCartItemFields.PRICE, price)
						.setField(ShopCartItemFields.ORIGIN_PRICE, originPrice)
						.build();
				entities.add(entity);
			}
			data.add(entities);
		}
		sectionBeanNew.setDatas(data);
		sectionBeanNew.setHeaders(header);
		return sectionBeanNew;
	}
}
