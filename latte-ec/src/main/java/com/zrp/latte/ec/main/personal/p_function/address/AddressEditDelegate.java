package com.zrp.latte.ec.main.personal.p_function.address;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
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
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;


import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ec.main.personal.p_function.address.edit.SearchAddressAdapter;
import com.zrp.latte.ui.widget.ClearEditText;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddressEditDelegate extends LatteDelegate implements OnGetPoiSearchResultListener {

	private static final String TAG = "AddressEditDelegate";
	@BindView(R2.id.btn_address_edit_save)
	AppCompatButton mBtnSave;
	@BindView(R2.id.edit_address_edit_name)
	ClearEditText mAddressName;
	@BindView(R2.id.edit_address_edit_phone)
	ClearEditText mAddressPhone;
	@BindView(R2.id.tv_address_edit_house_number)
	ClearEditText mAddressHouseNumber;
	@BindView(R2.id.mv_address_edit_map)
	MapView mMapView;
	@BindView(R2.id.btn_address_edit_delete)
	TextView mTvDeleteAddress;
	@BindView(R2.id.tv_address_edit)
	TextView mTvEditAddress;
	@BindView(R2.id.cv_address_edit_bottom)
	CardView mCardViewBottom;
	@BindView(R2.id.cv_address_edit_top)
	CardView mCardViewTop;
	@BindView(R2.id.ll_address_edit_input)
	LinearLayout mLlInputText;
	@BindView(R2.id.tv_address_edit_city)
	TextView mTvCurrrentCity;
	@BindView(R2.id.et_address_edit_keyword)
	EditText mEtSearchKeyword;
	@BindView(R2.id.lv_address_edit_search)
	RecyclerView mLvSearchAddress;
	@BindView(R2.id.cv_address_edit_search)
	CardView mCardViewSearchAddress;
	@BindView(R2.id.lv_address_edit_poiSearch)
	RecyclerView mLvPoiSearchAddress;
	@BindView(R2.id.ll_address_edit_poisearch)
	LinearLayout mLlPoiSearchAddress;
	@BindView(R2.id.tv_address_edit_detail_pre)
	TextView mTvAddressPrefix;
	@BindView(R2.id.tv_address_edit_detail_suf)
	TextView mTvAddressSuffix;
	@BindView(R2.id.radioBtn_address_edit_male)
	RadioButton mRadioBtnMale;
	@BindView(R2.id.radioBtn_address_edit_female)
	RadioButton mRadioBtnFemale;
	@BindView(R2.id.tv_address_edit_company_tag)
	TextView mTvCompanyTag;
	@BindView(R2.id.tv_address_edit_school_tag)
	TextView mTvSchoolTag;
	@BindView(R2.id.tv_address_edit_home_tag)
	TextView mTvHomeTag;

	private String addressPre = null;
	private String addressSuf = null;
	private String houseNumber = null;
	private Integer tag = null;
	private String name = null;
	private Integer gender = null;
	private String phone = null;

	//标签
	Drawable address_tag_border;
	Drawable address_tag_border_choosed;

	//地图定位
	private BaiduMap mBaiduMap;
	private MyLocationListener myListener = new MyLocationListener();
	public LocationClient mLocationClient = null;
	private LocationClientOption option = null;
	private boolean isFirstLocation = true;
	private LatLng currentLatLng;//当前所在位置
	private Marker marker;//地图标注

	//poi检索
	private GeoCoder mGeoCoder;//反向地理解析，获取周边poi
	private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;//地点检索输入提示检索（Sug检索）
	private LatLng center;//地图中心点坐标
	private int radius = 300;//poi检索半径
	private int loadIndex = 0;//分页页码（分页功能我就不写了，常用的东西，你们自个加吧）
	private int pageSize = 50;
	private List<PoiInfo> poiInfoListForGeoCoder = new ArrayList<>();//地理反向解析获取的poi
	private List<PoiInfo> poiInfoListForSearch = new ArrayList<>();//检索结果集合

	private SearchAddressAdapter mSearchAddressAdapter;
	private String cityName = "";
	private String keyword = "";

	private static final String NAME = "NAME";
	private static final String PHONE = "PHONE";
	private static final String PREFIX_ADDRESS = "PREFIX_ADDRESS";
	private static final String SUFFIX_ADDRESS = "SUFFIX_ADDRESS";
	private static final String HOUSE_NUMBER = "HOUSE_NUMBER";
	private static final String ADDRESS_TAG = "ADDRESS_TAG";
	private static final String GENDER = "GENDER";

	private static final Integer HOME_TAG = 0;
	private static final Integer SCHOOL_TAG = 1;
	private static final Integer COMPANY_TAG = 2;
	private UiSettings mUiSettings;


	public static AddressEditDelegate create(String houseNumber, String name, String phone, String addressPre, String addressSuf, Integer tag, Integer gender) {
		final Bundle args = new Bundle();
		args.putString(NAME, name);
		args.putString(PHONE, phone);
		args.putString(PREFIX_ADDRESS, addressPre);
		args.putString(SUFFIX_ADDRESS, addressSuf);
		args.putString(HOUSE_NUMBER, houseNumber);
		args.putInt(ADDRESS_TAG, tag);
		args.putInt(GENDER, gender);
		final AddressEditDelegate delegate = new AddressEditDelegate();
		delegate.setArguments(args);
		return delegate;
	}

	/**
	 * 地址前缀 addressPre  e.g.青岛国际动漫游戏产业园
	 * 地址后缀 addressSuf  e.g.青岛市市南区
	 * 门牌号  housenNumber e.g.9栋301
	 * 标签    tag
	 * 联系人  name
	 * 手机号  phone
	 * 性别 先生、女士 gender
	 */
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Bundle args = getArguments();
		if (args != null) {
			name = args.getString(NAME);
			phone = args.getString(PHONE);
			houseNumber = args.getString(HOUSE_NUMBER);
			addressPre = args.getString(PREFIX_ADDRESS);
			addressSuf = args.getString(SUFFIX_ADDRESS);
			gender = args.getInt(GENDER);
			tag = args.getInt(ADDRESS_TAG);
		} else {
			throw new NullPointerException("Bundle is null");
		}
	}

	@Override
	public Object setLayout() {
		return R.layout.delegate_address_edit;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {


		final MapStatus.Builder builder = new MapStatus.Builder();
		builder.zoom(18.0f);
		//监听输入框
		monitorEditTextChage();
		initGeoCoder();
		initPoiSearch();
		initSuggestionSearch();
		initMap();
		initLocation();
		monitorMap();
//            final LatLng GEO_CHONGQING = new LatLng(29.5924475600, 106.4984776500);
//            MapStatusUpdate status1  = MapStatusUpdateFactory.newLatLng(GEO_CHONGQING);
//            mBaiduMap.setMapStatus(status1);
//            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

		//姓名
		mAddressName.setText(name);
		//手机号
		mAddressPhone.setText(phone);
		//门牌号
		mAddressHouseNumber.setText(houseNumber);
		//地址前缀
		mTvAddressPrefix.setText(addressPre);
		//地址后缀
		mTvAddressSuffix.setText(addressSuf);
		//性别
		if (gender == 0) {
			mRadioBtnMale.setChecked(true);
		} else {
			mRadioBtnFemale.setChecked(true);
		}
		//地址标签 0:home 1:school 2:company
		if (tag.equals(HOME_TAG)) {
			mTvHomeTag.setBackground(getResources()
					.getDrawable(R.drawable.address_tag_border_choosed));

		} else if (tag.equals(SCHOOL_TAG)) {
			mTvSchoolTag.setBackground(getResources()
					.getDrawable(R.drawable.address_tag_border_choosed));
		} else if (tag.equals(COMPANY_TAG)) {
			mTvCompanyTag.setBackground(getResources()
					.getDrawable(R.drawable.address_tag_border_choosed));
		}
		address_tag_border = getResources().getDrawable(R.drawable.address_tag_border);
		address_tag_border_choosed = getResources().getDrawable(R.drawable.address_tag_border_choosed);

	}

	/**
	 * 监听地图事件（这里主要监听移动地图）
	 */
	private void monitorMap() {
		mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {

			@Override
			public void onMapLoaded() {
				//地图加载完成
			}
		});
		mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

			/**
			 * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
			 * @param mapStatus 地图状态改变开始时的地图状态
			 */
			@Override
			public void onMapStatusChangeStart(MapStatus mapStatus) {
			}

			/** 因某种操作导致地图状态开始改变。
			 * @param mapStatus 地图状态改变开始时的地图状态
			 * @param i 取值有：
			 * 1：用户手势触发导致的地图状态改变,比如双击、拖拽、滑动底图
			 * 2：SDK导致的地图状态改变, 比如点击缩放控件、指南针图标
			 * 3：开发者调用,导致的地图状态改变
			 */
			@Override
			public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
				Log.e(TAG, "地图状态改变开始时：" + i + "");
			}

			/**
			 * 地图状态变化中
			 * @param mapStatus 当前地图状态
			 */
			@Override
			public void onMapStatusChange(MapStatus mapStatus) {
				LatLng latlng = mBaiduMap.getMapStatus().target;
				double latitude = latlng.latitude;
				double longitude = latlng.longitude;
				Log.e(TAG, "地图状态变化中：" + latitude + "," + longitude);
				addMarker(latlng);
			}

			/**
			 * 地图状态改变结束
			 * @param mapStatus 地图状态改变结束后的地图状态
			 */
			@Override
			public void onMapStatusChangeFinish(MapStatus mapStatus) {
				center = mBaiduMap.getMapStatus().target;
//                Log.e(TAG, "地图状态改变结束后：" + center.latitude + "," + center.longitude);
				if (poiInfoListForGeoCoder != null) {
					poiInfoListForGeoCoder.clear();
				}
				//反向地理解析（含有poi列表）
				mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
						.location(center));
			}
		});
	}

	/**
	 * 添加marker
	 *
	 * @param point Marker坐标点
	 */
	private void addMarker(LatLng point) {
		if (marker != null) {
			//marker.remove();
			marker.setPosition(point);
		} else {
			//构建Marker图标
			BitmapDescriptor bitmap = BitmapDescriptorFactory
					.fromResource(R.drawable.address_edit_tag);
			//构建MarkerOption，用于在地图上添加Marker
			OverlayOptions option = new MarkerOptions()
					.position(point)
					.icon(bitmap);
			//在地图上添加Marker，并显示
			marker = (Marker) mBaiduMap.addOverlay(option);
		}
	}

	private void initPoiSearch() {
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
	}

	//-----------------------------------------建议搜索（sug检索）------------------------------------------------------------------
	private void initSuggestionSearch() {
		// 初始化建议搜索模块，注册建议搜索事件监听(sug搜索)
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
			/**
			 * 获取在线建议搜索结果，得到requestSuggestion返回的搜索结果
			 * @param suggestionResult    Sug检索结果
			 */
			@Override
			public void onGetSuggestionResult(SuggestionResult suggestionResult) {
				if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
					Toast.makeText(getContext(), "未找到结果", Toast.LENGTH_LONG).show();
					return;
				}

				List<SuggestionResult.SuggestionInfo> sugList = suggestionResult.getAllSuggestions();
				for (SuggestionResult.SuggestionInfo info : sugList) {
					if (info.key != null) {
						Log.e(TAG, "搜索结果：" + info.toString());
						Log.e(TAG, "key：" + info.key);
						DecimalFormat df = new DecimalFormat("######0");
						//用当前所在位置算出距离
						String distance = df.format(DistanceUtil.getDistance(currentLatLng, info.pt));
						Log.e(TAG, "距离：" + distance);
					}
				}

			}
		});
	}

	private void initGeoCoder() {
		mGeoCoder = GeoCoder.newInstance();
		mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			@Override
			public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

			}

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
				if (poiInfoListForGeoCoder != null) {
					poiInfoListForGeoCoder.clear();
				}
				if (reverseGeoCodeResult.error.equals(SearchResult.ERRORNO.NO_ERROR)) {
					//获取城市
					ReverseGeoCodeResult.AddressComponent addressComponent = reverseGeoCodeResult.getAddressDetail();
					cityName = addressComponent.city;
					mTvCurrrentCity.setText(cityName);
					//获取poi列表
					if (reverseGeoCodeResult.getPoiList() != null) {
						poiInfoListForGeoCoder = reverseGeoCodeResult.getPoiList();
					}
				} else {
					Toast.makeText(getContext(), "该位置范围内无信息", Toast.LENGTH_SHORT).show();
				}
				initGeoCoderListView();
			}
		});
	}


	private void initMap() {
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mUiSettings = mBaiduMap.getUiSettings();
		mUiSettings.setAllGesturesEnabled(false);
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


	@OnClick(R2.id.btn_address_edit_save)
	void onClickSaveAddress() {

	}

	@OnClick(R2.id.iv_address_edit_backarrow)
	void onClickReturn() {
		getSupportDelegate().pop();
	}

	@OnClick(R2.id.btn_address_edit_delete)
	void onClickDeleteAddress() {
		getSupportDelegate().pop();
	}

	@OnClick(R2.id.tv_address_edit)
	void onClickEditAddress() {

		mCardViewBottom.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.out_bottom));
		mCardViewTop.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.out_bottom));

		mCardViewBottom.setVisibility(View.GONE);
		mCardViewTop.setVisibility(View.GONE);
		mCardViewSearchAddress.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.in_bottom));
		mLlInputText.setVisibility(View.VISIBLE);
		mCardViewSearchAddress.setVisibility(View.VISIBLE);
		mLvSearchAddress.setVisibility(View.VISIBLE);
		mSearchAddressAdapter.notifyDataSetChanged();
		mUiSettings.setAllGesturesEnabled(true);
	}


	// 0:home 1:school 2:company
	@OnClick(R2.id.tv_address_edit_home_tag)
	void onClickChooseHomeTag() {
		if (tag.equals(HOME_TAG)) {
			mTvHomeTag.setBackground(address_tag_border);
		} else {
			tag = HOME_TAG;
			mTvHomeTag.setBackground(address_tag_border_choosed);
			mTvCompanyTag.setBackground(address_tag_border);
			mTvSchoolTag.setBackground(address_tag_border);
		}
	}

	@OnClick(R2.id.tv_address_edit_school_tag)
	void onClickChooseSchoolTag() {
		if (tag.equals(SCHOOL_TAG)) {
			mTvSchoolTag.setBackground(address_tag_border);
		} else {
			tag = SCHOOL_TAG;
			mTvSchoolTag.setBackground(address_tag_border_choosed);
			mTvHomeTag.setBackground(address_tag_border);
			mTvCompanyTag.setBackground(address_tag_border);
		}
	}

	@OnClick(R2.id.tv_address_edit_company_tag)
	void onClickChooseCompanyTag() {
		if (tag.equals(COMPANY_TAG)) {
			mTvCompanyTag.setBackground(address_tag_border);
		} else {
			tag = COMPANY_TAG;
			mTvCompanyTag.setBackground(address_tag_border_choosed);
			mTvHomeTag.setBackground(address_tag_border);
			mTvSchoolTag.setBackground(address_tag_border);
		}
	}

	@OnClick(R2.id.radioBtn_address_edit_male)
	void onClickMale() {
		if (gender != 0) {
			gender = 0;
			mRadioBtnMale.setChecked(true);
			mRadioBtnFemale.setChecked(false);
		}
	}

	@OnClick(R2.id.radioBtn_address_edit_female)
	void onClickFemale() {
		if (gender != 1) {
			gender = 1;
			mRadioBtnFemale.setChecked(true);
			mRadioBtnMale.setChecked(false);
		}
	}


	@Override
	public void onResume() {
		//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
		super.onResume();
	}

	@Override
	public void onPause() {
		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
		super.onPause();
	}


	/**
	 * 获取POI搜索结果，包括searchInCity，searchNearby，searchInBound返回的搜索结果
	 *
	 * @param poiResult Poi检索结果，包括城市检索，周边检索，区域检索
	 */
	@Override
	public void onGetPoiResult(PoiResult poiResult) {
		if (poiInfoListForSearch != null) {
			poiInfoListForSearch.clear();
		}
		if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(getContext(), "未找到结果", Toast.LENGTH_LONG).show();
			initPoiSearchListView();
			return;
		}

		if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
			poiInfoListForSearch = poiResult.getAllPoi();
			showSeachView();
			initPoiSearchListView();
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

	private void initGeoCoderListView() {

		mSearchAddressAdapter = new SearchAddressAdapter(poiInfoListForGeoCoder, currentLatLng);
		mSearchAddressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				final PoiInfo info = (PoiInfo) adapter.getItem(position);

				Collections.swap(poiInfoListForGeoCoder, 0, position);
				assert info != null;
				LatLng choosedLocation = info.getLocation();
				//移动marker的位置
				if (marker != null) {
					marker.setPosition(choosedLocation);
				}
				final MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(info.getLocation());
				mBaiduMap.setMapStatus(status);
				mCardViewSearchAddress.setVisibility(View.GONE);
				mLlInputText.setVisibility(View.GONE);

				mCardViewBottom.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.in_bottom));
				mCardViewTop.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.in_bottom));
				mCardViewTop.setVisibility(View.VISIBLE);
				mCardViewBottom.setVisibility(View.VISIBLE);

				mTvAddressPrefix.setText(info.getName());
				mTvAddressSuffix.setText(info.getAddress());
				mUiSettings.setAllGesturesEnabled(false);
			}
		});
		final LinearLayoutManager manager = new LinearLayoutManager(getContext());
		mLvSearchAddress.setLayoutManager(manager);
		mLvSearchAddress.setAdapter(mSearchAddressAdapter);
	}

	private void initPoiSearchListView() {
		mSearchAddressAdapter = new SearchAddressAdapter(poiInfoListForSearch, currentLatLng);
		mSearchAddressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				final PoiInfo poiInfo = (PoiInfo) adapter.getItem(position);
				assert poiInfo != null;
				if (marker != null) {
					marker.setPosition(poiInfo.getLocation());
				}
				final MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(poiInfo.getLocation());
				mBaiduMap.setMapStatus(status);
				mTvAddressPrefix.setText(poiInfo.getName());
				mTvAddressSuffix.setText(poiInfo.getAddress());
				mUiSettings.setAllGesturesEnabled(false);
				mMapView.setVisibility(View.VISIBLE);
				mCardViewSearchAddress.setVisibility(View.GONE);
				mLvPoiSearchAddress.setVisibility(View.GONE);
				mLlInputText.setVisibility(View.GONE);
				mCardViewBottom.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.in_bottom));
				mCardViewTop.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.in_bottom));
				mCardViewBottom.setVisibility(View.VISIBLE);
				mCardViewTop.setVisibility(View.VISIBLE);
			}
		});
		final LinearLayoutManager manager = new LinearLayoutManager(getContext());
		mLvPoiSearchAddress.setLayoutManager(manager);
		mLvPoiSearchAddress.setAdapter(mSearchAddressAdapter);

	}

	/**
	 * 展示搜索的布局
	 */
	private void showSeachView() {
		mMapView.setVisibility(View.GONE);
		mCardViewSearchAddress.setVisibility(View.GONE);
		mLlPoiSearchAddress.setVisibility(View.VISIBLE);
	}

	/**
	 * 展示地图的布局
	 */
	private void showMapView() {
		mMapView.setVisibility(View.VISIBLE);
		mCardViewSearchAddress.setVisibility(View.VISIBLE);
		mLlPoiSearchAddress.setVisibility(View.GONE);
	}

	/**
	 * 获取POI详情搜索结果，得到searchPoiDetail返回的搜索结果
	 * V5.2.0版本之后，该方法废弃，使用{@link #onGetPoiDetailResult(PoiDetailSearchResult)}代替
	 *
	 * @param poiDetailResult POI详情检索结果
	 */
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
			if (location == null || mMapView == null) {
				return;
			}

			Log.e(TAG, "当前“我”的位置：" + location.getAddrStr());
			if (location.getLocType() == BDLocation.TypeGpsLocation
					|| location.getLocType() == BDLocation.TypeNetWorkLocation) {
				navigateTo(location);
				cityName = location.getCity();
				mTvCurrrentCity.setText(cityName);
				Log.e(TAG, "当前定位城市：" + location.getCity() +
						"定位地址: " + location.getAddress());
			}

		}
	}

	/**
	 * 根据获取到的位置在地图上移动"我"的位置
	 *
	 * @param location
	 */
	private void navigateTo(BDLocation location) {
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();
		String address = location.getAddrStr();
		if (isFirstLocation) {
			currentLatLng = new LatLng(latitude, longitude);
			MapStatus.Builder builder = new MapStatus.Builder();
			MapStatus mapStatus = builder.target(currentLatLng).zoom(17.0f).build();
			mBaiduMap.animateMapStatus(MapStatusUpdateFactory
					.newMapStatus(mapStatus));
			isFirstLocation = false;

			//反向地理解析(含有poi列表)
			mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
					.location(currentLatLng)
					.radius(radius));
		}
		MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
		locationBuilder.latitude(location.getLatitude());
		locationBuilder.longitude(location.getLongitude());
		MyLocationData locationData = locationBuilder.build();
		mBaiduMap.setMyLocationData(locationData);
	}

	/**
	 * 根据收货地址的位置在地图上移动"我"的位置
	 */
	private void navigateTo(double longitude, double latitude) {
		if (isFirstLocation) {
			currentLatLng = new LatLng(latitude, longitude);
			MapStatus.Builder builder = new MapStatus.Builder();
			MapStatus mapStatus = builder.target(currentLatLng).zoom(17.0f).build();
			mBaiduMap.animateMapStatus(MapStatusUpdateFactory
					.newMapStatus(mapStatus));
			isFirstLocation = false;

			//反向地理解析（含有poi列表）
			mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
					.location(currentLatLng));
		}
		MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
		locationBuilder.latitude(latitude);
		locationBuilder.longitude(longitude);
		MyLocationData locationData = locationBuilder.build();
		mBaiduMap.setMyLocationData(locationData);
	}

	private void monitorEditTextChage() {
		mEtSearchKeyword.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				keyword = editable.toString();
				if (keyword.length() <= 0) {
					//当清空文本后展示地图，隐藏搜索结果
					showMapView();
					return;
				}
				/* 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新 */
				/* 由于我们需要滑动地图展示周边poi，所以就不用建议搜索列表来搜索poi了，搜索时直接利用城市和输入的关键字进行城市内检索poi */
//                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
//                        .keyword(keyword)
//                        .city(cityName));
				searchCityPoiAddress();
			}
		});
	}

	public void searchCityPoiAddress() {
		mPoiSearch.searchInCity((new PoiCitySearchOption())
				.city(cityName)
				.keyword(keyword)//必填
				.pageCapacity(pageSize)
				.pageNum(loadIndex));//分页页码
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mMapView != null) {
			mMapView.onDestroy();
		}
		// 当不需要定位图层时关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		// 取消监听函数
		if (mLocationClient != null) {
			mLocationClient.unRegisterLocationListener(myListener);
		}
		mPoiSearch.destroy();
		mSuggestionSearch.destroy();
		mGeoCoder.destroy();
	}

}
