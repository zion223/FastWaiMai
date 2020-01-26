package com.zrp.latte.ec.main.personal.p_function.address.edit;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.latte.latte_ec.R;
import com.zrp.latte.app.Latte;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.ec.main.personal.p_function.address.AddressEditDelegate;
import com.zrp.latte.ec.main.personal.p_function.address.AddressItemFields;
import com.zrp.latte.ec.main.personal.p_function.address.AddressItemType;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;
import com.zrp.latte.ui.recycler.MultipleRecyclerAdapter;
import com.zrp.latte.ui.recycler.MultipleViewHolder;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

public class SearchAddressAdapter extends BaseQuickAdapter<PoiInfo, BaseViewHolder> {

    private LatLng currentLatLng;

    public SearchAddressAdapter(@Nullable List<PoiInfo> data, LatLng currentLatLng) {
        super(R.layout.item_search_address, data);
        this.currentLatLng = currentLatLng;

    }

    @Override
    protected void convert(BaseViewHolder helper, PoiInfo item) {

        final ImageView ivPointImage = helper.getView(R.id.iv_point);
        final TextView tvName = helper.getView(R.id.tv_name);
        final TextView tvAddress = helper.getView(R.id.tv_address);
        final TextView tvDistance = helper.getView(R.id.tv_distance);
        LatLng latLng = item.getLocation();

        double distance = DistanceUtil.getDistance(currentLatLng, latLng);
        if(helper.getPosition() == 0){
            ivPointImage.setImageResource(R.drawable.point_orange);
            tvName.setTextColor(Latte.getApplication().getResources().getColor(R.color.orange));
        }else{
            ivPointImage.setImageResource(R.drawable.point_gray);
            tvName.setTextColor(Latte.getApplication().getResources().getColor(R.color.black));
        }
        tvName.setText(item.getName());
        tvAddress.setText(item.getAddress());
        tvDistance.setText(formatDistance(distance));
    }


    private String formatDistance(double distance){
        String str;
        if(distance >= 1000){
            DecimalFormat df = new DecimalFormat("#.00");
            double b = distance/1000;
            str = df.format(b) + "千米";
        }else{
            DecimalFormat df = new DecimalFormat("######0");
            str = df.format(distance) + "米";
        }
        return str;
    }
}
