package com.zrp.latte.ec.main.detail;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.joanzapata.iconify.widget.IconTextView;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ec.detail.GoodsInfoDelegate;
import com.zrp.latte.ec.detail.TabPagerAdapter;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.ui.banner.HolderCreator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class GoodsDetailDelegate extends LatteDelegate implements AppBarLayout.OnOffsetChangedListener {


    private static final String ARGS_GOODS_ID = "ARGS_GOODS_ID";

    @BindView(R2.id.detail_banner)
    ConvenientBanner mBanner;
    @BindView(R2.id.frame_goods_info)
    ContentFrameLayout mFrameGoodsInfo;
    @BindView(R2.id.tv_detail_title_text)
    AppCompatTextView mTvDetailTitleText;
    @BindView(R2.id.goods_detail_toolbar)
    Toolbar mGoodsDetailToolbar;
    @BindView(R2.id.collapsing_toolbar_detail)
    CollapsingToolbarLayout mCollapsingToolbarDetail;
    @BindView(R2.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R2.id.app_bar_detail)
    AppBarLayout mAppBar;
    @BindView(R2.id.view_pager)
    ViewPager mViewPager;
    @BindView(R2.id.icon_favor)
    IconTextView mIconFavor;
    @BindView(R2.id.rl_favor)
    RelativeLayout mRlFavor;
    @BindView(R2.id.icon_shop_cart)
    IconTextView mIconShopCart;
    @BindView(R2.id.rl_shop_cart)
    RelativeLayout mRlShopCart;
    @BindView(R2.id.rl_add_shop_cart)
    RelativeLayout mRlAddShopCart;
    @BindView(R2.id.ll_bottom)
    LinearLayoutCompat mLlBottom;


    private int mGoodsId = -1;

    public static GoodsDetailDelegate create(int goodsId) {
        final Bundle args = new Bundle();
        args.putInt(ARGS_GOODS_ID, goodsId);
        final GoodsDetailDelegate delegate = new GoodsDetailDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_goods_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        mCollapsingToolbarDetail.setContentScrimColor(Color.WHITE);
        mAppBar.addOnOffsetChangedListener(this);
        initData();
        initTabLayout();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mGoodsId = args.getInt(ARGS_GOODS_ID);
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    private void initData() {
        RestClient.builder()
                .url("api/goodsdetail")
                //.parmas("goodsId", mGoodsId)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //绑定数据
                        final JSONObject data = JSON.parseObject(response).getJSONObject("data");
                        initBanner(data);
                        initGoodsInfo(data);
                        initPager(data);
                    }
                })
                .build()
                .get();
    }

    private void initBanner(JSONObject data) {

        final JSONArray bannerArray = data.getJSONArray("banners");
        final ArrayList<String> banners = new ArrayList<>();
        final int size = bannerArray.size();
        for(int i=0; i<size; i++){
            final String banner = bannerArray.getString(i);
            banners.add(banner);
        }
        mBanner.setPages(new HolderCreator(), banners)
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_normal})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setPageTransformer(new DefaultTransformer())
                .startTurning(3000)
                .setCanLoop(true);
    }

    private void initGoodsInfo(JSONObject data) {
        final String goodsdata = data.toJSONString();
        getSupportDelegate().loadRootFragment(R.id.frame_goods_info, GoodsInfoDelegate.create(goodsdata));

    }

    private void initPager(JSONObject data) {
        final TabPagerAdapter adapter = new TabPagerAdapter(getFragmentManager(), data);
        mViewPager.setAdapter(adapter);
    }

    private void initTabLayout() {
        //固定不能滑动,标签多时会被挤压
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setBackgroundColor(Color.WHITE);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }


    @OnClick(R2.id.icon_goods_back)
    public void onViewClickedReturn() {
        getSupportDelegate().pop();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }
}
