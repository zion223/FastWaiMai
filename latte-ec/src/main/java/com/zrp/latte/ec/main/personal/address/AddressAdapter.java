package com.zrp.latte.ec.main.personal.address;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.latte.latte_ec.R;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;
import com.zrp.latte.ui.recycler.MultipleRecyclerAdapter;
import com.zrp.latte.ui.recycler.MultipleViewHolder;

import java.util.List;

public class AddressAdapter extends MultipleRecyclerAdapter {

    private final AddressDelegate DELEGATE;

    AddressAdapter(List<MultipleItemEntity> data, AddressDelegate delegate) {
        super(data);
        DELEGATE = delegate;
        addItemType(AddressItemType.ITEM_ADDRESS, R.layout.item_address);
    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()){
            case AddressItemType.ITEM_ADDRESS:
                final Integer id = entity.getField(MultipleFields.ID);
                final String surname = entity.getField(AddressItemFields.SURNAME);
                final String phone = entity.getField(AddressItemFields.PHONE);
                final String gender = entity.getField(AddressItemFields.GENDER);
                final String preAddress = entity.getField(AddressItemFields.ADDRESS_PREFIX);
                final String sufAddress = entity.getField(AddressItemFields.ADDRESS_SUFFIX);
                final Boolean isDefault = entity.getField(AddressItemFields.DEFAULT);
                holder.setText(R.id.tv_item_address_firstname, surname);
                holder.setText(R.id.tv_item_address_suffix, sufAddress);
                holder.setText(R.id.tv_item_address_prefix, preAddress);
                holder.setText(R.id.tv_item_address_gender, gender);
                holder.setText(R.id.tv_item_address_phone, phone);
                final ImageView editView = holder.getView(R.id.iv_item_address_edit);
                editView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //编辑单个地址
                        DELEGATE.getSupportDelegate().start(AddressEditDelegate.create(id, surname, phone, preAddress, isDefault));
                    }
                });
            default:
                break;

        }
    }
}
