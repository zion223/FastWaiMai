package com.zrp.latte.ec.main.personal.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zrp.latte.ec.main.cart.ShopCartItemFields;
import com.zrp.latte.ui.recycler.DataConverter;
import com.zrp.latte.ui.recycler.MultipleFields;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class OrderListDataConverter extends DataConverter {



    @Override
    public LinkedList<MultipleItemEntity> convert() {
        //获取全部订单

        final JSONArray orderArray = JSON.parseObject(getJsonData()).getJSONArray("resultList");
        final int orderSize = orderArray.size();
        for(int i = 0; i < orderSize; i++){
            //每一个订单
            final JSONObject orderObject = orderArray.getJSONObject(i);
            //订单ID
            final String orderId = orderObject.getString("orderCode");
            //订单创建时间
            final String createTime = orderObject.getString("createTime");
            //订单总价格
            final Double totalPrice = orderObject.getDoubleValue("totalPrice");
            /**
             * 订单状态:
             *      0: 未付款
             *      1: 代发货
             *      2: 待收货
             *      3: 评价
             *      4: 退款/售后
             *
             *
             */
            final String orderStatus = orderObject.getString("orderStatus");
            //获取当前订单下的商品
            final JSONArray goodsArray = orderObject.getJSONArray("orderDetailList");
            final int goodsSize = goodsArray.size();

            final MultipleItemEntity headerEntity = MultipleItemEntity.builder()
                    .setItemType(OrderListItemType.ITEM_ORDER_LIST_HEADER)
                    .setField(OrderItemFields.ORDER_ID, orderId)
                    .setField(OrderItemFields.CREATE_TIME, createTime)
                    .setField(OrderItemFields.PAY_STATE, orderStatus)
                    .build();

            final MultipleItemEntity footEntity = MultipleItemEntity.builder()
                    .setItemType(OrderListItemType.ITEM_ORDER_LIST_FOOTER)
                    .setField(OrderItemFields.TOTAL_PRICE, totalPrice)
                    .build();

            ENTITYS.add(headerEntity);
            for(int j = 0; j < goodsSize; j++){
                final JSONObject goodsObject = goodsArray.getJSONObject(j);
                final String goodsName = goodsObject.getString("productName");
                final String goodsPic = goodsObject.getString("productPic");
                final double goodsPrice = goodsObject.getDoubleValue("price");
                final int goodsCount = goodsObject.getIntValue("count");

                final MultipleItemEntity contentEntity = MultipleItemEntity.builder()
                        .setItemType(OrderListItemType.ITEM_ORDER_LIST_CONTENT)
                        .setField(OrderItemFields.PRODUCT_COUNT, goodsCount)
                        .setField(OrderItemFields.PRODUCT_IMG, goodsPic)
                        .setField(OrderItemFields.PRODUCT_NAME, goodsName)
                        .setField(OrderItemFields.PRODUCT_PRICE, goodsPrice)
                        .build();
                ENTITYS.add(contentEntity);
            }
            ENTITYS.add(footEntity);

        }
        /**
         *  ENTITYS 添加数据的顺序影响显示的顺序
         *      headerEntity
         *      contentEntity
         *      footEntity
         */

        return ENTITYS;
    }
}
