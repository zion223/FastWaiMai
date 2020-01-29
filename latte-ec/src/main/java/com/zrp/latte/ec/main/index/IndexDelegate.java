package com.zrp.latte.ec.main.index;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.ec.main.index.location.LocationDelegate;
import com.zrp.latte.ec.main.index.spec.SpecZoneAdapter;
import com.zrp.latte.ec.main.index.spec.SpecZoneBean;
import com.zrp.latte.ec.main.index.spec.SpecZoneDataConverter;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.ui.recycler.MultipleRecyclerAdapter;
import com.zrp.latte.ui.recycler.RgbValue;
import com.zrp.latte.ui.tab.TabPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class IndexDelegate extends BottomItemDelegate implements View.OnFocusChangeListener {

	@BindView(R2.id.rv_index)
	RecyclerView mRecycleView;
	@BindView(R2.id.rl_index_search)
	RelativeLayout mRlSearch;
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

	private MultipleRecyclerAdapter mAdapter = null;
	private final RgbValue RGB_VALUE = RgbValue.create(255,69,0);
	private List<SpecZoneBean> mSpecData = null;
	private static final int LOCATION_CODE = 11;

	private MyLocationListener myListener = new MyLocationListener();
	public LocationClient mLocationClient = null;
	private LocationClientOption option = null;

	@OnClick(R2.id.tv_index_location)
	void onClickLocation() {

		getParentDelegate().getSupportDelegate().startForResult(LocationDelegate.create(mTvLocation.getText().toString()), LOCATION_CODE);

		//扫描二维码
//        String[] perms = {Manifest.permission.CAMERA};
//        //EasyPermission中请求的权限需要在Manifest中申请
//        if (EasyPermissions.hasPermissions(Latte.getApplication(), perms)) {
//            getSupportDelegate().startForResult(new ScannerDelegate(), CameraRequestCodes.SCAN);
//        } else {
//            EasyPermissions.requestPermissions(this, "请打开相关权限", 1, perms);
//        }

	}


	@Override
	public Object setLayout() {
		return R.layout.delegate_index;
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
		//mRefreshHandler = RefreshHandler.create(mRefreshLayout, mRecycleView, new IndexDataConverter());
		mEtSearch.setHint(getResources().getString(R.string.indexsearch));
		mNestScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
			@Override
			public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
				if (scrollY > oldScrollY) {
					Log.e("=====", "下滑" + "scrollY" + scrollY + "oldScrollY" + oldScrollY);

					mToolBar.setBackgroundColor(Color.WHITE);
					mRlSearch.setBackgroundColor(Color.WHITE);
					mToolBar.setVisibility(View.VISIBLE);
				}
				if (scrollY < oldScrollY) {
					Log.e("=====", "上滑");
					mRlSearch.setBackground(getResources().getDrawable(R.drawable.index_toorbar_backgroundtwo));
					mToolBar.setBackground(getResources().getDrawable(R.drawable.index_toorbar_background));
					mToolBar.setVisibility(View.GONE);
				}

				if (scrollY == 0) {
					Log.e("=====", "滑到顶部");
					mToolBar.setBackground(getResources().getDrawable(R.drawable.index_toorbar_background));
					mToolBar.setVisibility(View.VISIBLE);
				}

				if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
					Log.e("=====", "滑到底部");
				}
			}
		});

		initLocation();

	}


	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);
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

//    private void initRefreshLayout() {
//        mRefreshLayout.setColorSchemeResources(
//                android.R.color.holo_blue_light,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_red_light
//        );
//        mRefreshLayout.setProgressViewOffset(true, 120, 300);
//    }


	/**
	 * 跳转到 SearchDelegate
	 *
	 * @param view
	 * @param hasFocus
	 */
	@Override
	public void onFocusChange(View view, boolean hasFocus) {

	}

	@Override
	public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
		final String address = data.getString("address");
		mTvLocation.setText(address);
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

		// 利用LocationClientOption类配置定位SDK参数
		option = new LocationClientOption();
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		// 可选，设置定位模式，默认高精度  LocationMode.Hight_Accuracy：高精度； LocationMode. Battery_Saving：低功耗； LocationMode. Device_Sensors：仅使用设备；

		option.setCoorType("bd09ll");
		// 可选，设置返回经纬度坐标类型，默认gcj02
		// gcj02：国测局坐标；
		// bd09ll：百度经纬度坐标；
		// bd09：百度墨卡托坐标；
		// 海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

		option.setOpenGps(true);
		// 可选，设置是否使用gps，默认false
		// 使用高精度和仅用设备两种定位模式的，参数必须设置为true

		option.setLocationNotify(true);
		// 可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

		option.setIgnoreKillProcess(true);
		// 可选，定位SDK内部是一个service，并放到了独立进程。
		// 设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

		option.SetIgnoreCacheException(false);
		// 可选，设置是否收集Crash信息，默认收集，即参数为false

		option.setEnableSimulateGps(false);
		// 可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

		option.setIsNeedLocationDescribe(true);
		// 可选，是否需要位置描述信息，默认为不需要，即参数为false

		option.setIsNeedLocationPoiList(true);
		// 可选，是否需要周边POI信息，默认为不需要，即参数为false

		option.setIsNeedAddress(true);// 获取详细信息
		//设置扫描间隔
//        option.setScanSpan(10000);

		mLocationClient.setLocOption(option);
		// 注册监听函数
		mLocationClient.registerLocationListener(myListener);
		mLocationClient.start();
	}


}
