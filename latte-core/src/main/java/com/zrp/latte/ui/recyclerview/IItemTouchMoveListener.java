package com.zrp.latte.ui.recyclerview;

import android.support.v7.widget.RecyclerView;

public interface IItemTouchMoveListener {

	//交换Item位置
	void onItemDragMoving(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target);

	//滑动删除
	boolean onItemRemove(int position);
}
