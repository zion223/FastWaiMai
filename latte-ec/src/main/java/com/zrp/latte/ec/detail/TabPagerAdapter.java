package com.zrp.latte.ec.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<String> TAB_TITLES = new ArrayList<>();
    private final ArrayList<ArrayList<String>> PICTURES = new ArrayList<>();

    public TabPagerAdapter(FragmentManager fm, JSONObject data) {
        super(fm);
        final JSONArray tabArray = data.getJSONArray("tabs");
        final int size = tabArray.size();
        for(int i=0; i<size; i++){
            final JSONObject tab = tabArray.getJSONObject(i);
            final String name = tab.getString("name");
            final JSONArray picArray = tab.getJSONArray("pictures");
            final ArrayList<String> picList = new ArrayList<>();
            final int picSize = picArray.size();
            for(int j=0 ; j < picSize; j++){
                picList.add(picArray.getString(j));
            }
            TAB_TITLES.add(name);
            PICTURES.add(picList);
        }
    }


    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return ImageDelegate.create(PICTURES.get(0));
        }else {
            return ImageDelegate.create(PICTURES.get(1));
        }
    }

    @Override
    public int getCount() {
        return TAB_TITLES.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES.get(position);
    }
}
