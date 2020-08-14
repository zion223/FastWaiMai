package com.zrp.latte.ec.main.sort.content;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.ui.tab.TabPagerAdapter;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ContentPageViewDelegate extends BottomItemDelegate {

    private static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";

    @BindView(R2.id.tl_order_status)
    TabLayout mTabLayout;
    @BindView(R2.id.vp_order)
    ViewPager mViewPager;


    private int mCategoryId = -1;

    private SectionBean mData = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_list_content;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mCategoryId = args.getInt(ARG_CATEGORY_ID);
        }
    }


    public static ContentPageViewDelegate newInstance(Integer categoryId) {
        final ContentPageViewDelegate contentPageViewDelegate = new ContentPageViewDelegate();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_CATEGORY_ID, categoryId);
        contentPageViewDelegate.setArguments(bundle);
        return contentPageViewDelegate;
    }


    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        super.onBindView(savedInstanceState, view);
        initData();
    }

    @SuppressLint("CheckResult")
    private void initData() {
        RestClient.builder()
                .url("api/content/" + mCategoryId)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {

                    @Override
                    public void accept(String response) throws Exception {
                        mData = new SectionDataConverter().convert(response);
                        final String[] mTitles = new String[mData.getHeaders().size()];
                        if(mData.getHeaders() != null){
                            mData.getHeaders().toArray(mTitles);
                        }
                        final int length = mTitles.length;
                        final List<Fragment> mFragments = new ArrayList<>(length);
                        for (int i = 0; i < length; i++) {
                            List<MultipleItemEntity> entity = mData.getDatas().get(i);
                            mFragments.add(new ContentDelegate().setEntities(entity));
                        }

                        final TabPagerAdapter adapter = new TabPagerAdapter(getActivity().getSupportFragmentManager(), mTitles, mFragments);

                        mViewPager.setAdapter(adapter);
                        mViewPager.setOffscreenPageLimit(length);

                        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                        mTabLayout.setBackgroundColor(Color.WHITE);
                        mTabLayout.setupWithViewPager(mViewPager);
                    }
                });

    }

}
