package com.zrp.latte.ec.main.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.ycbjie.slide.VerticalScrollView;
import com.zrp.latte.delegates.LatteDelegate;

import butterknife.BindView;



public class ShopMainFragment extends LatteDelegate {


    @BindView(R2.id.tv_goods_title)
    TextView mTvGoodsTitle;
    @BindView(R2.id.tv_new_price)
    TextView mTvNewPrice;
    @BindView(R2.id.tv_old_price)
    TextView mTvOldPrice;
    @BindView(R2.id.tv_current_goods)
    TextView mTvCurrentGoods;
    @BindView(R2.id.ll_current_goods)
    LinearLayout mLlCurrentGoods;
    @BindView(R2.id.iv_ensure)
    ImageView mIvEnsure;
    @BindView(R2.id.tv_comment_count)
    TextView mTvCommentCount;
    @BindView(R2.id.tv_good_comment)
    TextView mTvGoodComment;
    @BindView(R2.id.iv_comment_right)
    ImageView mIvCommentRight;
    @BindView(R2.id.ll_comment)
    LinearLayout mLlComment;
    @BindView(R2.id.ll_empty_comment)
    LinearLayout mLlEmptyComment;
    @BindView(R2.id.ll_recommend)
    LinearLayout mLlRecommend;
    @BindView(R2.id.tv_bottom_view)
    TextView mTvBottomView;
    @BindView(R2.id.scrollView)
    VerticalScrollView mScrollView;


    @Override
    public Object setLayout() {
        return R.layout.include_shop_main;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

    }

    public void changBottomView(boolean isDetail) {
        if (isDetail) {
            mTvBottomView.setText("下拉回到商品详情");
        } else {
            mTvBottomView.setText("继续上拉，查看图文详情");
        }
    }

}
