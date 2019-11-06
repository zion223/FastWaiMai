package com.zrp.latte.ec.main.cart;

public interface ICartItemListener {
    //计算购物车总价格
    void onItemClick(double totalPrice);
    //计算购物车是否清空
    void checkItemCount();
}
