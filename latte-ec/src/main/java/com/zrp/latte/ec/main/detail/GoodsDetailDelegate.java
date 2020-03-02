package com.zrp.latte.ec.main.detail;

import android.graphics.Color;
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
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.joanzapata.iconify.widget.IconTextView;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ec.detail.GoodsInfoDelegate;
import com.zrp.latte.ec.detail.TabPagerAdapter;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.ui.animation.BazierAnimation;
import com.zrp.latte.ui.banner.HolderCreator;
import com.zrp.latte.ui.widget.CircleTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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
    @BindView(R2.id.tv_shopping_cart_amount)
    CircleTextView mCircleTextView;


    private int mGoodsId = -1;

    private final String mGoodsThumbUrl = "http://img3m5.ddimg.cn/44/26/27903095-1_l_3.jpg";
    private int mShopCount = 0;
    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate()
            .override(100, 100);

    @OnClick(R2.id.rl_add_shop_cart)
    public void onViewClicked() {
        final CircleImageView circleImageView = new CircleImageView(getContext());
        Glide.with(this)
                .load(mGoodsThumbUrl)
                .apply(OPTIONS)
                .into(circleImageView);
        mShopCount++;
        mCircleTextView.setVisibility(View.VISIBLE);
        mCircleTextView.setText(String.valueOf(mShopCount));
        float[] minPosition = new float[2];
        minPosition[0] = 0;
        minPosition[1] = -1000;
        BazierAnimation.addToShopCart(circleImageView, mRlAddShopCart, minPosition, mIconShopCart);
    }

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
        mCircleTextView.setCircleBackground(Color.RED);
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
        for (int i = 0; i < size; i++) {
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

    @OnClick(R2.id.icon_goods_back)
    public void onViewClickedReturn() {
        getSupportDelegate().pop();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

}
