package com.zrp.latte.ec.main.sort.content;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;

import java.util.List;

import butterknife.BindView;

//分类右边栏 Delegate
public class ContentDelegate extends BottomItemDelegate {

	private static final String ARG_CATEGORY_ID = "CATEGORY_ID";

	@BindView(R2.id.rv_list_content)
	RecyclerView mRecyclerView;

	private int mCategoryId = -1;

	private List<SectionBean> mData = null;

	@Override
	public Object setLayout() {
		return R.layout.delegate_list_content;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Bundle args = getArguments();
		if (args != null) {
			mCategoryId = args.getInt(ARG_CATEGORY_ID);
		}
	}

	/**
	 *	第一次加载时 categoryId=242  中国文学
	 *	ContentDelegate提供的实例化方法
	 */
	public static ContentDelegate newInstance(Integer categoryId) {
		final ContentDelegate contentDelegate = new ContentDelegate();
		final Bundle bundle = new Bundle();
		bundle.putInt(ARG_CATEGORY_ID, categoryId);
		contentDelegate.setArguments(bundle);

		return contentDelegate;
	}


	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
		super.onBindView(savedInstanceState, view);
		//瀑布流
		final StaggeredGridLayoutManager manager =
				new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(manager);
		initData();
	}

	private void initData(){
		RestClient.builder()
				.url("http://apis.juhe.cn/goodbook/query?key=4f2a6e2eb200a619fb39f2c54860d519&catalog_id="+ mCategoryId +"&rn=0&rn=15")
				//.url("api/sort_content")
				.success(new ISuccess() {
					@Override
					public void onSuccess(String response) {
						//获取数据
						 mData = new SectionDataConverter().convert(response);
						 final SectionAdapter sectionAdapter =
								 new SectionAdapter(R.layout.item_section_content,R.layout.item_section_header,mData);
						 mRecyclerView.setAdapter(sectionAdapter);

					}
				})
				.build()
				.get();
	}


}
