package com.zrp.latte.ec.main.index;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.joanzapata.iconify.widget.IconTextView;
import com.zrp.latte.app.Latte;
import com.zrp.latte.delegates.bottom.BaseBottomDelegate;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.ec.main.EcBottomDelegate;
import com.zrp.latte.ec.main.index.scaner.ScannerDelegate;
import com.zrp.latte.ec.main.index.spec.SpecZoneAdapter;
import com.zrp.latte.ec.main.index.spec.SpecZoneBean;
import com.zrp.latte.ec.main.index.spec.SpecZoneDataConverter;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.IError;
import com.zrp.latte.net.callback.IFailure;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.ui.camera.CameraRequestCodes;
import com.zrp.latte.ui.recycler.MultipleRecyclerAdapter;
import com.zrp.latte.ui.refresh.RefreshHandler;

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

    private RefreshHandler mRefreshHandler = null;
    private MultipleRecyclerAdapter mAdapter = null;

    private List<SpecZoneBean> mSpecData = null;
    @OnClick(R2.id.icon_index_scan)
    void onClickScan() {
        //扫描二维码
        String[] perms = {Manifest.permission.CAMERA};
        //EasyPermission中请求的权限需要在Manifest中申请
        if (EasyPermissions.hasPermissions(Latte.getApplication(), perms)) {
            getSupportDelegate().startForResult(new ScannerDelegate(), CameraRequestCodes.SCAN);
        } else {
            EasyPermissions.requestPermissions(this, "请打开相关权限", 1, perms);
        }
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
        //加载广告数据
        RestClient.builder()
                .url("api/books")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //得到 page 和 pageSize 再次查询时  page + 1
                        final JSONObject object = JSONObject.parseObject(response);

                        mAdapter = MultipleRecyclerAdapter.create(new IndexDataConverter().setJsonData(response));
                        mAdapter.openLoadAnimation();
                        //加载更多 回调
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

    private void initRecyclerView() {
        //总的SpanCount大小4 通过spanSize进行填充
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 10);
        mRecycleView.setLayoutManager(manager);
        //添加分割线

        //mRecycleView.addItemDecoration(BaseDecoration.create(ContextCompat.getColor(getContext(), R.color.app_background), 5));

        //final EcBottomDelegate ecBottomDelegate = getParentDelegate();

        //传递this 跳转时有EcBottomDelegate 传递ecBottomDelegate 跳转时无EcBottomDelegate
        mRecycleView.addOnItemTouchListener(IndexItemClickListener.create(this));

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
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
