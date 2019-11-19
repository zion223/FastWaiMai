package com.zrp.latte.ec.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ui.recycler.ItemType;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

import butterknife.BindView;


public class ImageDelegate extends LatteDelegate {

    private static final String ARGS_PICTURE = "ARGS_PICTURE";

    @BindView(R2.id.rv_image_contatiner)
    RecyclerView mRecyclerView;

    public static ImageDelegate create(ArrayList<String> pictures) {
        final Bundle args = new Bundle();
        args.putStringArrayList(ARGS_PICTURE, pictures);
        final ImageDelegate delegate = new ImageDelegate();
        delegate.setArguments(args);
        return delegate;
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_image;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initImages();
    }

    private void initImages() {
        final ArrayList<String> pictures = getArguments().getStringArrayList(ARGS_PICTURE);
        final ArrayList<MultipleItemEntity> entities = new ArrayList<>();
        final int size = pictures.size();
        for(int i=0; i<size; i++){
            final String pic = pictures.get(i);
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(ItemType.SINGLE_BIG_IMAGE)
                    .setField(MultipleFields.IMAGE_URL, pic)
                    .build();
            entities.add(entity);
        }
        final RecyclerImageAdapter adapter = new RecyclerImageAdapter(entities);
        mRecyclerView.setAdapter(adapter);

    }

}
