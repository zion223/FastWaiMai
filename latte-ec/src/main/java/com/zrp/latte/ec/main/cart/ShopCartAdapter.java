package com.zrp.latte.ec.main.cart;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.latte.latte_ec.R;
import com.joanzapata.iconify.widget.IconTextView;
import com.zrp.latte.app.Latte;
import com.zrp.latte.ui.recyclerview.IItemTouchMoveListener;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;
import com.zrp.latte.ui.recycler.MultipleRecyclerAdapter;
import com.zrp.latte.ui.recycler.MultipleViewHolder;

import java.util.Collections;
import java.util.List;

public class ShopCartAdapter extends MultipleRecyclerAdapter implements IItemTouchMoveListener {

	private boolean mIsSelectedAll = false;

	private static final RequestOptions OPTIONS = new RequestOptions()
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.centerCrop()
			.dontAnimate();

	protected ShopCartAdapter(List<MultipleItemEntity> data) {
		super(data);
		init();
	}

	@Override
	protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
		super.convert(holder, entity);
		switch (holder.getItemViewType()){
			case ShopCartItemType.SHOP_CART_ITEM:
				//取出数据
				final int id = entity.getField(MultipleFields.ID);
				final String thumb = entity.getField(MultipleFields.IMAGE_URL);
				final String title = entity.getField(ShopCartItemFields.TITLE);
				final String desc = entity.getField(ShopCartItemFields.DESC);
				final int count = entity.getField(ShopCartItemFields.COUNT);
				final float price = entity.getField(ShopCartItemFields.PRICE);
				//final boolean isSelected = entity.getField(ShopCartItemFields.IS_SELECTED);

				//取出布局中的控件
				final AppCompatImageView imgThumb = holder.getView(R.id.image_item_shop_cart);
				final AppCompatTextView tvTitle = holder.getView(R.id.tv_item_shop_cart_title);
				final AppCompatTextView tvDesc = holder.getView(R.id.tv_item_shop_cart_desc);
				final AppCompatTextView tvPrice = holder.getView(R.id.tv_item_shop_cart_price);
				final IconTextView iconMinus = holder.getView(R.id.icon_item_minus);
				final IconTextView iconPlus = holder.getView(R.id.icon_item_plus);
				final AppCompatTextView tvCount = holder.getView(R.id.tv_item_shop_cart_count);
				final IconTextView iconIsSelected = holder.getView(R.id.icon_item_shop_cart);
				//控件属性赋值
				tvTitle.setText(title);
				tvDesc.setText(desc);
				tvPrice.setText(String.valueOf(price));
				tvCount.setText(String.valueOf(count));
				Glide.with(mContext)
						.load(thumb)
						.apply(OPTIONS)
						.into(imgThumb);
				//全选与取消全选状态
				entity.setField(ShopCartItemFields.IS_SELECTED, mIsSelectedAll);
				final boolean isSelected = entity.getField(ShopCartItemFields.IS_SELECTED);
				//左侧选择框 初始化状态
				if(isSelected){
					iconIsSelected.setTextColor(
							ContextCompat.getColor(Latte.getApplication(),R.color.app_main));
				}else{
					iconIsSelected.setTextColor(Color.GRAY);
				}
				//左侧选择框 点击事件 使用匿名内部类实现,不可实现接口
				iconIsSelected.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						final boolean currentSelected = entity.getField(ShopCartItemFields.IS_SELECTED);
						//当前被选中 点击则不被选中
						if(currentSelected){
							iconIsSelected.setTextColor(Color.GRAY);
							entity.setField(ShopCartItemFields.IS_SELECTED, false);
						}else{
							iconIsSelected.setTextColor(
									ContextCompat.getColor(Latte.getApplication(),R.color.app_main));
							entity.setField(ShopCartItemFields.IS_SELECTED, true);
						}
					}
				});
				//商品数量加减
				iconMinus.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						//不与后端进行数据交互
						tvCount.setText(String.valueOf(count-1));
					}
				});
				iconPlus.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						tvCount.setText(String.valueOf(count+1));
					}
				});
				break;
			default:
				break;
		}
	}

	public void setIsSelectedAll(boolean mIsSelectedAll) {
		this.mIsSelectedAll = mIsSelectedAll;
	}

	private void init(){
		addItemType(ShopCartItemType.SHOP_CART_ITEM, R.layout.item_shop_cart);
	}


	@Override
	public boolean onItemMove(int fromPosition, int position) {
		Collections.swap(getData(), fromPosition, position);
		notifyItemMoved(fromPosition, position);
		return false;
	}

	@Override
	public boolean onItemRemove(int position) {
		getData().remove(position);
		notifyItemRangeRemoved(position, getData().size());
		return true;
	}
}
