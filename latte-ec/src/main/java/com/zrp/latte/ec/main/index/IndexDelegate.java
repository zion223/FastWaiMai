package com.zrp.latte.ec.main.index;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.joanzapata.iconify.widget.IconTextView;
import com.zrp.latte.app.Latte;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.ec.main.index.scaner.ScannerDelegate;
import com.zrp.latte.ui.camera.CameraRequestCodes;
import com.zrp.latte.ui.recycler.BaseDecoration;
import com.zrp.latte.ui.refresh.RefreshHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;


public class IndexDelegate extends BottomItemDelegate implements View.OnFocusChangeListener {

	@BindView(R2.id.rv_index)
	RecyclerView mRecycleView;
	@BindView(R2.id.sr1_index)
	SwipeRefreshLayout mRefreshLayout;
	@BindView(R2.id.icon_index_scan)
	IconTextView mIconScan;
	@BindView(R2.id.icon_index_message)
	IconTextView mIconMessage;
	@BindView(R2.id.tb_index)
	Toolbar mToolBar;
	@BindView(R2.id.tl_index_status)
	TabLayout tabLayout;
	@BindView(R2.id.vp_index)
	ViewPager viewPager;

	private RefreshHandler mRefreshHandler = null;

	@OnClick(R2.id.icon_index_scan)
	void onClickScan() {
		//扫描二维码
		String[] perms = {Manifest.permission.CAMERA};
		//EasyPermission中请求的权限需要在Manifest中申请
		if (EasyPermissions.hasPermissions(Latte.getApplication(), perms)) {
			getSupportDelegate().startForResult(new ScannerDelegate(), CameraRequestCodes.SCAN);
		} else {
			EasyPermissions.requestPermissions(this, "请打开相关权限", 1, perms);
		}
	}


	@Override
	public Object setLayout() {
		return R.layout.delegate_index;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
		mRefreshHandler = RefreshHandler.create(mRefreshLayout, mRecycleView, new IndexDataConverter());
	}


	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);
		initRefreshLayout();
		initRecyclerView();
		mRefreshHandler.firstPage("api/books", 5);
	}

	private void initRecyclerView() {
		//总的SpanCount大小4 通过spanSize进行填充
		final GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
		mRecycleView.setLayoutManager(manager);
		//添加分割线
		mRecycleView.addItemDecoration(BaseDecoration.create(ContextCompat.getColor(getContext(), R.color.app_background), 5));

		//final EcBottomDelegate ecBottomDelegate = getParentDelegate();
		//传递this 跳转时有EcBottomDelegate 传递ecBottomDelegate 跳转时无EcBottomDelegate
		mRecycleView.addOnItemTouchListener(IndexItemClickListener.create(this));

	}

	private void initRefreshLayout() {
		mRefreshLayout.setColorSchemeResources(
				android.R.color.holo_blue_light,
				android.R.color.holo_orange_dark,
				android.R.color.holo_red_light
		);
		mRefreshLayout.setProgressViewOffset(true, 120, 300);
	}


	/**
	 * 跳转到 SearchDelegate
	 *
	 * @param view
	 * @param hasFocus
	 */
	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		if (hasFocus) {

		}
	}


}
