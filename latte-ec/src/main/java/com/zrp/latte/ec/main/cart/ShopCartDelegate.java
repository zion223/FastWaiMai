package com.zrp.latte.ec.main.cart;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewStubCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.joanzapata.iconify.widget.IconTextView;
import com.zrp.latte.app.Latte;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.ec.pay.FastPay;
import com.zrp.latte.ui.recyclerview.ShopCartItemTouchHelperCallback;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopCartDelegate extends BottomItemDelegate implements ISuccess,ICartItemListener {


	private ShopCartAdapter mAdapter;
	private int mCurrentCount = 0;
	private int mTotalCount = 0;

	@BindView(R2.id.tv_top_shop_cart_clear)
	AppCompatTextView tvTopShopCartClear;
	@BindView(R2.id.tv_top_shop_cart_remove_selected)
	AppCompatTextView tvTopShopCartRemoveSelected;
	@BindView(R2.id.rv_shop_cart)
	RecyclerView mRecyclerView;
	@BindView(R2.id.stub_no_item)
	ViewStubCompat mStubNoItem;
	@BindView(R2.id.icon_shop_cart_select_all)
	IconTextView mIconSelectAll;
	@BindView(R2.id.tv_shop_cart_total_price)
	AppCompatTextView mTvTotalPrice;
	@BindView(R2.id.tv_shop_cart_pay)
	AppCompatTextView tvShopCartPay;

	//购物车商品全选
	@OnClick(R2.id.icon_shop_cart_select_all)
	void iconShopCartSelectAll(){
		final int tag = (int) mIconSelectAll.getTag();
		if(tag == 0){
			mIconSelectAll.setTag(1);
			mIconSelectAll.setTextColor(
					ContextCompat.getColor(Latte.getApplication(),R.color.app_main));
			//全选状态
			mAdapter.setIsSelectedAll(true);
			//批量更新 起始位置:0
			mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
		}else{
			mIconSelectAll.setTag(0);
			mIconSelectAll.setTextColor(Color.GRAY);
			mAdapter.setIsSelectedAll(false);
			mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
		}
	}
	//删除已选择的商品
	@OnClick(R2.id.tv_top_shop_cart_remove_selected)
	void removeSelected(){
		final List<MultipleItemEntity> data = mAdapter.getData();

		List<MultipleItemEntity> deletedEntity = new ArrayList<>();
		for(MultipleItemEntity entity : data){
			final boolean isSelected = entity.getField(ShopCartItemFields.IS_SELECTED);
			if(isSelected){
				deletedEntity.add(entity);
			}
		}
		for(MultipleItemEntity entity : deletedEntity){
			//int removePisotion;

			//初始值: 0a 1b 2c 3d 4e
			//要删除的项 0a 2c 4e
			//删除 0a 后 0b 1c 2d 3e
			//删除 1c 后 0b 1d 2e
			//删除 2e 后 0b 1d

			final int removePisotion = entity.getField(ShopCartItemFields.POSITION);
			entity.getField(ShopCartItemFields.TITLE);
			if(removePisotion <= mAdapter.getItemCount()){
				mAdapter.getData().remove(entity);
				mAdapter.remove(removePisotion);
				mCurrentCount = mAdapter.getItemCount();
				//mAdapter.notifyItemRangeChanged(removePisotion, mAdapter.getItemCount());
				mAdapter.notifyDataSetChanged();
			}
		}
		checkItemCount();
	}
	//清空购物车
	@OnClick(R2.id.tv_top_shop_cart_clear)
	void shopCartClear(){
		mAdapter.getData().clear();
		mAdapter.notifyDataSetChanged();
		mTvTotalPrice.setText("0");
		checkItemCount();
	}

	/**
	 * 结算
	 */
	@OnClick(R2.id.tv_shop_cart_pay)
	void shopCartPay(){
		//TODO 后台生成订单进行支付
		FastPay.create(this).beginPayDialog();
	}

	/**
	 * 提交到后台服务器生成订单
	 */
	private void createOrder(){

	}

	@Override
	public void checkItemCount() {
		final int totalCount = mAdapter.getItemCount();
		if(totalCount == 0){
			//购物车中没有商品
			@SuppressLint("RestrictedApi")
			final View stubView = mStubNoItem.inflate();
			final AppCompatTextView stubToBuy = (AppCompatTextView) stubView.findViewById(R.id.tv_stub_to_buy);
			stubToBuy.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					Toast.makeText(getContext(),"你该购物了!",Toast.LENGTH_SHORT).show();
				}
			});
			mRecyclerView.setVisibility(View.GONE);
		}else{
			mRecyclerView.setVisibility(View.VISIBLE);
		}
	}



	@Override
	public Object setLayout() {
		return R.layout.delegate_shopcart;
	}


	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
		super.onBindView(savedInstanceState, view);
		//初始化全选按钮的Tag
		mIconSelectAll.setTag(0);

	}

	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);
		RestClient.builder()
				.url("api/shop_cart")
				.loader(getContext())
				.success(this)
				.build()
				.get();
	}

	@Override
	public void onSuccess(String response) {
		final LinkedList<MultipleItemEntity> data =
				new ShopCartDataConverter().setJsonData(response).convert();
		final LinearLayoutManager manager = new LinearLayoutManager(getContext());
		mAdapter = new ShopCartAdapter(data);
		mAdapter.setCartItemListener(this);
		final ItemTouchHelper.Callback callback = new ShopCartItemTouchHelperCallback(mAdapter);
		final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
		itemTouchHelper.attachToRecyclerView(mRecyclerView);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setLayoutManager(manager);
		checkItemCount();
		final double totalPrice = mAdapter.getTotalPrice();
		mTvTotalPrice.setText(String.valueOf(totalPrice));
	}


	@Override
	public void onItemClick(double totalPrice) {
		final String price = String.valueOf(totalPrice);
		mTvTotalPrice.setText(price);
	}

}
