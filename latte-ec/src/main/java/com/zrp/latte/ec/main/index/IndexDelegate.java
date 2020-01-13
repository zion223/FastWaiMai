package com.zrp.latte.ec.main.index;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.joanzapata.iconify.widget.IconTextView;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;
import com.zrp.latte.app.Latte;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.ec.main.index.scaner.ScannerDelegate;
import com.zrp.latte.ec.main.index.spec.SpecZoneAdapter;
import com.zrp.latte.ec.main.index.spec.SpecZoneBean;
import com.zrp.latte.ec.main.index.spec.SpecZoneDataConverter;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.ui.camera.CameraRequestCodes;
import com.zrp.latte.ui.recycler.MultipleRecyclerAdapter;
import com.zrp.latte.ui.refresh.RefreshHandler;
import com.zrp.latte.ui.tab.TabPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;


public class IndexDelegate extends BottomItemDelegate implements View.OnFocusChangeListener {

    @BindView(R2.id.rv_index)
    RecyclerView mRecycleView;
    @BindView(R2.id.sr1_index)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R2.id.icon_index_scan)
    IconTextView mIconScan;
    @BindView(R2.id.icon_index_message)
    IconTextView mIconMessage;
    @BindView(R2.id.tb_index)
    Toolbar mToolBar;
    @BindView(R2.id.rv_index_spec)
    RecyclerView mSpecRecyclerView;
    @BindView(R2.id.tl_index_sort)
    TabLayout mTabLayout;
    @BindView(R2.id.vp_index_sort)
    ViewPager mViewPager;

    private RefreshHandler mRefreshHandler = null;
    private MultipleRecyclerAdapter mAdapter = null;

    private List<SpecZoneBean> mSpecData = null;

    @OnClick(R2.id.icon_index_scan)
    void onClickScan() {
        //扫描二维码
//        String[] perms = {Manifest.permission.CAMERA};
//        //EasyPermission中请求的权限需要在Manifest中申请
//        if (EasyPermissions.hasPermissions(Latte.getApplication(), perms)) {
//            getSupportDelegate().startForResult(new ScannerDelegate(), CameraRequestCodes.SCAN);
//        } else {
//            EasyPermissions.requestPermissions(this, "请打开相关权限", 1, perms);
//        }
        List<HotCity> hotCities = new ArrayList<>();
        hotCities.add(new HotCity("北京", "北京", "101010100")); //code为城市代码
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("杭州", "浙江", "101210101"));
        CityPicker.from(getActivity()) //activity或者fragment
                .enableAnimation(true)	//启用动画效果，默认无
                //.setAnimationStyle(anim)	//自定义动画
                //APP自身已定位的城市，传null会自动定位（默认）
                .setLocatedCity(null)
                .setHotCities(hotCities)	//指定热门城市
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        Toast.makeText(Latte.getApplication(), data.getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(){
                        Toast.makeText(Latte.getApplication(), "取消选择", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLocate() {
                        //定位接口，需要APP自身实现，这里模拟一下定位
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //定位完成之后更新数据
//                                CityPicker.getInstance()
//                                        .locateComplete(new LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS);
                            }
                        }, 3000);
                    }
                })
                .show();
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_index;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        mRefreshHandler = RefreshHandler.create(mRefreshLayout, mRecycleView, new IndexDataConverter());
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        initRecyclerView();
        initTabLayout();
        //加载广告数据和分类数据
        RestClient.builder()
                .url("api/home")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        mAdapter = MultipleRecyclerAdapter.create(new IndexDataConverter().setJsonData(response));
                        mAdapter.openLoadAnimation();
                        mRecycleView.setAdapter(mAdapter);
                    }
                })
                .build()
                .post();
        //加载特色专区数据
        RestClient.builder()
                .url("api/spec")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mSpecData = new SpecZoneDataConverter().convert(response);
                        final SpecZoneAdapter mSpecZoneAdapter = new SpecZoneAdapter(R.layout.item_multiple_spec, R.layout.item_multiple_spec_header, mSpecData);
                        mSpecRecyclerView.setAdapter(mSpecZoneAdapter);
                    }
                })
                .build()
                .post();


    }

    private void initTabLayout() {
        final String[] mTitles = {"全部", "晚餐", "人气", "必选"};

        final List<Fragment> mFragments = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mFragments.add(new IndexTabDelegate());
        }
        final TabPagerAdapter adapter = new TabPagerAdapter(getActivity().getSupportFragmentManager(), mTitles, mFragments);

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(4);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setBackgroundColor(Color.WHITE);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initRecyclerView() {
        //总的SpanCount大小10 通过spanSize进行填充
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 10);
        mRecycleView.setLayoutManager(manager);

        //添加分割线
        //mRecycleView.addItemDecoration(BaseDecoration.create(ContextCompat.getColor(getContext(), R.color.app_background), 5));

        //final EcBottomDelegate ecBottomDelegate = getParentDelegate();

        //传递this 跳转时有EcBottomDelegate 传递getParentDelegate():ecBottomDelegate 跳转时无EcBottomDelegate
        mRecycleView.addOnItemTouchListener(IndexItemClickListener.create(this));
		//瀑布流
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mSpecRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_light
        );
        mRefreshLayout.setProgressViewOffset(true, 120, 300);
    }


    /**
     * 跳转到 SearchDelegate
     *
     * @param view
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {

        }
    }

}
