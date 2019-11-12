package com.zrp.latte.ec.main.sort.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ec.main.sort.SortDelegate;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);
		RestClient.builder()
				.url("http://apis.juhe.cn/goodbook/catalog?key=4f2a6e2eb200a619fb39f2c54860d519&dtype=json")
				.loader(getContext())
				.success(new ISuccess() {
					@Override
					public void onSuccess(String response) {
						final List<MultipleItemEntity> data =
							new VerticalListDataConverter().setJsonData(response).convert();
						final SortDelegate delegate = getParentDelegate();
						mAdapter = new SortRecyclerAdapter(data, delegate);
						mRecyclerView.setAdapter(mAdapter);
					}
				})
				.build()
				.get();
	}
}
