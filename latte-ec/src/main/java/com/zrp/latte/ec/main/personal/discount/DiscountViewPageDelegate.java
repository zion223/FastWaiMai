package com.zrp.latte.ec.main.personal.discount;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.ui.recycler.DataConverter;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.LinkedList;

import butterknife.BindView;

public class DiscountViewPageDelegate extends BottomItemDelegate {

    private static final String ARGS_DISCOUNT_STATUS = "ARGS_DISCOUNT_STATUS";

    @BindView(R2.id.rv_all_normal)
    RecyclerView recyclerView;


    private DiscountAdapter mAdapter = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_normal_recyclerview;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        super.onBindView(savedInstanceState, view);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        Bundle arguments = getArguments();
        final int status = arguments.getInt(ARGS_DISCOUNT_STATUS);
        RestClient.builder()
                .url("api/discount/" + status)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final DataConverter converter = new DiscountDataConverter().setJsonData(response);
                        LinkedList<MultipleItemEntity> entities = converter.convert();
                        mAdapter = new DiscountAdapter(entities);
                        recyclerView.setAdapter(mAdapter);
                        recyclerView.setBackgroundColor(getResources().getColor(R.color.app_background));

                    }
                })
                .build()
                .get();

    }
    public static DiscountViewPageDelegate create(int orderStatus) {
        final Bundle args = new Bundle();
        args.putInt(ARGS_DISCOUNT_STATUS, orderStatus);
        final DiscountViewPageDelegate delegate = new DiscountViewPageDelegate();
        delegate.setArguments(args);
        return delegate;
    }



}
