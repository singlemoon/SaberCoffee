package com.coffee.saber.utils;

import java.lang.reflect.Type;

import com.coffee.saber.model.BaseModel;
import com.google.gson.Gson;

public class JsonUtils {

	public static String toJson(BaseModel model) {
		Gson gson = new Gson();
		return gson.toJson(model);
	}

	public static String toJson(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}

	public static <T> T fromJson(String json, Type typeOfT) {
		Gson gson = new Gson();
		return gson.fromJson(json, typeOfT);
	}
}
