package com.zrp.latte.ec.main.index;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ec.main.detail.GoodsDetailDelegate;
import com.zrp.latte.ec.main.sort.SortDelegate;
import com.zrp.latte.ui.recycler.ItemType;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;


/**
 * 点击Item跳转到对应的商品详情
 */
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
//        //取出商品ID
//        final int goodsId = entity.getField(MultipleFields.ID);
//        //根据ID不同查询不同的Delegate
        final GoodsDetailDelegate goodsDetailDelegate = GoodsDetailDelegate.create(1);
        DELEGATE.getSupportDelegate().start(goodsDetailDelegate);
        Integer type = entity.getField(MultipleFields.ITEM_TYPE);
        if(type.equals(ItemType.SORT)){
            //DELEGATE.getSupportDelegate().showHideFragment(new SortDelegate());
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
