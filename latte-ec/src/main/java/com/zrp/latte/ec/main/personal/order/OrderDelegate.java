package com.zrp.latte.ec.main.personal.order;

import android.annotation.SuppressLint;
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
import com.zrp.latte.ui.recycler.DataConverter;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


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

    @SuppressLint("CheckResult")
    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        super.onBindView(savedInstanceState, view);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRvAllOrder.setLayoutManager(manager);
        Bundle arguments = getArguments();
        if (arguments != null) {
            final int status = arguments.getInt(ARGS_ORDER_STATUS);
            RestClient.builder()
                    .url("api/order/" + status)
                    .build()
                    .get()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String response) throws Exception {
                            final DataConverter converter = new OrderListDataConverter().setJsonData(response);
                            mAdapter = new OrderListAdapter(converter.convert());
                            mRvAllOrder.setAdapter(mAdapter);
                            //mRvAllOrder.addOnItemTouchListener(new OrderListClickListener(OrderDelegate.this));
                        }
                    });

        }


    }



}