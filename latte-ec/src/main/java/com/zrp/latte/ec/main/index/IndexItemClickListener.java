package com.zrp.latte.ec.main.index;


import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import com.zrp.latte.app.Latte;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.delegates.bottom.BaseBottomDelegate;
import com.zrp.latte.ec.main.cart.ShopCartItemType;
import com.zrp.latte.ui.recycler.ItemType;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;


public class IndexItemClickListener extends SimpleClickListener {

    private final LatteDelegate DELEGATE;

    private IndexItemClickListener(LatteDelegate delegate){
        this.DELEGATE = delegate;
    }
    public static IndexItemClickListener create(LatteDelegate delegate){
        return new IndexItemClickListener(delegate);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final MultipleItemEntity entity = (MultipleItemEntity) adapter.getData().get(position);

        final Integer type = entity.getField(MultipleFields.ITEM_TYPE);
        if(type.equals(ItemType.SORT)){
	        DELEGATE.getParentDelegate().getSupportDelegate().showHideFragment(BaseBottomDelegate.ITEM_DELEGATES.get(1),
		            BaseBottomDelegate.ITEM_DELEGATES.get(0));
        }else if(type.equals(ItemType.BANNER)){
	        Toast.makeText(Latte.getApplication(), "首页广告", Toast.LENGTH_SHORT).show();
        }else if(type.equals(ShopCartItemType.SHOP_YOU_LIKE)){
	        //final GoodsDetailDelegate goodsDetailDelegate = GoodsDetailDelegate.create(1);
	        //DELEGATE.getParentDelegate().getSupportDelegate().start(goodsDetailDelegate);
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
