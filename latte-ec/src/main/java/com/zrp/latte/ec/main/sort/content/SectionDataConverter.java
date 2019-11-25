package com.zrp.latte.ec.main.sort.content;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SectionDataConverter {

	final List<SectionBean> convert(String json) {

		final List<SectionBean> dataList = new ArrayList();
		final JSONObject goodsObject = JSON.parseObject(json).getJSONObject("data").getJSONArray("cate").getJSONObject(0);

		final String catrgoryId = goodsObject.getString("id");
		final String catrgory = goodsObject.getString("name");

		final SectionBean sectionTitleBean = new SectionBean(true, catrgory);
		//标题ID
		//TODO
		sectionTitleBean.setId(1);
		//显示更多More
		sectionTitleBean.setIsMore(false);
		dataList.add(sectionTitleBean);
		final JSONArray goodsArray = goodsObject.getJSONArray("products");
		//解析goods
		final int goodsSize = goodsArray.size();
		for (int j = 0; j < goodsSize; j++) {
			final JSONObject good = goodsArray.getJSONObject(j);
			//final Integer goodsId = good.getIntValue("goods_id");
			//商品图片url
			final String goodsThumb = good.getString("small_image");
			//商品名称
			final String goodsName = good.getString("name");
			final String goodsSpec = good.getString("spec");
			final String price = good.getString("price");
			final String originPrice = good.getString("origin_price");
			//封装 SectionContentItemEntity
			final SectionContentItemEntity entity = new SectionContentItemEntity();

			entity.setGoodsId(2);
			entity.setGoodsDetail(goodsSpec);
			entity.setGoodsOldPrice(originPrice);
			entity.setGoodsPrice(price);
			entity.setGoodsName(goodsName);
			entity.setGoodsThumb(goodsThumb);
			//添加到List
			dataList.add(new SectionBean(entity));
		}

		return dataList;
	}
}
