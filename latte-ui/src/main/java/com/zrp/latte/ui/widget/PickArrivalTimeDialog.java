package com.zrp.latte.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.example.latte.ui.R;
import com.example.latte.ui.R2;
import com.zrp.latte.ui.recycler.ItemType;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PickArrivalTimeDialog extends Dialog {

	@BindView(R2.id.tv_arrival_time_title)
	TextView mTvArrivalTimeTitle;
	@BindView(R2.id.tv_arrival_time_toady)
	TextView mTvArrivalTimeToady;
	@BindView(R2.id.tv_arrival_time_tomorrow)
	TextView mTvArrivalTimeTomorrow;
	@BindView(R2.id.rv_arrival_time)
	RecyclerView mRvArrivalTime;

	private final LinkedList<MultipleItemEntity> ENTITYS = new LinkedList<>();

	private OnArrivalTimePickListener listener;

	private static final int ITEM_ARRIVAL_TIME_NORMAL = 30;
	private static final int ARRIVAL_TIME_TIME = 31;
	private static final int ARRIVAL_TIME_MONEY = 32;

	public PickArrivalTimeDialog(@NonNull Context context) {
		super(context);
	}

	public void setListener(OnArrivalTimePickListener listener) {
		this.listener = listener;
	}

	public PickArrivalTimeDialog(@NonNull Context context, String title, OnArrivalTimePickListener listener) {
		super(context);
		this.listener = listener;
		mTvArrivalTimeTitle.setText(title);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.dialog_discountcard_choose);
		final Window window = getWindow();
		if(window != null){
			window.setContentView(R.layout.dialog_discountcard_choose);
			window.setGravity(Gravity.BOTTOM);
			window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
			window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			//设置属性
			final WindowManager.LayoutParams params = window.getAttributes();
			params.width = WindowManager.LayoutParams.MATCH_PARENT;
			//FLAG_DIM_BEHIND: 窗口之后的内容变暗  FLAG_BLUR_BEHIND: 窗口之后的内容变模糊。
			params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
			window.setAttributes(params);
		}
		String time = "12:200-14:00";
		String money = "1.5元配送费";
		for(int i = 0; i < 6; i++){
			final MultipleItemEntity entity = MultipleItemEntity.builder()
					.setItemType(ITEM_ARRIVAL_TIME_NORMAL)
					.setField(ARRIVAL_TIME_TIME, time)
					.setField(ARRIVAL_TIME_MONEY, money)
					.build();
			ENTITYS.add(entity);
		}

		final ArrivalTimeAdapter arrivalTimeAdapter = new ArrivalTimeAdapter(ENTITYS);

		mRvArrivalTime.setAdapter(arrivalTimeAdapter);
		final LinearLayoutManager manager = new LinearLayoutManager(getContext());
		mRvArrivalTime.setLayoutManager(manager);
		mRvArrivalTime.addOnItemTouchListener(new SimpleClickListener(){
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				final MultipleItemEntity entity = (MultipleItemEntity) adapter.getData().get(position);

				if(listener != null){
					final String tempMoney = entity.getField(ARRIVAL_TIME_MONEY);
					final String time = entity.getField(ARRIVAL_TIME_TIME);
					double money = Double.parseDouble(tempMoney.substring(0, tempMoney.length()-4));
					final ImageView checkView = (ImageView) findViewById(R2.id.iv_item_arrival_flag);
					checkView.setVisibility(View.VISIBLE);
					listener.onTimePick(time, money);
				}
			}

			@Override
			public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

			}

			@Override
			public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

			}

			@Override
			public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

			}
		});
	}

	@OnClick(R2.id.tv_arrival_time_toady)
	public void onViewClickedToady(View view) {

	}
	@OnClick(R2.id.tv_arrival_time_tomorrow)
	public void onViewClickedTomorrow(View view) {

	}
	@OnClick(R2.id.bt_cancle)
	public void onViewClickedCancle(View view) {
		dismiss();
	}

	public interface OnArrivalTimePickListener {
		public void onTimePick(String time, double money);
	}

	class ArrivalTimeAdapter extends BaseMultiItemQuickAdapter<MultipleItemEntity, BaseViewHolder> {

		private ArrivalTimeAdapter(List<MultipleItemEntity> data) {
			super(data);
			addItemType(ITEM_ARRIVAL_TIME_NORMAL, R.layout.item_settle_arrival_time);
		}

		@Override
		protected void convert(BaseViewHolder helper, MultipleItemEntity item) {
			switch (helper.getItemViewType()) {
				case ITEM_ARRIVAL_TIME_NORMAL:
					final TextView timeView =helper.getView(R.id.tv_item_arrival_time);
					timeView.setText((String)item.getField(ARRIVAL_TIME_TIME));
					final TextView moneyView =helper.getView(R.id.tv_item_arrival_distribution_money);
					moneyView.setText((String)item.getField(ARRIVAL_TIME_MONEY));

					break;
				default:
					break;
			}
		}
	}
}
