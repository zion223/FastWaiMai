package com.zrp.latte.ec.main.personal.order;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.latte.latte_ec.R;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderListAdapter extends BaseMultiItemQuickAdapter<MultipleItemEntity, BaseViewHolder> {


    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public OrderListAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(OrderListItemType.ITEM_ORDER_LIST_HEADER, R.layout.item_allorder_header);
        addItemType(OrderListItemType.ITEM_ORDER_LIST_CONTENT, R.layout.item_allorder_each_goods);
        addItemType(OrderListItemType.ITEM_ORDER_LIST_FOOTER, R.layout.item_allorder_footer);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItemEntity item) {

        switch (helper.getItemViewType()){
            case OrderListItemType.ITEM_ORDER_LIST_HEADER:
                //订单ID
                String orderId = item.getField(OrderItemFields.ORDER_ID);
                //创建时间
                String createTime = item.getField(OrderItemFields.CREATE_TIME);
                //订单状态
                String orderStatus = item.getField(OrderItemFields.PAY_STATE);
                final AppCompatTextView orderIdView = helper.getView(R.id.tv_item_allorder_orderid);
                helper.setText(R.id.tv_item_allorder_state, orderStatus);
                //helper.setText(R.id.tv_item_allorder_createtime, createTime);
                orderIdView.setText(String.valueOf("订单编号:"+orderId));
                break;
            case OrderListItemType.ITEM_ORDER_LIST_CONTENT:
                final String goodsName = item.getField(OrderItemFields.PRODUCT_NAME);
                final String goodsPic = item.getField(OrderItemFields.PRODUCT_IMG);
                final double goodsPrice = item.getField(OrderItemFields.PRODUCT_PRICE);
                final int count = item.getField(OrderItemFields.PRODUCT_COUNT);
                helper.setText(R.id.tv_item_allorder_title, goodsName);
                helper.setText(R.id.tv_item_allorder_item_num, "共"+count+"件");
                helper.setText(R.id.tv_item_allorder_item_price, goodsPrice+"");
                final AppCompatImageView imgThumb = helper.getView(R.id.iv_item_allorder_pic);
                Glide.with(mContext)
                        .load(goodsPic)
                        .apply(OPTIONS)
                        .into(imgThumb);
                break;
            case OrderListItemType.ITEM_ORDER_LIST_FOOTER:
                final Double totalPrice = item.getField(OrderItemFields.TOTAL_PRICE);
                helper.setText(R.id.tv_item_allorder_total, totalPrice+"");
                break;
            default:
                break;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int positions) {
        super.onBindViewHolder(holder, positions);
    }
}
