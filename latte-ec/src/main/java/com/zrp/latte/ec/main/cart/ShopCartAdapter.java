package com.zrp.latte.ec.main.cart;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

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
import java.util.LinkedList;
import java.util.List;

public class ShopCartAdapter extends MultipleRecyclerAdapter implements IItemTouchMoveListener {

	private boolean mIsSelectedAll = false;
	private ICartItemListener mCartItemListener = null;


	private double mTotalPrice = 0.00;
	private static final RequestOptions OPTIONS = new RequestOptions()
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.centerCrop()
			.dontAnimate();

	protected ShopCartAdapter(LinkedList<MultipleItemEntity> data) {
		super(data);
		//第一次初始化价格
		for(MultipleItemEntity entity:data){
			final double currentPrice = entity.getField(ShopCartItemFields.PRICE);
			final int currentCount = entity.getField(ShopCartItemFields.COUNT);
			final double total = currentCount * currentPrice;
			mTotalPrice += total;
		}
		//添加布局
		initItemType();
	}
	private void initItemType(){
		addItemType(ShopCartItemType.SHOP_CART_ITEM, R.layout.item_shop_cart);
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
				final double price = entity.getField(ShopCartItemFields.PRICE);
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
				//商品数量加减 更新购物车价格
				iconMinus.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						//不与后端进行数据交互
						final Integer minusCount = Integer.valueOf(tvCount.getText().toString()) - 1;
						if(!(minusCount == 0)){
							tvCount.setText(String.valueOf(Integer.valueOf(tvCount.getText().toString()) - 1));
							//在entity中更新数量
							entity.setField(ShopCartItemFields.COUNT, minusCount);
							mTotalPrice -= price;
							if(mCartItemListener != null){
								mCartItemListener.onItemClick(mTotalPrice);
							}else{
								throw new NullPointerException("mCartItemListener is null!!!!!");
							}
						}else{
							//显示Dialog是否要移除此项商品
                            Toast.makeText(mContext,R.string.count2zero,Toast.LENGTH_SHORT).show();
						}

					}
				});
				iconPlus.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						final Integer plusCount = Integer.valueOf(tvCount.getText().toString()) + 1;
						tvCount.setText(String.valueOf(Integer.valueOf(tvCount.getText().toString()) + 1));
						entity.setField(ShopCartItemFields.COUNT, plusCount);
						mTotalPrice += price;
						if(mCartItemListener != null){
							mCartItemListener.onItemClick(mTotalPrice);
						}else{
							throw new NullPointerException("mCartItemListener is null!!!!!");
						}
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

	public void setCartItemListener(ICartItemListener cartItemListener) {
		this.mCartItemListener = cartItemListener;
	}
	public double getTotalPrice() {
		return mTotalPrice;
	}


    @Override
    public void onItemDragMoving(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int from = source.getAdapterPosition() - getHeaderLayoutCount();
        int to = target.getAdapterPosition() - getHeaderLayoutCount();

        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(mData, i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(mData, i, i - 1);
            }
        }
        notifyItemMoved(source.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
	public boolean onItemRemove(int position) {

        final MultipleItemEntity removedEntity = mData.get(position);
		mData.remove(position);

		notifyItemRemoved(position);
		final int count = removedEntity.getField(ShopCartItemFields.COUNT);
		final double price = removedEntity.getField(ShopCartItemFields.PRICE);
		if(mCartItemListener != null){
		    if(mData.size() == 0){
                mCartItemListener.checkItemCount();
            }
            final double currentTotalPrice =  getTotalPrice() - count*price;
            mTotalPrice = currentTotalPrice;
			mCartItemListener.onItemClick(currentTotalPrice);
		}else{
		    throw new NullPointerException("mCartItemListener is null!!!");
        }
		return true;
	}
}
