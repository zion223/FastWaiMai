package com.zrp.latte.ec.main.sort;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.latte.latte_ec.R;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.ec.main.sort.content.ContentDelegate;
import com.zrp.latte.ec.main.sort.list.VerticalListDelegate;

public class SortDelegate extends BottomItemDelegate {

	@Override
	public Object setLayout() {
		return R.layout.delegate_sort;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
		super.onBindView(savedInstanceState, view);
	}

	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);
		final VerticalListDelegate listDelegate = new VerticalListDelegate();

		//加载左边分类栏
		getSupportDelegate().loadRootFragment(R.id.vertical_list_container,listDelegate);

		//加载右边内容栏   默认的内容栏
		//getSupportDelegate().loadRootFragment(R.id.sort_content_container, ContentDelegate.newInstance(0));
		getSupportDelegate().loadRootFragment(R.id.sort_content_container, ContentDelegate.newInstance(0));
	}

}
