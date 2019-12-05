package com.zrp.latte.ec.main.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.latte.latte_ec.R;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.delegates.web.WebDelegateImpl;

import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class DiscoverDelegate extends BottomItemDelegate {

	@Override
	public Object setLayout() {
		return R.layout.delegate_discover;
	}


	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);
		final WebDelegateImpl webDelegate = WebDelegateImpl.create("index.html");
		webDelegate.setTopDelegate(this.getParentDelegate());
		//loadFragment
		getSupportDelegate().loadRootFragment(R.id.web_discovery_container, webDelegate);
	}

	/**
	 * 跳转的动画
	 * @return
	 */
	@Override
	public FragmentAnimator onCreateFragmentAnimator() {
		return new DefaultHorizontalAnimator();
	}


}
