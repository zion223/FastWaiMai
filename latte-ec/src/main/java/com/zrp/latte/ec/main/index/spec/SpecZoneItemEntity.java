package com.zrp.latte.ec.main.index.spec;

public class SpecZoneItemEntity {
    private int mId = 0;
    private String mTitle = null;
    private String mSubTitle = null;
    private String mImgOne = null;
    private String mImgTwo = null;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    public void setSubTitle(String subTitle) {
        this.mSubTitle = subTitle;
    }

    public String getImgOne() {
        return mImgOne;
    }

    public void setImgOne(String imgOne) {
        this.mImgOne = imgOne;
    }

    public String getImgTwo() {
        return mImgTwo;
    }

    public void setImgTwo(String imgTwo) {
        this.mImgTwo = imgTwo;
    }
}
