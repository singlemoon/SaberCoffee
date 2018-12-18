package com.coffee.saber.model;

import com.coffee.saber.utils.JsonUtils;

public class BaseModel {
	
	public BaseModel() {
		// TODO Auto-generated constructor stub
	}
	
	public String toJson() {
		return JsonUtils.toJson(this);
	}
}
