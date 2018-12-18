package com.coffee.saber.utils;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simo on 17-3-6.
 */

public class HttpParser {

    public static Map<String,String> parseMapGet(String urlWithParam) {
        String response = HttpUtils.doGet(urlWithParam);
        return new HashMap<>();
    }

    public static Map<String,String> parseMapPost(String url, String param) {
        String response = HttpUtils.doPost(url, param);
        return new HashMap<>();
    }
}
