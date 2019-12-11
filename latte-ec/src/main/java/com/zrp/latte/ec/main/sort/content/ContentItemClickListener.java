package com.zrp.latte.ec.main.sort.content;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ec.main.detail.GoodsDetailDelegate;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

public class ContentItemClickListener extends SimpleClickListener {
	private final LatteDelegate DELEGATE;

	private ContentItemClickListener(LatteDelegate delegate){
		this.DELEGATE = delegate;
	}
	public static ContentItemClickListener create(LatteDelegate delegate){
		return new ContentItemClickListener(delegate);
	}
	@Override
	public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		final MultipleItemEntity entity = (MultipleItemEntity) adapter.getData().get(position);
		Integer type = entity.getField(MultipleFields.ITEM_TYPE);
		if(type.equals(ContentItemType.ITEM_NORMAL)){
			DELEGATE.getSupportDelegate().start(GoodsDetailDelegate.create(1));
		}
	}

	@Override
	public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

	}

	@Override
	public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

	}

	@Override
	public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

	}
}
