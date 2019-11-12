package com.zrp.latte.ec.main.personal.address;

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
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ec.main.personal.order.OrderListAdapter;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.ui.recycler.DataConverter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddressDelegate extends LatteDelegate {

    @BindView(R2.id.icon_address_add)
    IconTextView mIconAddressAdd;
    @BindView(R2.id.rv_address)
    RecyclerView mRvAddress;
    private AddressAdapter mAdapter = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_address;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRvAddress.setLayoutManager(manager);
        RestClient.builder()
                .url("api/address")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final DataConverter converter =
                                new AddressDataConverter().setJsonData(response);
                        mAdapter = new AddressAdapter(converter.convert(), AddressDelegate.this);
                        mRvAddress.setAdapter(mAdapter);
                    }
                })
                .build()
                .get();
    }

}
