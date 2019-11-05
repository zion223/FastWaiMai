package com.zrp.latte.ui.recycler;

import java.util.ArrayList;

public abstract class DataConverter {

    //仅限首页使用
    protected final ArrayList<MultipleItemEntity> ENTITYS = new ArrayList<>();

    private String mJonData = null;

    public abstract ArrayList<MultipleItemEntity> convert();

    public DataConverter setJsonData(String jsonData){
        this.mJonData = jsonData;
        return this;
    }
    //可以被继承类引用
    protected String getJsonData(){
        if(mJonData == null && mJonData.equals("")){
            throw new NullPointerException("json数据不能为空");
        }
        return this.mJonData;
    }

    public void clearData(){
        ENTITYS.clear();
    }

}
