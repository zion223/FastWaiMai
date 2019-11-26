package com.zrp.latte.ec.main.personal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.ec.main.personal.address.AddressDelegate;
import com.zrp.latte.ec.main.personal.discount.DiscountCardAdapter;
import com.zrp.latte.ec.main.personal.discount.DiscountCardDataConverter;
import com.zrp.latte.ec.main.personal.order.OrderPageViewDelegate;
import com.zrp.latte.ec.main.personal.order.OrderStatusType;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.ui.recycler.DataConverter;
import com.zrp.latte.ui.widget.CircleTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalDelegate extends BottomItemDelegate {

    @BindView(R2.id.tv_top_txtTitle)
    TextView tvTopTxtTitle;
    @BindView(R2.id.tv_top_edit)
    TextView tvTopEdit;
    @BindView(R2.id.img_user_avatar)
    ImageView imgUserAvatar;
    @BindView(R2.id.textView1)
    TextView textView1;
    @BindView(R2.id.tv_all_order)
    TextView tvAllOrder;
    @BindView(R2.id.rv_discount_card)
    RecyclerView mRvDiscountCard;
    @BindView(R2.id.tv_address_list)
    TextView mTvPersonalAddress;
    @BindView(R2.id.tv_order_status_0)
    CircleTextView mTvOrderPay;
    @BindView(R2.id.tv_order_status_1)
    CircleTextView mTvOrderReceive;
    @BindView(R2.id.tv_order_status_2)
    CircleTextView mTvOrderEvaluate;
    @BindView(R2.id.tv_order_status_3)
    CircleTextView mTvOrderAfterMarket;



    /**
     * 个人页面数据分为三部分
     * 头部: 个人用户名、头像、收藏店铺宝贝、个人等级
     * 中部: 订单详情 不同订单状态数据 订单数量
     * 底部: 优惠劵卡包
     */

    @Override
    public Object setLayout() {
        return R.layout.delegate_personal;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRvDiscountCard.setLayoutManager(manager);

        //收货地址
        mTvPersonalAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportDelegate().start(new AddressDelegate());
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {

        RestClient.builder()
                .url("api/personal")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject order = JSON.parseObject(response).getJSONObject("order");
                        //不同状态订单数量
                        mTvOrderPay.setText(String.valueOf(order.getIntValue("PAY")));
                        mTvOrderReceive.setText(String.valueOf(order.getIntValue("RECEIVE")));
                        mTvOrderEvaluate.setText(String.valueOf(order.getIntValue("EVALUATE")));
                        mTvOrderAfterMarket.setText(String.valueOf(order.getIntValue("AFTERMARKET")));
                        //优惠券部分
                        final String discount = JSON.parseObject(response).getJSONArray("discount").toJSONString();
                        final DataConverter cardDataConverter = new DiscountCardDataConverter().setJsonData(discount);
                        final DiscountCardAdapter discountCardAdapter = new DiscountCardAdapter(cardDataConverter.convert());
                        mRvDiscountCard.setAdapter(discountCardAdapter);
                    }
                })
                .build()
                .get();
    }

    @OnClick(R2.id.tv_all_order)
    void onClickAllOrder() {
        getSupportDelegate().start(OrderPageViewDelegate.create(OrderStatusType.ALL));
    }
    
    @OnClick(R2.id.rl_pay)
    void onClickedPay() {
        getSupportDelegate().start(OrderPageViewDelegate.create(OrderStatusType.PAY));
    }
    @OnClick(R2.id.rl_receive)
    void onClickedReceive() {
        getSupportDelegate().start(OrderPageViewDelegate.create(OrderStatusType.RECEIVE));
    }
    @OnClick(R2.id.rl_evaluate)
    void onClickedEvaluate() {
        getSupportDelegate().start(OrderPageViewDelegate.create(OrderStatusType.EVALUATE));
    }
    @OnClick(R2.id.rl_after_market)
    void onClickedAfterMarket() {
        getSupportDelegate().start(OrderPageViewDelegate.create(OrderStatusType.AFTER_MARKET));
    }
}
