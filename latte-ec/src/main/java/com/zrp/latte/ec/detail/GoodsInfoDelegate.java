package com.zrp.latte.ec.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.LatteDelegate;


import butterknife.BindView;


public class GoodsInfoDelegate extends LatteDelegate {

    @BindView(R2.id.tv_goods_info_title)
    AppCompatTextView mTvGoodsTitle;
    @BindView(R2.id.tv_goods_info_desc)
    AppCompatTextView mTvGoodsDesc;
    @BindView(R2.id.tv_goods_info_price)
    AppCompatTextView mTvGoodsPrice;

    private static final String ARGS_GOODS_DATA = "ARGS_GOODS_DATA";
    private JSONObject mData = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_goods_info;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments();
        final String data = bundle.getString(ARGS_GOODS_DATA);
        mData = JSON.parseObject(data);
    }
    public static GoodsInfoDelegate create(String goodsInfo){
        final GoodsInfoDelegate delegate = new GoodsInfoDelegate();
        final Bundle args = new Bundle();
        args.putString(ARGS_GOODS_DATA, goodsInfo);
        delegate.setArguments(args);
        return delegate;
    }


    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        final String name = mData.getString("name");
        final String desc = mData.getString("description");
        final double price = mData.getDoubleValue("price");
        mTvGoodsTitle.setText(name);
        mTvGoodsDesc.setText(desc);
        mTvGoodsPrice.setText(String.valueOf(price));
    }


}
