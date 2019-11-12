package com.zrp.latte.ec.main.personal.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.joanzapata.iconify.widget.IconTextView;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.ui.recycler.DataConverter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class OrderDelegate extends BottomItemDelegate {

    @BindView(R2.id.rv_all_order)
    RecyclerView mRvAllOrder;


    private OrderListAdapter mAdapter = null;

    //TODO TAB切换不同状态的订单

    @Override
    public Object setLayout() {
        return R.layout.delegate_personal_order;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        super.onBindView(savedInstanceState, view);
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRvAllOrder.setLayoutManager(manager);

        RestClient.builder()
                .url("api/order")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final DataConverter converter = new OrderListDataConverter().setJsonData(response);
                        mAdapter = new OrderListAdapter(converter.convert());
                        mRvAllOrder.setAdapter(mAdapter);
                    }
                })
                .build()
                .get();


    }


    @OnClick(R2.id.icon_order_return)
    public void onViewClickedReturn() {
        getSupportDelegate().pop();
    }
}
