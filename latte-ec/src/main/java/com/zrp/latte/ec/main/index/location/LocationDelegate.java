package com.zrp.latte.ec.main.index.location;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LocationDelegate extends LatteDelegate{

	@BindView(R2.id.tv_index_location_city)
	TextView mTvLocationCity;
	@BindView(R2.id.tv_location_detail)
	TextView mTvLocationDetail;
	@BindView(R2.id.rv_location_address)
	RecyclerView mRvAddress;
	Unbinder unbinder;

	private AddressAdapter mAdapter = null;

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
		//附近的地址

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO: inflate a fragment view
		View rootView = super.onCreateView(inflater, container, savedInstanceState);
		unbinder = ButterKnife.bind(this, rootView);
		return rootView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	@OnClick(R2.id.iv_location_backarrow)
	public void onViewClickedBack(View view) {
		getSupportDelegate().pop();
	}

	@OnClick(R2.id.tv_index_location_city)
	public void onClickPickCity(View view) {

		final List<HotCity> hotCities = new ArrayList<>();
		hotCities.add(new HotCity("北京", "北京", "101010100")); //code为城市代码
		hotCities.add(new HotCity("上海", "上海", "101020100"));
		hotCities.add(new HotCity("广州", "广东", "101280101"));
		hotCities.add(new HotCity("深圳", "广东", "101280601"));
		hotCities.add(new HotCity("杭州", "浙江", "101210101"));
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
}
