package com.zrp.latte.ec.main.personal.address;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.joanzapata.iconify.widget.IconTextView;


import com.smarttop.library.bean.City;
import com.smarttop.library.bean.County;
import com.smarttop.library.bean.Province;
import com.smarttop.library.bean.Street;
import com.smarttop.library.widget.AddressSelector;
import com.smarttop.library.widget.OnAddressSelectedListener;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;

import butterknife.BindView;
import butterknife.OnClick;

public class AddressEditDelegate extends LatteDelegate {

    @BindView(R2.id.tv_address_edit_save)
    ImageView mTvAddressSave;
    @BindView(R2.id.edit_address_edit_name)
    EditText mEditAddressName;
    @BindView(R2.id.edit_address_edit_phone)
    EditText mEditAddressPhone;
    @BindView(R2.id.tv_address_edit_detail)
    TextView mTextAddressDetail;
    @BindView(R2.id.icon_address_edit_pick)
    IconTextView mIconAddressPick;
    @BindView(R2.id.sc_address_edit_compat)
    SwitchCompat mScDefaultAddress;
    @BindView(R2.id.btn_address_edit_delete)
    AppCompatButton mBtnDeleteAddress;
    @BindView(R2.id.ll_address_pick)
    LinearLayout mLinearLayout;

    private Integer id = 0;
    private String name = null;
    private String phone = null;
    private String address = null;
    private boolean isDefault = false;

    public static AddressEditDelegate create(Integer id, String name, String phone, String address, Boolean isDefault) {
        final Bundle args = new Bundle();
        args.putInt("id", id);
        args.putString("name", name);
        args.putString("phone", phone);
        args.putString("address", address);
        args.putBoolean("isDefault", isDefault);
        final AddressEditDelegate delegate = new AddressEditDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            id = args.getInt("id");
            name = args.getString("name");
            phone = args.getString("phone");
            address = args.getString("address");
            isDefault = args.getBoolean("isDefault");
        } else {
            throw new NullPointerException("Bundle is null");
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_address_edit;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        mEditAddressName.setText(name);
        mEditAddressPhone.setText(phone);
        mTextAddressDetail.setText(address);
        mBtnDeleteAddress.setFocusable(isDefault);
    }


    @OnClick(R2.id.icon_address_edit_pick)
    void onClickPickAddress(){

        AddressSelector selector = new AddressSelector(getContext());
        selector.setOnAddressSelectedListener(new OnAddressSelectedListener() {
            @Override
            public void onAddressSelected(Province province, City city, County county, Street street) {
                mTextAddressDetail.setText(String.format("%s%s%s", province.name, city.name, county.name));
                mLinearLayout.removeAllViews();
            }
        });
        selector.setOnDialogCloseListener(new AddressSelector.OnDialogCloseListener() {
            @Override
            public void dialogclose() {
                mLinearLayout.removeAllViews();
            }
        });
        View view = selector.getView();
        mLinearLayout.addView(view);
    }

    @OnClick(R2.id.tv_address_edit_save)
    void onClickSaveAddress(){
        RestClient.builder()
                .url("")
                .parmas("","")
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                    }
                })
                .build()
                .get();
    }
    @OnClick(R2.id.icon_address_edit_back)
    void onClickReturn(){
        getSupportDelegate().pop();
    }

    @OnClick(R2.id.btn_address_edit_delete)
    void onClickDeleteAddress(){
        RestClient.builder()
                .url("")
                .parmas("id", id)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        getSupportDelegate().pop();
                    }
                })
                .build()
                .get();

    }
}
