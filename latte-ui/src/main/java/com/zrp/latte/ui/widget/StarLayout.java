package com.zrp.latte.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.latte.ui.R;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;

public class StarLayout extends LinearLayout implements View.OnClickListener {

    private static final CharSequence ICON_UN_SELECT = "fa-star-o";
    private static final CharSequence ICON_SELECTED = "fa-star";
    private static final int STAR_TOTAL_COUNT = 5;
    private static final ArrayList<IconTextView> STARS = new ArrayList<>();

    public StarLayout(Context context) {
        super(context);
    }

    public StarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStarIcon();
    }



    private void initStarIcon(){
        for(int i=0; i<STAR_TOTAL_COUNT ;i++){
            final IconTextView star = new IconTextView(getContext());
            //int width, int height
            final LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            star.setLayoutParams(lp);
            star.setText(ICON_UN_SELECT);
            star.setTag(R.id.star_count, i);
            star.setTag(R.id.star_is_selected, false);
            star.setOnClickListener(this);
            STARS.add(star);
            this.addView(star);
        }
    }

    @Override
    public void onClick(View v) {
        final IconTextView star = (IconTextView)v;
        final int position = (int) star.getTag(R.id.star_count);
        final boolean isSelect = (boolean)v.getTag(R.id.star_is_selected);
        if(isSelect){
            //取消
            unSelectStar(position);
        }else{
            selectStar(position);
        }
    }
    //获取实心星星的个数
    public int getSlectedStarCount() {
        int count = 0;
        for(int i=0; i<STAR_TOTAL_COUNT ;i++){
            IconTextView star = STARS.get(i);
            boolean selected = (boolean) star.getTag(R.id.star_is_selected);
            if(selected){
                count ++;
            }
        }
        return count;
    }
    private void selectStar(int position) {
        for(int i=0;i<position;i++){
            final IconTextView star = STARS.get(i);
            star.setTextColor(Color.RED);
            star.setTag(R.id.star_is_selected, true);
        }
    }
    private void unSelectStar(int position) {
        for(int i=0;i<position;i++){
            final IconTextView star = STARS.get(i);
            star.setTextColor(Color.GRAY);
            star.setTag(R.id.star_is_selected, false);
        }
    }
}
