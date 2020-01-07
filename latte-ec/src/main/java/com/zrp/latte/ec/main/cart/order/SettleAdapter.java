package com.zrp.latte.ec.main.cart.order;

import android.widget.ImageView;

import com.example.latte.latte_ec.R;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;
import com.zrp.latte.ui.recycler.MultipleRecyclerAdapter;
import com.zrp.latte.ui.recycler.MultipleViewHolder;

import java.util.List;

public class SettleAdapter extends MultipleRecyclerAdapter {


	protected SettleAdapter(List<MultipleItemEntity> data) {
		super(data);
		addItemType(SettleItemType.ITEM_SETTLE_GOODS, R.layout.item_settle_goods);
	}

	@Override
	protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
		super.convert(holder, entity);

		switch (holder.getItemViewType()){
			case SettleItemType.ITEM_SETTLE_GOODS:
				final ImageView foodImage = holder.getView(R.id.iv_settle_img);
				//entity.getField(MultipleFields)


				break;


		}
	}
}
