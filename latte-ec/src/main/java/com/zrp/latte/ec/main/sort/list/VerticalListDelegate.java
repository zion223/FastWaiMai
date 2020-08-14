package com.zrp.latte.ec.main.sort.list;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ec.main.sort.SortDelegate;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class VerticalListDelegate extends LatteDelegate {

	@BindView(R2.id.rv_menu_list)
	RecyclerView mRecyclerView;

	private SortRecyclerAdapter mAdapter = null;

	@Override
	public Object setLayout() {
		return R.layout.delegate_vertical_list;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {

		initRecyclerView();
	}

	private void initRecyclerView() {
		final LinearLayoutManager manager = new LinearLayoutManager(getContext());
		mRecyclerView.setLayoutManager(manager);
		mRecyclerView.setItemAnimator(null);
	}

	/**
	 * 初始化数据
	 * @param savedInstanceState
	 */
	@SuppressLint("CheckResult")
	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);
		RestClient.builder()
				.url("api/p_sort")
				.build()
				.get()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<String>() {
					@Override
					public void accept(String response) throws Exception {
						final List<MultipleItemEntity> data =
								new VerticalListDataConverter().setJsonData(response).convert();
						final SortDelegate delegate = getParentDelegate();
						mAdapter = new SortRecyclerAdapter(data, delegate);
						mRecyclerView.setAdapter(mAdapter);
					}
				});

	}
}
