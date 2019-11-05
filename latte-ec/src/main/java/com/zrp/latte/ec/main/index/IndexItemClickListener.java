package com.zrp.latte.ec.main.index;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ec.main.detail.GoodsDetailDelegate;


/**
 * 点击Item跳转到对应的商品详情
 */
public class IndexItemClickListener extends SimpleClickListener {

    private LatteDelegate DELEGATE;

    private IndexItemClickListener(LatteDelegate delegate){
        this.DELEGATE = delegate;
    }
    public static IndexItemClickListener create(LatteDelegate delegate){
        return new IndexItemClickListener(delegate);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final GoodsDetailDelegate goodsDetailDelegate = GoodsDetailDelegate.create();
        DELEGATE.getSupportDelegate().start(goodsDetailDelegate);
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
