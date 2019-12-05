package com.zrp.latte.ec.main.personal.address;

import android.view.View;
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
                final String name = entity.getField(AddressItemFields.NAME);
                final String phone = entity.getField(AddressItemFields.PHONE);
                final String address = entity.getField(AddressItemFields.ADDRESS);
                final Boolean isDefault = entity.getField(AddressItemFields.DEFAULT);
                holder.setText(R.id.tv_address_name, name);
                holder.setText(R.id.tv_address_phone, phone);
                holder.setText(R.id.tv_address_detail, address);
                final TextView editView = holder.getView(R.id.tv_address_edit);
                editView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //编辑单个地址
                        //Toast.makeText(mContext, id, Toast.LENGTH_SHORT).show();
                        DELEGATE.getSupportDelegate().start(AddressEditDelegate.create(id, name, phone, address, isDefault));
                    }
                });

        }
    }
}
