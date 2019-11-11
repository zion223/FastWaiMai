package com.zrp.latte.ec.main.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.zrp.latte.delegates.bottom.BottomItemDelegate;
import com.zrp.latte.ec.main.personal.address.AddressDelegate;
import com.zrp.latte.ec.main.personal.discount.DiscountCardAdapter;
import com.zrp.latte.ec.main.personal.discount.DiscountCardBean;
import com.zrp.latte.ec.main.personal.discount.DiscountCardItemType;
import com.zrp.latte.ec.main.personal.order.OrderDelegate;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
    @BindView(R2.id.ll_pay)
    LinearLayout llPay;
    @BindView(R2.id.textView)
    TextView textView;
    @BindView(R2.id.ll_receive)
    LinearLayout llReceive;
    @BindView(R2.id.ll_evaluate)
    LinearLayout llEvaluate;
    @BindView(R2.id.ll_after_market)
    LinearLayout llAfterMarket;
    @BindView(R2.id.rv_discount_card)
    RecyclerView mRvDiscountCard;
    @BindView(R2.id.tv_address_list)
    TextView mTvPersonalAddress;


    /**
     * 个人页面数据分为三部分
     * 头部: 个人用户名、头像、收藏店铺宝贝、个人等级
     * 中部: 订单详情 不同订单状态数据
     * 底部: 优惠劵卡包
     */

    @Override
    public Object setLayout() {
        return R.layout.delegate_personal;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        //模拟优惠券数据
        final ArrayList discountCardData = initDiscardCountList();

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRvDiscountCard.setLayoutManager(manager);
        final DiscountCardAdapter discountCardAdapter = new DiscountCardAdapter(discountCardData);
        mRvDiscountCard.setAdapter(discountCardAdapter);
        //收货地址
        mTvPersonalAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到 AddressDelegate
                getSupportDelegate().start(new AddressDelegate());
            }
        });
    }

    @NonNull
    private ArrayList initDiscardCountList() {
        //优惠劵卡包数据绑定
        final DiscountCardBean bean1 = new DiscountCardBean.Builder()
                .setDelegate(this)
                .setItemType(DiscountCardItemType.ITEM_NORMAL)
                .setDiscountCardName("生活卷")
                .setDiscountCardDetail("让生活更美好")
                .setDiscountCardUrlId(R.drawable.user_8)
                .setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(_mActivity, "生活卷", Toast.LENGTH_SHORT).show();
                    }
                })
                .setId(1)
                .build();
        final DiscountCardBean bean2 = new DiscountCardBean.Builder()
                .setDelegate(this)
                .setItemType(DiscountCardItemType.ITEM_NORMAL)
                .setDiscountCardName("网店券")
                .setDiscountCardDetail("暂无可用优惠券")
                .setDiscountCardUrlId(R.drawable.user_9)
                .setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(_mActivity, "生活卷", Toast.LENGTH_SHORT).show();
                    }
                })
                .setId(2)
                .build();
        final DiscountCardBean bean3 = new DiscountCardBean.Builder()
                .setDelegate(this)
                .setItemType(DiscountCardItemType.ITEM_NORMAL)
                .setDiscountCardName("会员卡")
                .setDiscountCardDetail("一卡在手，逛店无忧")
                .setDiscountCardUrlId(R.drawable.user_10)
                .setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(_mActivity, "生活卷", Toast.LENGTH_SHORT).show();
                    }
                })
                .setId(2)
                .build();
        final ArrayList discountCardData = new ArrayList<DiscountCardBean>();
        discountCardData.add(bean1);
        discountCardData.add(bean2);
        discountCardData.add(bean3);
        return discountCardData;
    }

    @OnClick(R2.id.tv_all_order)
    void allOrder(){
        getSupportDelegate().start(new OrderDelegate());
    }

}
