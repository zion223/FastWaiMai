package com.zrp.latte.ec.main.sort.content;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.List;

import butterknife.BindView;

public class ContentEachDelegate extends BottomItemDelegate {


    @BindView(R2.id.rv_all_normal)
    RecyclerView recyclerView;


    private List<MultipleItemEntity> entities;

    public ContentEachDelegate setEntities(List<MultipleItemEntity> entities) {
        this.entities = entities;
        return this;
    }
    @Override
    public Object setLayout() {
        return R.layout.delegate_normal_recyclerview;
    }


    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        super.onBindView(savedInstanceState, view);

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        if(entities != null){
            final SectionAdapter mAdapter = new SectionAdapter(entities);
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

    }

}
