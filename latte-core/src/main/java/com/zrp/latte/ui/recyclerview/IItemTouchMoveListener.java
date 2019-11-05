package com.zrp.latte.ui.recyclerview;

public interface IItemTouchMoveListener {

	//交换Item位置
	boolean onItemMove(int fromPosition, int toPosition);

	//滑动删除
	boolean onItemRemove(int position);
}
