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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.example.latte.ui.R;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;
import com.zrp.latte.util.timer.DateUtil;

import java.util.LinkedList;
import java.util.List;


public class PickArrivalTimeDialog extends Dialog implements View.OnClickListener{

	private TextView mTvArrivalTimeTitle;
	private TextView mTvArrivalTimeToady;
	private TextView mTvArrivalTimeTomorrow;
	private RecyclerView mRvArrivalTime;
	private Button mCancleButton;

	private LinkedList<MultipleItemEntity> todayEntities = new LinkedList<>();
	private LinkedList<MultipleItemEntity> tomorrowEntities = new LinkedList<>();

	private final ArrivalTimeDialogListener listener = new ArrivalTimeDialogListener();

	private OnArrivalTimePickListener mListener;
	private ArrivalTimeAdapter adapter = null;

	private int mTodayPrePosition = 0;
	private int mTomorrowPrePosition = 0;

	private static final int ITEM_ARRIVAL_TIME_NORMAL = 30;
	//配送具体时间
	private static final int ARRIVAL_TIME_TIME = 31;
	//配送费
	private static final int ARRIVAL_TIME_MONEY = 32;
	//配送是今天还是明天
	private static final int ARRIVAL_TIME_DAYTYPE = 33;

	public PickArrivalTimeDialog(@NonNull Context context) {
		super(context);
	}

	public void setmListener(OnArrivalTimePickListener mListener) {
		this.mListener = mListener;
	}

	public PickArrivalTimeDialog(@NonNull Context context, String title, OnArrivalTimePickListener listener) {
		super(context);
		this.mListener = listener;
		mTvArrivalTimeTitle.setText(title);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_settle_arrival_time);

		mTvArrivalTimeTitle = (TextView) findViewById(R.id.tv_arrival_time_title);
		mTvArrivalTimeToady = (TextView) findViewById(R.id.tv_arrival_time_toady);
		mTvArrivalTimeTomorrow = (TextView) findViewById(R.id.tv_arrival_time_tomorrow);
		mRvArrivalTime = (RecyclerView) findViewById(R.id.rv_arrival_time);
		mCancleButton = (Button) findViewById(R.id.bt_cancle);

		mTvArrivalTimeToady.setText(DateUtil.getWeek());
		mTvArrivalTimeTomorrow.setText(DateUtil.getWeek(1));

		mCancleButton.setOnClickListener(this);
		mTvArrivalTimeTomorrow.setOnClickListener(this);
		mTvArrivalTimeToady.setOnClickListener(this);

		initTodayArrivalTime();
		initTomorrowArrivalTime();
		final Window window = getWindow();
		if(window != null){
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

		adapter = new ArrivalTimeAdapter(todayEntities);

		mRvArrivalTime.setAdapter(adapter);
		final LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
		mRvArrivalTime.setLayoutManager(manager);
		mRvArrivalTime.addOnItemTouchListener(listener);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if( id == R.id.bt_cancle){
			dismiss();
		}else if(id == R.id.tv_arrival_time_toady){
			mTvArrivalTimeToady.setBackgroundColor(Color.WHITE);
			mTvArrivalTimeTomorrow.setBackgroundColor(Color.parseColor("#11111111"));
			adapter.setNewData(todayEntities);
		}else if(id == R.id.tv_arrival_time_tomorrow){
			mTvArrivalTimeToady.setBackgroundColor(Color.parseColor("#11111111"));
			mTvArrivalTimeTomorrow.setBackgroundColor(Color.WHITE);
			adapter.setNewData(tomorrowEntities);
		}
	}

