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

import butterknife.BindView;

public class DiscountDelegate extends BottomItemDelegate {

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

    }
    public static DiscountDelegate create(int orderStatus) {
        final Bundle args = new Bundle();
        args.putInt(ARGS_DISCOUNT_STATUS, orderStatus);
        final DiscountDelegate delegate = new DiscountDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        Bundle arguments = getArguments();
        final int status = arguments.getInt(ARGS_DISCOUNT_STATUS);
        RestClient.builder()
                .url("api/discount/" + status)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final DataConverter converter = new DiscountDataConverter().setJsonData(response);

                        mAdapter = new DiscountAdapter(converter.convert());
                        recyclerView.setAdapter(mAdapter);

                    }
                })
                .build()
                .get();


    }

}
