package com.zrp.latte.ec.main.index.location;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zrp.latte.app.Latte;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ec.main.personal.p_function.address.AddressAdapter;
import com.zrp.latte.ec.main.personal.p_function.address.AddressDataConverter;
import com.zrp.latte.ec.main.personal.p_function.address.AddressItemFields;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.ui.recycler.DataConverter;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LocationDelegate extends LatteDelegate implements OnGetPoiSearchResultListener {

	@BindView(R2.id.tv_index_location_city)
	TextView mTvLocationCity;
	@BindView(R2.id.tv_location_detail)
	TextView mTvLocationDetail;
	@BindView(R2.id.rv_location_address)
	RecyclerView mRvAddress;
	@BindView(R2.id.rv_location_around)
	RecyclerView mRvRoundAddress;
	@BindView(R2.id.mv_location_hidden)
	MapView mMapView;


	private AddressAdapter mAdapter = null;

	//地图定位
	private MyLocationListener myListener = new MyLocationListener();
	public LocationClient mLocationClient = null;
	private LocationClientOption option = null;
	private LatLng currentLatLng;//当前所在位置

	//poi检索
	private GeoCoder mGeoCoder;//反向地理解析，获取周边poi
	private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;//地点检索输入提示检索（Sug检索）
	private LatLng center;//地图中心点坐标
	private int radius = 600;//poi检索半径
	private int loadIndex = 0;//分页页码（分页功能我就不写了，常用的东西，你们自个加吧）
	private int pageSize = 50;
	private List<PoiInfo> poiInfoListForGeoCoder = new ArrayList<>();//地理反向解析获取的poi
	private List<PoiInfo> poiInfoListForSearch = new ArrayList<>();//检索结果集合

	private NearbyAddressAdapter mSearchAddressAdapter;
	private String cityName = "";
	private String keyword = "";


	private static final String ARGS_LOCATION_DETAIL = "ARGS_LOCATION_DETAIL";

	public static LocationDelegate create(String address) {
		final Bundle args = new Bundle();
		args.putString(ARGS_LOCATION_DETAIL, address);
		final LocationDelegate delegate = new LocationDelegate();
		delegate.setArguments(args);
		return delegate;
	}


	@Override
	public Object setLayout() {
		return R.layout.delegate_location;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) throws Exception {

		assert getArguments() != null;
		final String detail = getArguments().getString(ARGS_LOCATION_DETAIL);
		mTvLocationDetail.setText(detail);
		//我的收货地址
		initMyAddress();

		//附近的地址
		initGeoCoder();
		initPoiSearch();

		//建议搜索
		//initSuggestionSearch();
		initLocation();

	}

	private void initMyAddress() {
		//我的收货地址
		final LinearLayoutManager manager = new LinearLayoutManager(getContext());
		mRvAddress.setLayoutManager(manager);
		RestClient.builder()
				.url("api/address")
				.success(new ISuccess() {
					@Override
					public void onSuccess(String response) {
						final DataConverter converter =
								new AddressDataConverter().setJsonData(response);
						mAdapter = new AddressAdapter(converter.convert());
						mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
							@Override
							public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
								final MultipleItemEntity entity = (MultipleItemEntity) adapter.getItem(position);
								assert entity != null;
								final String address = entity.getField(AddressItemFields.ADDRESS_PREFIX);
								final Bundle bundle = new Bundle();
								bundle.putString("address", address);
								getSupportDelegate().setFragmentResult(RESULT_OK, bundle);
								getSupportDelegate().onDestroy();
								getSupportDelegate().pop();
							}
						});
						mRvAddress.setAdapter(mAdapter);
					}
				})
				.build()
				.get();
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

	private void initGeoCoder() {
		mGeoCoder = GeoCoder.newInstance();
		mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			//地理编码检索监听器
			@Override
			public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

			}

			//逆地理编码检索监听器
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
				if (poiInfoListForGeoCoder != null) {
					poiInfoListForGeoCoder.clear();
				}
				if (reverseGeoCodeResult.error.equals(SearchResult.ERRORNO.NO_ERROR)) {
					//获取城市
					ReverseGeoCodeResult.AddressComponent addressComponent = reverseGeoCodeResult.getAddressDetail();
					cityName = addressComponent.city;
					//获取poi列表
					if (reverseGeoCodeResult.getPoiList() != null) {
						poiInfoListForGeoCoder = reverseGeoCodeResult.getPoiList();
					}
				}
				initGeoCoderListView();
			}
		});
	}
	private void initGeoCoderListView() {
		mSearchAddressAdapter = new NearbyAddressAdapter(poiInfoListForGeoCoder);
		mSearchAddressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				final PoiInfo info = (PoiInfo) adapter.getItem(position);

				Toast.makeText(Latte.getApplication(), "选中了" + info.getName() + info.getAddress(),Toast.LENGTH_LONG).show();

			}
		});
		final LinearLayoutManager manager = new LinearLayoutManager(getContext());
		mRvRoundAddress.setLayoutManager(manager);
		mRvRoundAddress.setAdapter(mSearchAddressAdapter);
	}

	private void initPoiSearch() {
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
	}

	@OnClick(R2.id.iv_location_backarrow)
	public void onViewClickedBack(View view) {
		getSupportDelegate().pop();
	}

	@OnClick(R2.id.tv_location_relocation)
	public void onClickReLocation(View view){
		mLocationClient.start();
	}

	@OnClick(R2.id.tv_index_location_city)
	public void onClickPickCity(View view) {

		final List<HotCity> hotCities = new ArrayList<>();
		hotCities.add(new HotCity("北京", "北京", "101010100")); //code为城市代码
		hotCities.add(new HotCity("上海", "上海", "101020100"));
		hotCities.add(new HotCity("广州", "广东", "101280101"));
		hotCities.add(new HotCity("深圳", "广东", "101280601"));
		hotCities.add(new HotCity("杭州", "浙江", "101210101"));
		assert getActivity() != null;
		CityPicker.from(getActivity()) //activity或者fragment
				.enableAnimation(true)    //启用动画效果，默认无
				//.setAnimationStyle(anim)	//自定义动画
				//APP自身已定位的城市，传null会自动定位（默认）
				.setLocatedCity(null)
				.setHotCities(hotCities)    //指定热门城市
				.setOnPickListener(new OnPickListener() {
					@Override
					public void onPick(int position, City data) {
						mTvLocationCity.setText(data.getName());
					}

					@Override
					public void onCancel() {
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
	public void onGetPoiResult(PoiResult poiResult) {
		if (poiInfoListForSearch != null) {
			poiInfoListForSearch.clear();
		}
		if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(getContext(), "未找到结果", Toast.LENGTH_LONG).show();
			//initPoiSearchListView();
			return;
		}

		if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
			poiInfoListForSearch = poiResult.getAllPoi();
			//showSeachView();
			//initPoiSearchListView();
			return;
		}

		if (poiResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
			// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
			String strInfo = "在";

			for (CityInfo cityInfo : poiResult.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}

			strInfo += "找到结果";
			Toast.makeText(getContext(), strInfo, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
		if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getContext(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getContext(),
					poiDetailResult.getName() + ": " + poiDetailResult.getAddress(),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
		if (poiDetailSearchResult.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getContext(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		} else {
			List<PoiDetailInfo> poiDetailInfoList = poiDetailSearchResult.getPoiDetailInfoList();
			if (null == poiDetailInfoList || poiDetailInfoList.isEmpty()) {
				Toast.makeText(getContext(), "抱歉，检索结果为空", Toast.LENGTH_SHORT).show();
				return;
			}

			for (int i = 0; i < poiDetailInfoList.size(); i++) {
				PoiDetailInfo poiDetailInfo = poiDetailInfoList.get(i);
				if (null != poiDetailInfo) {
					Toast.makeText(getContext(),
							poiDetailInfo.getName() + ": " + poiDetailInfo.getAddress(),
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@Override
	public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

	}

	public class MyLocationListener extends BDAbstractLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null){
				return;
			}

			if (location.getLocType() == BDLocation.TypeGpsLocation
					|| location.getLocType() == BDLocation.TypeNetWorkLocation) {

				final String currentAddr = location.getAddrStr();
				currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
				mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
						.location(currentLatLng)
						.radius(radius));
				mTvLocationDetail.setText(currentAddr.substring(5));
			}

		}
	}
}
