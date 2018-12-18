package com.coffee.saber.utils;

import android.net.wifi.aware.PublishConfig;

/**
 * Created by Simo on 2018/12/10.
 */
public class Global {
    private static final String BASE_URL = "http://192.168.1.1:8080/SaberCoffee/";

    private static final String USER_CONTROLLER = BASE_URL + "UserController/";
    private static final String ORDER_CONTROLLER = BASE_URL + "OrderController/";
    private static final String PRODUCT_CONTROLLER = BASE_URL + "ProductController/";
    private static final String SHOPPING_CART_CONTROLLER = BASE_URL + "ShoppingCartController/";

    public static final String LOGIN_URL = USER_CONTROLLER + "?action=login";
    public static final String PRODUCT_LIST_URL = PRODUCT_CONTROLLER+ "?action=product_list";
    public static final String BUY_URL = ORDER_CONTROLLER+ "?action=buy";
}
