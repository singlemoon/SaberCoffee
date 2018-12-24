package com.coffee.saber.model;

public class Order extends BaseModel{
	private int id;
	private String orderCode;
	private int userId;
	private int productId;
	private int num;
	private int status;
	private long createTime;
	private long endTime;
	private String productName;
	private int productPrice;
	private String remark;
	
	public Order() {
		super();
	}

	public Order(int userId, int productId, int num) {
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

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	@Override
	public String toString() {
		return "Order{" +
				"id=" + id +
				", orderCode='" + orderCode + '\'' +
				", userId=" + userId +
				", productId=" + productId +
				", num=" + num +
				", status=" + status +
				", createTime=" + createTime +
				", endTime=" + endTime +
				", productName='" + productName + '\'' +
				", productPrice=" + productPrice +
				", remark='" + remark + '\'' +
				'}';
	}
}
