package com.zrp.latte.ec.main.index.spec;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.latte.latte_ec.R;

import java.util.List;

public class SpecZoneAdapter extends BaseSectionQuickAdapter<SpecZoneBean, BaseViewHolder>{

    private static final RequestOptions RECYCLE_OPTIONS =
            new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();

    public SpecZoneAdapter(int layoutResId, int sectionHeadResId, List<SpecZoneBean> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, SpecZoneBean item) {
        helper.setText(R.id.tv_spec_title_header, "特色专区");
    }

    @Override
    protected void convert(BaseViewHolder helper, SpecZoneBean item) {
        final String title = item.t.getTitle();
        final String subTitle = item.t.getSubTitle();
        final String subImg1 = item.t.getImgOne();
        final String subImg2 = item.t.getImgTwo();

        helper.setText(R.id.tv_spec_title, title);
        helper.setText(R.id.tv_spec_subtitle, subTitle);
        Glide.with(mContext)
                .load(subImg1)
                .apply(RECYCLE_OPTIONS)
                .into((ImageView) helper.getView(com.example.latte.ui.R.id.iv_spec_imgae_one));
        Glide.with(mContext)
                .load(subImg2)
                .apply(RECYCLE_OPTIONS)
                .into((ImageView) helper.getView(com.example.latte.ui.R.id.iv_spec_imgae_two));
    }
}
