package com.coffee.saber.utils;

import com.coffee.saber.model.BaseModel;
import com.coffee.saber.model.Order;
import com.coffee.saber.model.Product;
import com.coffee.saber.model.ShoppingCart;
import com.coffee.saber.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CoffeeSecret
 * Created by Simo on 2017/2/15.
 */

public class TestData {

    public static List<Product> getProducts() {
        String[] names = {"摩卡", "香草拿铁", "拿铁", "澳洲白", "标准美式", "加浓美式", "焦糖玛奇朵", "卡布奇诺", "焦糖美式", "榛果拿铁"};
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setId(i);
            product.setName(names[i]);
            product.setDescribe(names[i] + "超级好喝，一定来喝");
            product.setPrice((int)(Math.random()*10)*i);
            product.setRemark(names[i] + "：冬天暖心又暖胃");
            products.add(product);
        }
        return products;
    }

    public static List<Order> getOrders() {
        String[] names = {"摩卡", "香草拿铁", "拿铁", "澳洲白", "标准美式", "加浓美式", "焦糖玛奇朵", "卡布奇诺", "焦糖美式", "榛果拿铁"};
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setId(i);
            order.setProductId(1);
            order.setOrderCode("201812120000" + i);
            order.setRemark(names[i] + "：冬天暖心又暖胃");
            orders.add(order);
        }
        return orders;
    }

    public static  List<ShoppingCart> getShoppingCarts() {
        List<Product> products = getProducts();

        List<ShoppingCart> shoppingCarts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ShoppingCart shoppingCart = new ShoppingCart();
            Product product = products.get(i);
            shoppingCart.setId(i);
            shoppingCart.setProductId(i);
            shoppingCart.setProductName(product.getName());
            shoppingCart.setDescribe(product.getDescribe());
            shoppingCart.setUserId(1);
            shoppingCart.setNum(i);
            shoppingCart.setProductPrice(product.getPrice());
            shoppingCarts.add(shoppingCart);
        }

        return shoppingCarts;
    }

    public static User getUser() {
        User user = new User();
        user.setId(1);
        user.setNick("夕阳墨白");
        user.setPhone("13175003361");
        user.setSex(1);
        return user;
    }
}
