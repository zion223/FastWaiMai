package com.zrp.latte.ec.main.index;

import android.Manifest;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.leaf.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zrp.latte.activities.ProxyActivity;
import com.zrp.latte.app.Latte;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.ec.main.index.location.LocationDelegate;
import com.zrp.latte.ec.main.index.spec.SpecZoneAdapter;
import com.zrp.latte.ec.main.index.spec.SpecZoneBean;
import com.zrp.latte.ec.main.index.spec.SpecZoneDataConverter;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.ui.recycler.MultipleRecyclerAdapter;
import com.zrp.latte.ui.refresh.CustomRefreshHeader;
import com.zrp.latte.ui.tab.TabPagerAdapter;
import com.zrp.latte.util.dimen.DimenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;

public class IndexDelegate extends BottomItemDelegate implements View.OnFocusChangeListener{

	@BindView(R2.id.rv_index)
	RecyclerView mBannerRecycleView;
	@BindView(R2.id.rl_index_search)
	RelativeLayout mRlSearch;
	@BindView(R2.id.app_bar_layout_index)
	AppBarLayout mAppBarLayout;
	@BindView(R2.id.iv_index_message)
	ImageView mIconMessage;
	@BindView(R2.id.tb_index)
	Toolbar mToolBar;
	@BindView(R2.id.rv_index_spec)
	RecyclerView mSpecRecyclerView;
	@BindView(R2.id.tl_index_sort)
	TabLayout mTabLayout;
	@BindView(R2.id.vp_index_sort)
	ViewPager mViewPager;
	@BindView(R2.id.tv_index_location)
	TextView mTvLocation;
	@BindView(R2.id.et_index_search)
	EditText mEtSearch;
	@BindView(R2.id.nestScrollView)
	NestedScrollView mNestScrollView;
	@BindView(R2.id.refresh_layout_index)
	SmartRefreshLayout mRefreshLayout;
	@BindView(R2.id.iv_cart)
	CircleImageView mIvCartView;

	private static final String TAG = "IndexDelegate";

	private MultipleRecyclerAdapter mAdapter = null;
	private List<SpecZoneBean> mSpecData = null;

	/**
	 * 地理位置
	 */
	private MyLocationListener myListener = new MyLocationListener();
	private LocationClient mLocationClient = null;
	private LocationClientOption option = null;

	/**
	 * 购物车图案UI
	 */
	private float startY; //上下滑动的距离
	private int moveDistance;//动画移动的距离
	private volatile boolean isShowFloatImage = true;//标记图片是否显示
	private Timer timer;//计时器
	private long upTime;//记录抬起的时间

