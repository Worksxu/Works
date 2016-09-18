package com.ruiqi.bean;

import java.io.Serializable;

/**
 * 订单的一些基础信息
 * @author Administrator
 *
 */
public class TableInfo implements Serializable{
	private String orderNum;//订单号
	
	private String orderMoney;//订单金额
	
	private String orderStatus;//订单状态
	
	private String orderTime;//下单时间 
	private String kid;//下单kid 
	private String yuqi; //余气钱
	private String xinpian; //xinpian
	public String getXinpian() {
		return xinpian;
	}



	public void setXinpian(String xinpian) {
		this.xinpian = xinpian;
	}



	public String getYuqi() {
		return yuqi;
	}



	public void setYuqi(String yuqi) {
		this.yuqi = yuqi;
	}



	/**
	 * 两列的构造器
	 */
	
	public TableInfo(String orderNum, String orderMoney) {
		super();
		this.orderNum = orderNum;
		this.orderMoney = orderMoney;
	}
	

	
	public String getKid() {
		return kid;
	}



	public void setKid(String kid) {
		this.kid = kid;
	}



	/**
	 * 3列的构造器
	 * @param orderNum
	 * @param orderMoney
	 * @param orderStatus
	 */
	public TableInfo(String orderNum, String orderMoney, String orderStatus) {
		super();
		this.orderNum = orderNum;
		this.orderMoney = orderMoney;
		this.orderStatus = orderStatus;
	}


	/**
	 * 4列的构造器
	 * @param orderNum
	 * @param orderMoney
	 * @param orderStatus
	 * @param orderTime
	 */
	public TableInfo(String orderNum, String orderMoney, String orderStatus,String orderTime) {
		super();
		this.orderNum = orderNum;
		this.orderMoney = orderMoney;
		this.orderStatus = orderStatus;
		this.orderTime = orderTime;
		
	}
	public TableInfo(String orderNum, String orderMoney, String orderStatus,String orderTime, String kid) {
		super();
		this.orderNum = orderNum;
		this.orderMoney = orderMoney;
		this.orderStatus = orderStatus;
		this.orderTime = orderTime;
		this.kid = kid;
	}

	public TableInfo() {
		super();
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(String orderMoney) {
		this.orderMoney = orderMoney;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	@Override
	public String toString() {
		return "OrderInfo [orderNum=" + orderNum + ", orderMoney=" + orderMoney
				+ ", orderStatus=" + orderStatus + ", orderTime=" + orderTime
				+ "]";
	}
	
	
}
