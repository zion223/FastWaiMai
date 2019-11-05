package com.zrp.latte.ec.main.sort.content;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SectionDataConverter {

	final List<SectionBean> convert(String json) {

		final List<SectionBean> dataList = new ArrayList();
		final JSONArray bookArray = JSON.parseObject(json).getJSONObject("result").getJSONArray("data");
		//左边标题 中国文学 历史
		final String[] catrgory = bookArray.getJSONObject(0).getString("catalog").split(" ");

		final SectionBean sectionTitleBean = new SectionBean(true, catrgory[0]);
		//标题ID
		//TODO
		sectionTitleBean.setId(1);
		//显示更多More
		sectionTitleBean.setIsMore(true);
		dataList.add(sectionTitleBean);

		//解析goods
		final int goodsSize = bookArray.size();
		for (int j = 0; j < goodsSize; j++) {
			final JSONObject good = bookArray.getJSONObject(j);
			//final Integer goodsId = good.getIntValue("goods_id");
			//图片url
			final String goodsThumb = good.getString("img").replace("\\", " ");
			//图书名称
			final String goodsName = good.getString("title");
			//封装 SectionContentItemEntity
			final SectionContentItemEntity sectionContentItemEntity = new SectionContentItemEntity();
			//TODO 商品ID  提供图书详情查看
			sectionContentItemEntity.setGoodsId(2);
			sectionContentItemEntity.setGoodsName(goodsName);
			sectionContentItemEntity.setGoodsThumb(goodsThumb);
			//添加到List
			dataList.add(new SectionBean(sectionContentItemEntity));
		}

		return dataList;
	}
}
