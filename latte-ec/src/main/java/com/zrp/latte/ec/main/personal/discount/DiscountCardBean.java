package com.zrp.latte.ec.main.personal.discount;

import android.support.annotation.IdRes;
import android.view.View;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zrp.latte.delegates.LatteDelegate;

public class DiscountCardBean implements MultiItemEntity {


    private int mItemType = 0;
    private String mDiscountCardName = null;
    private String mDiscountCardDetail = null;
    private LatteDelegate mDelegate = null;
    private int mId = 0;

    private View.OnClickListener mOnClickListener = null;
    @IdRes
    private int mDiscountCardUrlId = 0;
    public DiscountCardBean(int mItemType, String mDiscountCardName, String mDiscountCardDetail,
                            LatteDelegate mDelegate, int mId, View.OnClickListener mOnClickListener,
                            int mDiscountCardUrlId) {
        this.mItemType = mItemType;
        this.mDiscountCardName = mDiscountCardName;
        this.mDiscountCardDetail = mDiscountCardDetail;
        this.mDelegate = mDelegate;
        this.mId = mId;
        this.mOnClickListener = mOnClickListener;
        this.mDiscountCardUrlId = mDiscountCardUrlId;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }

    public String getDiscountCardName() {
        return mDiscountCardName;
    }

    public String getDiscountCardDetail() {
        return mDiscountCardDetail;
    }

    public LatteDelegate getDelegate() {
        return mDelegate;
    }

    public int getId() {
        return mId;
    }

    public View.OnClickListener getOnClickListener() {
        if(mOnClickListener != null){
            return mOnClickListener;
        }else{
            throw new NullPointerException("OnClickListener is null!!!");
        }
    }


    public int getDiscountCardUrlId() {
        return mDiscountCardUrlId;
    }

    public static final class Builder{
        private int itemType = 0;
        private String discountCardName = null;
        private String discountCardDetail = null;
        private int discountCardUrlId = 0;
        private LatteDelegate delegate = null;
        private int id = 0;
        private View.OnClickListener onClickListener = null;

        public Builder setItemType(int mItemType) {
            this.itemType = mItemType;
            return this;
        }

        public Builder setDiscountCardUrlId(int discountCardUrlId) {
            this.discountCardUrlId = discountCardUrlId;
            return this;
        }

        public Builder setDiscountCardName(String mDiscountCardName) {
            this.discountCardName = mDiscountCardName;
            return this;
        }

        public Builder setDiscountCardDetail(String mDiscountCardDetail) {
            this.discountCardDetail = mDiscountCardDetail;
            return this;
        }

        public Builder setDelegate(LatteDelegate mDelegate) {
            this.delegate = mDelegate;
            return this;
        }

        public Builder setId(int mId) {
            this.id = mId;
            return this;
        }

        public Builder setOnClickListener(View.OnClickListener mOnClickListener) {
            this.onClickListener = mOnClickListener;
            return this;
        }
        public DiscountCardBean build(){
            return new DiscountCardBean(itemType,discountCardName,discountCardDetail,delegate,id,onClickListener,discountCardUrlId);
        }
    }
}
