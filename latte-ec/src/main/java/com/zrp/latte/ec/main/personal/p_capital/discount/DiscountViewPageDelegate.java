package com.zrp.latte.ec.main.personal.p_capital.discount;

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
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.LinkedList;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DiscountViewPageDelegate extends BottomItemDelegate {

    private static final String ARGS_DISCOUNT_STATUS = "ARGS_DISCOUNT_STATUS";

    @BindView(R2.id.rv_all_normal)
    RecyclerView recyclerView;


    private DiscountAdapter mAdapter = null;


    public static DiscountViewPageDelegate create(int orderStatus) {
        final Bundle args = new Bundle();
        args.putInt(ARGS_DISCOUNT_STATUS, orderStatus);
        final DiscountViewPageDelegate delegate = new DiscountViewPageDelegate();
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
        recyclerView.setLayoutManager(manager);
        Bundle arguments = getArguments();
        if (arguments != null) {
            final int status = arguments.getInt(ARGS_DISCOUNT_STATUS);
            RestClient.builder()
                    .url("api/p_discount/" + status)
                    .build()
                    .get()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String response) throws Exception {
                            final DataConverter converter = new DiscountDataConverter().setJsonData(response);
                            LinkedList<MultipleItemEntity> entities = converter.convert();
                            mAdapter = new DiscountAdapter(entities);
                            recyclerView.setAdapter(mAdapter);
                            recyclerView.setBackgroundColor(getResources().getColor(R.color.app_background));
                        }
                    });
        }


    }

}
