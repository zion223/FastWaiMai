package com.zrp.latte.ui.refresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zrp.latte.app.Latte;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.IError;
import com.zrp.latte.net.callback.IFailure;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.ui.recycler.DataConverter;
import com.zrp.latte.ui.recycler.MultipleRecyclerAdapter;

public class RefreshHandler implements
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {


    private final SwipeRefreshLayout REFRESH_LAYOUT;
    private final RecyclerView RECYCLERVIEW;
    private final DataConverter CONVERTER;
    private final PagingBean BEAN;

    private MultipleRecyclerAdapter mAdapter = null;

    private RefreshHandler(SwipeRefreshLayout refreshlayout,
                           RecyclerView recyclerView,
                           DataConverter converter, PagingBean bean) {
        REFRESH_LAYOUT = refreshlayout;
        BEAN = bean;
        RECYCLERVIEW = recyclerView;
        CONVERTER = converter;
        REFRESH_LAYOUT.setOnRefreshListener(this);
    }
    public static RefreshHandler create(SwipeRefreshLayout swipeRefreshLayout,
                                        RecyclerView recyclerView, DataConverter converter) {
        return new RefreshHandler(swipeRefreshLayout, recyclerView, converter, new PagingBean());
    }

    private void refresh(){
        REFRESH_LAYOUT.setRefreshing(true);
        Latte.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                REFRESH_LAYOUT.setRefreshing(false);
            }
        },1000);
    }

    @Override
    public void onRefresh() {
        //refresh();
    }

    /**
     * 首页的数据
     */
    public void firstPage(String url,Integer size){
        BEAN.setPageSize(size);
        RestClient.builder()
                .url(url)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //得到 page 和 pageSize 再次查询时  page + 1
                        final JSONObject object = JSONObject.parseObject(response);
                        BEAN.setTotal(object.getIntValue("total"));
                        BEAN.setPageIndex(object.getIntValue("page"));
                        BEAN.setCurrentCount(object.getIntValue("size"));

                        mAdapter = MultipleRecyclerAdapter.create(CONVERTER.setJsonData(response));
                        mAdapter.openLoadAnimation();
                        //加载更多 回调
                        //mAdapter.setOnLoadMoreListener(RefreshHandler.this,RECYCLERVIEW);
                        RECYCLERVIEW.setAdapter(mAdapter);
                        BEAN.addIndex();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        Toast.makeText(Latte.getApplication(),message,Toast.LENGTH_LONG).show();
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Toast.makeText(Latte.getApplication(),"onFailure",Toast.LENGTH_LONG).show();
                    }
                })
                .build()
                .post();
    }

    /**
     * 上滑加载更多数据 需被多次调用
     */
    private void loadMoreData(){
        final int pageSize = BEAN.getPageSize();
        final int currentCount = BEAN.getCurrentCount();
        final int total = BEAN.getTotal();
        final int index = BEAN.getPageIndex();
        if(mAdapter.getData().size() < pageSize || (index * pageSize) - total > pageSize){
            Log.d("RefreshHandler","loadMoreEnd");
            // Refresh end, no more data
            mAdapter.loadMoreEnd(true);
        }else{
            Latte.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("RefreshHandler","onLoadMoreRequested "+" currentData:"+ mAdapter.getData().size()
                            +" pageIndex:"+index +" pageSize:"+ pageSize+ " total:"+total);
                    RestClient.builder()
                            .url("api/books")
                            .success(new ISuccess() {
                                @Override
                                public void onSuccess(String response) {
                                    //得到 page 和 pageSize 再次查询时  page + 1
                                    CONVERTER.clearData();
                                    mAdapter.addData(CONVERTER.setJsonData(response).convert());
                                    RECYCLERVIEW.setAdapter(mAdapter);
                                    mAdapter.loadMoreComplete();
                                    BEAN.setCurrentCount(mAdapter.getData().size());
                                    BEAN.addIndex();
                                }
                            })
                            .error(new IError() {
                                @Override
                                public void onError(int code, String message) {
                                    Toast.makeText(Latte.getApplication(),"没有更多数据了",Toast.LENGTH_LONG).show();
                                }
                            })
                            .failure(new IFailure() {
                                @Override
                                public void onFailure() {
                                    Toast.makeText(Latte.getApplication(),"没有更多数据了",Toast.LENGTH_LONG).show();
                                }
                            })
                            .build()
                            .post();

                }
            },1000);

        }

    }

    @Override
    public void onLoadMoreRequested() {
        //loadMoreData();
    }
}
