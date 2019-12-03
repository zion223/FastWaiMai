package com.zrp.latte.ec.main.index.spec;

import com.chad.library.adapter.base.entity.SectionEntity;

public class SpecZoneBean extends SectionEntity<SpecZoneItemEntity> {

    private boolean mIsMore = false;
    private int mId = -1;

    public SpecZoneBean(SpecZoneItemEntity specItemEntity) {
        super(specItemEntity);
    }

    public SpecZoneBean(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public boolean isMore() {
        return mIsMore;
    }

    public void setIsMore(boolean isMore) {
        this.mIsMore = mIsMore;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = mId;
    }
}
