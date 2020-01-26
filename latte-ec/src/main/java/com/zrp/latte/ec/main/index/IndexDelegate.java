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
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


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

	@OnClick(R2.id.tv_index_location)
	void onClickLocation() {

		getParentDelegate().getSupportDelegate().startForResult(new LocationDelegate(), LOCATION_CODE);

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
					mToolBar.setVisibility(View.GONE);
					mToolBar.setBackground(getResources().getDrawable(R.drawable.index_toorbar_background));
				}

				if (scrollY == 0) {
					Log.e("=====", "滑到顶部");
					mToolBar.setBackground(getResources().getDrawable(R.drawable.index_toorbar_background));
				}

				if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
					Log.e("=====", "滑到底部");
				}
			}
		});
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



}
