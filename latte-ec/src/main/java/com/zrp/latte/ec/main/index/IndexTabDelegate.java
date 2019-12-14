package com.zrp.latte.ec.main.index;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ec.main.cart.like.ShopCartLikeAdapter;
import com.zrp.latte.ec.main.cart.like.ShopCartLikeConverter;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.List;

import butterknife.BindView;

public class IndexTabDelegate extends LatteDelegate {


    @BindView(R2.id.rv_all_normal)
    RecyclerView mTabReclclerView;


    @Override
    public Object setLayout() {
        return R.layout.delegate_normal_recyclerview;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        RestClient.builder()
                .url("api/youlike")
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
                        mTabReclclerView.setLayoutManager(gridLayoutManager);
                        final List<MultipleItemEntity> data = new ShopCartLikeConverter().setJsonData(response).convert();
                        final ShopCartLikeAdapter likeAdapter = new ShopCartLikeAdapter(data);
                        mTabReclclerView.setAdapter(likeAdapter);
	                    mTabReclclerView.addOnItemTouchListener(IndexItemClickListener.create(IndexTabDelegate.this));
                    }
                })
                .build()
                .get();

    }

}
