package com.zrp.latte.ec.main.detail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.zrp.latte.ui.banner.HolderCreator;
import com.zrp.latte.ui.widget.CircleTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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

    private String mGoodsThumbUrl = "http://img3m5.ddimg.cn/44/26/27903095-1_l_3.jpg";
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
        addShopToCart(circleImageView);
    }
    private void addShopToCart(final View imageView){
        final LinearLayout.LayoutParams params;
        int[] parentLocation = new int[2];
        int[] addLocation = new int[2];
        final float[] mCurrentPosition = new float[2];
        mRlAddShopCart.getLocationInWindow(addLocation);
        //240 475
        mRlAddShopCart.getLocationOnScreen(parentLocation);
        //75 240
        //像素
        final DisplayMetrics metrics = new DisplayMetrics();

        getSupportDelegate().getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int iconWidth = mIconShopCart.getWidth();
        final int iconHeidht = mIconShopCart.getHeight();
        final int height = mRlAddShopCart.getHeight();
        final int width = mRlAddShopCart.getWidth();
        float startX = 120;
        float startY = 480;
        int[] iconLocation = new int[2];
        //一个控件在其父窗口中的坐标位置
        mIconShopCart.getLocationInWindow(iconLocation);
        float toX = 100;
        float toY = 360;
        final Drawable drawable = ((ImageView)imageView).getDrawable();
        if(drawable != null){
            params = new LinearLayout.LayoutParams(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }else{
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        imageView.setLayoutParams(params);
        mRlAddShopCart.addView(imageView);
        Path path = new Path();
        //path.moveTo(startX, startY);
        path.moveTo(0, 0);


        path.cubicTo(width + width/2, height, 0, -1000,  -iconWidth,0);
        final PathMeasure mPathMeasure = new PathMeasure(path, false);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(500);
        //线性插值器:匀速动画
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                imageView.setTranslationX(mCurrentPosition[0]);
                imageView.setTranslationY(mCurrentPosition[1]);
            }
        });
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageView.setVisibility(View.INVISIBLE);
            }
        });

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