	private void initTodayArrivalTime() {

		final MultipleItemEntity nowEntity = MultipleItemEntity.builder()
				.setItemType(ITEM_ARRIVAL_TIME_NORMAL)
				.setField(ARRIVAL_TIME_TIME, getContext().getString(R.string.immediate_delivery))
				.setField(MultipleFields.TAG, true)
				.setField(ARRIVAL_TIME_MONEY, "2")
				.setField(ARRIVAL_TIME_DAYTYPE, 0)
				.build();
		todayEntities.add(nowEntity);
		String time = "20:40";
		String money = "5";
		for(int i = 0; i < 12; i++){
			final MultipleItemEntity entity = MultipleItemEntity.builder()
					.setItemType(ITEM_ARRIVAL_TIME_NORMAL)
					.setField(ARRIVAL_TIME_TIME, time)
					.setField(MultipleFields.TAG, false)
					.setField(ARRIVAL_TIME_MONEY, money)
					.setField(ARRIVAL_TIME_DAYTYPE, 0)
					.build();
			todayEntities.add(entity);
		}
	}

	private void initTomorrowArrivalTime() {
		String time = "13:00";
		String money = "5";
		for(int i = 0; i < 12; i++){
			final MultipleItemEntity entity = MultipleItemEntity.builder()
					.setItemType(ITEM_ARRIVAL_TIME_NORMAL)
					.setField(ARRIVAL_TIME_TIME, time)
					.setField(MultipleFields.TAG, false)
					.setField(ARRIVAL_TIME_MONEY, money)
					.setField(ARRIVAL_TIME_DAYTYPE, 1)
					.build();
			tomorrowEntities.add(entity);
		}
	}

	public interface OnArrivalTimePickListener {
		//dayType: 0-立即送达  1-指定时间 2-明天
		void onTimePick(String type, String time, double money);
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
					final TextView timeView = helper.getView(R.id.tv_item_arrival_time);
					timeView.setText((String)item.getField(ARRIVAL_TIME_TIME));
					final TextView moneyView = helper.getView(R.id.tv_item_arrival_distribution_money);
					final ImageView choosedView = helper.getView(R.id.iv_item_arrival_flag);
					moneyView.setText(String.format("%s%s", item.getField(ARRIVAL_TIME_MONEY), getContext().getString(R.string.currency_suffix)));
					boolean choosed = item.getField(MultipleFields.TAG);
					if(choosed){
						choosedView.setVisibility(View.VISIBLE);
						timeView.setTextColor(Color.BLUE);
						moneyView.setTextColor(Color.BLUE);
					}else{
						choosedView.setVisibility(View.INVISIBLE);
						timeView.setTextColor(Color.BLACK);
						moneyView.setTextColor(Color.BLACK);
					}
					break;
				default:
					break;
			}
		}

	}
	class ArrivalTimeDialogListener extends SimpleClickListener{

		@Override
		public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
			final MultipleItemEntity entity = (MultipleItemEntity) adapter.getData().get(position);
			String type = null;
			if(mListener != null){
				final double money = Double.parseDouble((String) entity.getField(ARRIVAL_TIME_MONEY));
				final int dayType = entity.getField(ARRIVAL_TIME_DAYTYPE);
				String time = entity.getField(ARRIVAL_TIME_TIME);

				if(dayType == 0 && mTodayPrePosition != position){
					((MultipleItemEntity)adapter.getData().get(mTodayPrePosition)).setField(MultipleFields.TAG, false);

					adapter.notifyItemChanged(mTodayPrePosition);
					entity.setField(MultipleFields.TAG, true);
					adapter.notifyItemChanged(position);
					mTodayPrePosition = position;
				}else if(mTomorrowPrePosition != position){
						time = "明天" + time;
						((MultipleItemEntity)adapter.getData().get(mTomorrowPrePosition)).setField(MultipleFields.TAG, false);
						adapter.notifyItemChanged(mTomorrowPrePosition);
						entity.setField(MultipleFields.TAG, true);
						adapter.notifyItemChanged(position);
						mTomorrowPrePosition = position;
					}
				if(position != 0){
					((MultipleItemEntity)adapter.getData().get(0)).setField(MultipleFields.TAG, false);
				}
				if(dayType == 0 && position == 0){
					type = "立即送达";
				}else{
					type = "指定时间";
				}
				mListener.onTimePick(type, time, money);
				dismiss();
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
	}

}
