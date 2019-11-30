package com.zrp.latte.ec.main.personal.order;

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

import butterknife.BindView;


public class OrderDelegate extends BottomItemDelegate {


    private static final String ARGS_ORDER_STATUS = "ARGS_ORDER_STATUS";

    @BindView(R2.id.rv_all_normal)
    RecyclerView mRvAllOrder;


    private OrderListAdapter mAdapter = null;

    /**
     * 不同订单状态
     *  0:全部订单
     *  1:待付款
     *  2:待收货
     *  3:待评价
     *  4:售后
     */

    public static OrderDelegate create(int orderStatus) {
        final Bundle args = new Bundle();
        args.putInt(ARGS_ORDER_STATUS, orderStatus);
        final OrderDelegate delegate = new OrderDelegate();
        delegate.setArguments(args);
        return delegate;
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_normal_recyclerview;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        super.onBindView(savedInstanceState, view);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRvAllOrder.setLayoutManager(manager);
        Bundle arguments = getArguments();
        final int status = arguments.getInt(ARGS_ORDER_STATUS);
        RestClient.builder()
                .url("api/order/" + status)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final DataConverter converter = new OrderListDataConverter().setJsonData(response);
                        mAdapter = new OrderListAdapter(converter.convert());
                        mRvAllOrder.setAdapter(mAdapter);
                        //mRvAllOrder.addOnItemTouchListener(new OrderListClickListener(OrderDelegate.this));
                    }
                })
                .build()
                .get();
    }



}