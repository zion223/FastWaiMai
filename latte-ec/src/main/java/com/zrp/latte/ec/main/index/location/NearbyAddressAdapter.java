package com.zrp.latte.ec.main.index.location;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.latte.latte_ec.R;

import java.util.List;

public class NearbyAddressAdapter extends BaseQuickAdapter<PoiInfo, BaseViewHolder> {


	NearbyAddressAdapter(@Nullable List<PoiInfo> data) {
		super(R.layout.item_nearby_address, data);
	}

	@Override
	protected void convert(BaseViewHolder helper, PoiInfo item) {
		final TextView tvName = helper.getView(R.id.tv_nearby_address);
		tvName.setText(item.getName());
	}

}