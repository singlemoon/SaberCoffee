package com.coffee.saber.utils;

import android.net.wifi.aware.PublishConfig;

/**
 * Created by Simo on 2018/12/10.
 */
public class Global {
    // 这里要换成自己的ip
//    private static final String BASE_URL = "http://192.168.1.1:8080/SaberCoffee/";
    private static final String BASE_URL = "http://192.168.1.5:8080/SaberCoffee/";

    private static final String USER_CONTROLLER = BASE_URL + "UserController";
    private static final String ORDER_CONTROLLER = BASE_URL + "OrderController";
    private static final String PRODUCT_CONTROLLER = BASE_URL + "ProductController";
    private static final String SHOPPING_CART_CONTROLLER = BASE_URL + "ShoppingCartController";

    // 用户
    public static final String LOGIN_URL = USER_CONTROLLER + "?action=login";
    public static final String REGISTER_URL = USER_CONTROLLER + "?action=register";
    public static final String UPDATE_USER = USER_CONTROLLER + "?action=updateUser";

    public static final String PRODUCT_LIST_URL = PRODUCT_CONTROLLER+ "?action=product_list";
    public static final String BUY_URL = ORDER_CONTROLLER+ "?action=buy";

    public static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS USER (" +
            "  id INT(11) NOT NULL, " +
            "  username VARCHAR(50) NOT NULL," +
            "  PASSWORD VARCHAR(50) NOT NULL," +
            "  nick VARCHAR(50) DEFAULT NULL," +
            "  phone VARCHAR(11) DEFAULT NULL," +
            "  sex INT(1) DEFAULT NULL," +
            "  PRIMARY KEY (id)" +
            ")";
}
