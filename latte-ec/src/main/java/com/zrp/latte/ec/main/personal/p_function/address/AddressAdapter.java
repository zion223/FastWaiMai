package com.zrp.latte.ec.main.personal.p_function.address;

import android.view.View;
import android.widget.ImageView;

import com.example.latte.latte_ec.R;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;
import com.zrp.latte.ui.recycler.MultipleRecyclerAdapter;
import com.zrp.latte.ui.recycler.MultipleViewHolder;

import java.util.List;

public class AddressAdapter extends MultipleRecyclerAdapter {

    private final LatteDelegate DELEGATE;
    private final Type currentType;
    private static final String MALE = "先生";
    private static final String FEMALE = "女士";

    AddressAdapter(List<MultipleItemEntity> data, LatteDelegate delegate) {
        super(data);
        DELEGATE = delegate;
        currentType = Type.ADDRESS;
        addItemType(AddressItemType.ITEM_ADDRESS, R.layout.item_address);
    }

    public AddressAdapter(List<MultipleItemEntity> data) {
        super(data);
        DELEGATE = null;
        currentType = Type.LOCATION;
        addItemType(AddressItemType.ITEM_ADDRESS, R.layout.item_address);
    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        String genderText;
        switch (holder.getItemViewType()){
            case AddressItemType.ITEM_ADDRESS:
                final String name = entity.getField(AddressItemFields.SURNAME);
                final String phone = entity.getField(AddressItemFields.PHONE);
                final Integer gender = entity.getField(AddressItemFields.GENDER);
                final String preAddress = entity.getField(AddressItemFields.ADDRESS_PREFIX);
                final String sufAddress = entity.getField(AddressItemFields.ADDRESS_SUFFIX);
                final String houseNumber = entity.getField(AddressItemFields.HOUSE_NUMBER);
                final int tag = entity.getField(AddressItemFields.ADDRESS_TAG);
                holder.setText(R.id.tv_item_address_firstname, name);
                holder.setText(R.id.tv_item_address_house_number, houseNumber);
                holder.setText(R.id.tv_item_address_prefix, preAddress);
                if(gender == 0){
                    genderText = MALE;
                }else{
                    genderText = FEMALE;
                }
                holder.setText(R.id.tv_item_address_gender, genderText);
                holder.setText(R.id.tv_item_address_phone, phone);
                final ImageView editView = holder.getView(R.id.iv_item_address_edit);
                if(currentType == Type.ADDRESS){
                    editView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //编辑单个地址
                            if(DELEGATE != null){
                                DELEGATE.getSupportDelegate()
                                        .start(AddressEditDelegate.create(houseNumber, name, phone, preAddress, sufAddress, tag, gender));
                            }
                        }
                    });
                }else {
                    editView.setVisibility(View.GONE);
                }

            default:
                break;

        }
    }
    enum Type{
        ADDRESS,
        LOCATION
    }
}
