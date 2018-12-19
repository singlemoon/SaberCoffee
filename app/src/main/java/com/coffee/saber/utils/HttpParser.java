package com.coffee.saber.utils;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simo on 17-3-6.
 */

public class HttpParser {

    public static Map<String,String> parseMapGet(String urlWithParam) {
        String response = HttpUtils.doGet(urlWithParam);
        Gson gson = new Gson();
        return gson.fromJson(response, new TypeToken<Map<String,String>>(){}.getType());
    }

    public static Map<String,String> parseMapPost(String url, String param) {
        String response = HttpUtils.doPost(url, param);
        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(response, new TypeToken<Map<String,String>>(){}.getType());
        Log.i("Http Parser", map.get("status"));
        return map;
    }
}
