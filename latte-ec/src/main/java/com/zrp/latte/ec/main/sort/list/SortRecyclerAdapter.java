package com.zrp.latte.ec.main.sort.list;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.example.latte.latte_ec.R;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ec.main.sort.SortDelegate;
import com.zrp.latte.ec.main.sort.content.ContentPageViewDelegate;
import com.zrp.latte.ui.recycler.ItemType;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;
import com.zrp.latte.ui.recycler.MultipleRecyclerAdapter;
import com.zrp.latte.ui.recycler.MultipleViewHolder;

import java.util.List;

import me.yokeyword.fragmentation.SupportHelper;

public class SortRecyclerAdapter extends MultipleRecyclerAdapter {

	private final SortDelegate DELEGATE;
	private int mPrePosition = 0;

	protected SortRecyclerAdapter(List<MultipleItemEntity> data, SortDelegate delegate) {
		super(data);
		DELEGATE = delegate;
		init();
	}

	private void init(){
		addItemType(ItemType.VERTICAL_MENU_LIST, R.layout.item_vertical_menu_list);
	}

	@Override
	protected void convert(final MultipleViewHolder holder, final MultipleItemEntity entity) {
		switch (holder.getItemViewType()){
			case ItemType.VERTICAL_MENU_LIST:
				final String text = entity.getField(MultipleFields.TEXT);
				final Boolean isClicked = entity.getField(MultipleFields.TAG);
				View viewLine = holder.getView(R.id.view_line);
				final AppCompatTextView textView =  holder.getView(R.id.tv_vertical_item_name);
				final View itemView = holder.itemView;
				itemView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//类别的点击  getAdapterPosition:从0开始
						final int currentPosition = holder.getAdapterPosition();
						if(mPrePosition != currentPosition){
							//还原上次状态
							getData().get(mPrePosition).setField(MultipleFields.TAG, false);
							notifyItemChanged(mPrePosition);


							//更新item状态
							entity.setField(MultipleFields.TAG, true);
							notifyItemChanged(currentPosition);

							mPrePosition = currentPosition;

							//点击类别 内容栏跳转
							final Integer categoryId = getData().get(currentPosition).getField(MultipleFields.ID);
							showContent(categoryId);

						}

					}
				});
				if(isClicked){
					viewLine.setVisibility(View.VISIBLE);
					textView.setTextColor(ContextCompat.getColor(mContext, R.color.app_main));
					viewLine.setBackgroundColor(ContextCompat.getColor(mContext, R.color.app_main));
					itemView.setBackgroundColor(Color.WHITE);
				}else {
					viewLine.setVisibility(View.INVISIBLE);
					textView.setTextColor(ContextCompat.getColor(mContext, R.color.we_chat_black));
					viewLine.setBackgroundColor(ContextCompat.getColor(mContext, R.color.item_background));
				}

				holder.setText(R.id.tv_vertical_item_name,text);
		}
	}

	private void showContent(Integer categoryId) {
		final ContentPageViewDelegate contentPageViewDelegate = ContentPageViewDelegate.newInstance(categoryId);
		final LatteDelegate latteDelegate = SupportHelper.findFragment(DELEGATE.getChildFragmentManager(),ContentPageViewDelegate.class);
		if(latteDelegate != null){
			//替换Delegate
			latteDelegate.getSupportDelegate().replaceFragment(contentPageViewDelegate,false);
		}
	}

}