	/**
	 * 需要动态申请的权限
	 */
	private final String[]  perms = {Manifest.permission.READ_PHONE_STATE
			, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
			, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

	@OnClick(R2.id.tv_index_location)
	void onClickLocation() {

		getParentDelegate().getSupportDelegate().startForResult(
				LocationDelegate.create(mTvLocation.getText().toString()), 11);

	}

	@Override
	public Object setLayout() {
		return R.layout.delegate_index;
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
		//渐变式状态栏
		StatusBarUtil.setGradientColor(getProxyActivity(), mToolBar);
		initNormalView();
		initRecyclerView();
		initData();
		if (EasyPermissions.hasPermissions(Latte.getApplication(), perms)) {
			initLocation();
		}else{
			EasyPermissions.requestPermissions(this, "请打开相关权限", 1, perms);
		}

	}


	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);
		initTouchListener();
		initTabLayout();
	}

	private void initNormalView() {
		mRefreshLayout.setRefreshHeader(new CustomRefreshHeader(getActivity()));
		//下拉高度
		mRefreshLayout.setHeaderHeight(80);
		mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
			@Override
			public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
				mRefreshLayout.finishLoadMore(2000, true, false);
			}

			@Override
			public void onRefresh(@NonNull RefreshLayout refreshLayout) {
				mRefreshLayout.finishRefresh(2000, true, false);
			}
		});
		mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
			@Override
			public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

				int toolBarHeight = mToolBar.getHeight();
				//Log.e(TAG, "verticalOffset: " + verticalOffset +" toolBarHeight:"+toolBarHeight);
				if (Math.abs(verticalOffset) <= toolBarHeight) {
					mToolBar.setAlpha(1.0f - Math.abs(verticalOffset) * 1.0f / toolBarHeight);
				}
			}
		});
		mNestScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
			@Override
			public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
				if (scrollY > oldScrollY) {

					//Log.e(TAG, "上滑" + "scrollY " + scrollY + " oldScrollY " + oldScrollY );

//					mToolBar.setBackgroundColor(Color.WHITE);
//					mRlSearch.setBackgroundColor(Color.WHITE);
//					mToolBar.setVisibility(View.VISIBLE);
				}
				if (scrollY < oldScrollY) {

					//Log.e(TAG, "下滑" + "scrollY" + scrollY + ":" + "oldScrollY" + oldScrollY);
//					mRlSearch.setBackground(getResources().getDrawable(R.drawable.index_toorbar_backgroundtwo));
//					mToolBar.setBackground(getResources().getDrawable(R.drawable.index_toorbar_background));
//					mToolBar.setVisibility(View.GONE);

				}

				if (scrollY == 0) {
					//Log.e(TAG, "滑到顶部");

//					mToolBar.setBackground(getResources().getDrawable(R.drawable.index_toorbar_background));
//					mToolBar.setVisibility(View.VISIBLE);

				}

				if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
					//Log.e(TAG, "滑到底部");
				}
			}
		});

		mIvCartView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				moveDistance = DimenUtil.getScreenWidth() - mIvCartView.getRight() + mIvCartView.getWidth() / 2;
				mIvCartView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
			}
		});

	}

	private void initTouchListener() {
		getProxyActivity().setListener(new ProxyActivity.OnTouchListener() {
			@Override
			public void onTouch(MotionEvent event) {
				switch (event.getAction()){
					//手指按下
					case MotionEvent.ACTION_DOWN:
						Log.d(TAG, "onTouch: ACTION_DOWN");
						if(System.currentTimeMillis() - upTime > 1000){
							if(timer != null){
								timer.cancel();
							}
						}
						startY = event.getY();
						break;
					//手指滑动 状态
					case MotionEvent.ACTION_MOVE:
						Log.d(TAG, "onTouch: ACTION_MOVE");
						if(Math.abs(startY - event.getY()) > 10){
							if(isShowFloatImage){
								hideFloatImage(moveDistance);
							}
						}
						startY = event.getY();
						break;
					//手指抬起后 1s后显示悬浮按钮
					case MotionEvent.ACTION_UP:
						Log.d(TAG, "onTouch: ACTION_UP");
						if(!isShowFloatImage){
							upTime = System.currentTimeMillis();
							timer = new Timer();
							timer.schedule(new FloatTask(), 1000);
						}
						break;
				}
			}
		});
	}

	private void initTabLayout() {
		final String[] mTitles = {"全部", "晚餐", "人气", "必选"};

		final List<Fragment> mFragments = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			mFragments.add(new IndexTabDelegate());
		}
		final TabPagerAdapter adapter = new TabPagerAdapter(getChildFragmentManager(), mTitles, mFragments);

		mViewPager.setAdapter(adapter);
		mViewPager.setOffscreenPageLimit(4);
		mTabLayout.setTabMode(TabLayout.MODE_FIXED);
		mTabLayout.setBackgroundColor(Color.WHITE);
		mTabLayout.setupWithViewPager(mViewPager);
	}

	private void initRecyclerView() {
		//总的SpanCount大小10 通过spanSize进行填充
		final GridLayoutManager manager = new GridLayoutManager(getContext(), 10);
		mBannerRecycleView.setLayoutManager(manager);

		//添加分割线
		//mBannerRecycleView.addItemDecoration(BaseDecoration.create(ContextCompat.getColor(getContext(), R.color.app_background), 5));

		//传递this 跳转时有EcBottomDelegate 传递getParentDelegate():ecBottomDelegate 跳转时无EcBottomDelegate
		mBannerRecycleView.addOnItemTouchListener(IndexItemClickListener.create(this));
		//瀑布流
		final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
		mSpecRecyclerView.setLayoutManager(staggeredGridLayoutManager);
	}

	private void initData() {
		//加载广告数据和分类数据
		RestClient.builder()
				.url("api/home")
				.success(new ISuccess() {
					@Override
					public void onSuccess(String response) {

						mAdapter = MultipleRecyclerAdapter.create(new IndexDataConverter().setJsonData(response));
						mAdapter.openLoadAnimation();
						mBannerRecycleView.setAdapter(mAdapter);
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

	/**
	 * 跳转到 SearchDelegate
	 */
	@Override
	public void onFocusChange(View view, boolean hasFocus) {

	}

	@Override
	public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
		final String address = data.getString("address");
		mTvLocation.setText(address);
	}

	//进入购物车界面
	@OnClick(R2.id.iv_cart)
	void onClickViewCart(){
		//getParentDelegate() : EcBottomDelegate
		//切换Delegate

	}



	private class FloatTask extends TimerTask{
		@Override
		public void run() {
			//ui线程中执行
			Latte.getHandler().post(new Runnable() {
				@Override
				public void run() {
					showFloatImage();
				}
			});
		}
	}

	private void showFloatImage() {
		isShowFloatImage = true;
		TranslateAnimation ta = new TranslateAnimation(moveDistance,0,0,0);
		ta.setDuration(300);

		AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
		aa.setDuration(300);

		AnimationSet set = new AnimationSet(true);
		//动画完成后不回到原位  true:persist 默认值为false
		set.setFillAfter(true);
		set.addAnimation(ta);
		set.addAnimation(aa);
		mIvCartView.startAnimation(set);
	}

	private void hideFloatImage(int moveDistance) {
		isShowFloatImage = false;
		TranslateAnimation ta = new TranslateAnimation(0, moveDistance,0,0);
		ta.setDuration(300);

		AlphaAnimation aa = new AlphaAnimation(1.0f, 0.5f);
		aa.setDuration(300);

		AnimationSet set = new AnimationSet(true);
		//动画完成后不回到原位  true:persist 默认值为false
		set.setFillAfter(true);
		set.addAnimation(ta);
		set.addAnimation(aa);
		mIvCartView.startAnimation(set);
	}

	public class MyLocationListener extends BDAbstractLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null){
				return;
			}
			mTvLocation.setText(location.getAddrStr().substring(5));
		}
	}

	private void initLocation() {
		// 声明LocationClient类
		mLocationClient = new LocationClient(getContext());
		option = new LocationClientOption();
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setOpenGps(true);
		option.setLocationNotify(true);
		option.setIgnoreKillProcess(true);
		option.SetIgnoreCacheException(false);
		option.setEnableSimulateGps(false);
		option.setIsNeedLocationDescribe(true);
		option.setIsNeedLocationPoiList(true);
		option.setIsNeedAddress(true);
		//设置扫描间隔
//        option.setScanSpan(10000);

		mLocationClient.setLocOption(option);
		// 注册监听函数
		mLocationClient.registerLocationListener(myListener);
		mLocationClient.start();
	}


}
