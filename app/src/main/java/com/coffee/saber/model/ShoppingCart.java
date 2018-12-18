package com.coffee.saber.model;

public class ShoppingCart extends BaseModel {
	private int id;
	private int userId;
	private int productId;
	private int num;
	private String productName;
	private String describe;
	private int productPrice;

	public ShoppingCart() {
		super();
	}
	
	public ShoppingCart(int userId, int productId, int num) {
		super();
		this.userId = userId;
		this.productId = productId;
		this.num = num;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	@Override
	public String toString() {
		return "ShoppingCart{" +
				"id=" + id +
				", userId=" + userId +
				", productId=" + productId +
				", num=" + num +
				", productName='" + productName + '\'' +
				", describe='" + describe + '\'' +
				", productPrice=" + productPrice +
				'}';
	}
}
