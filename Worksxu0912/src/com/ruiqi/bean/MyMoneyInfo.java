package com.ruiqi.bean;

public class MyMoneyInfo {
	private String ordersn;
	private String pay_money;
	private String ctime;
	private String type_name;
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public MyMoneyInfo(String ordersn, String pay_money, String ctime) {
		super();
		this.ordersn = ordersn;
		this.pay_money = pay_money;
		this.ctime = ctime;
	}
	public MyMoneyInfo(String ordersn, String pay_money, String ctime,String type_name) {
		super();
		this.ordersn = ordersn;
		this.pay_money = pay_money;
		this.ctime = ctime;
		this.type_name = type_name;
	}
	public MyMoneyInfo() {
		super();
	}
	public String getOrdersn() {
		return ordersn;
	}
	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}
	public String getPay_money() {
		return pay_money;
	}
	public void setPay_money(String pay_money) {
		this.pay_money = pay_money;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	@Override
	public String toString() {
		return "MyMoneyInfo [ordersn=" + ordersn + ", pay_money=" + pay_money
				+ ", ctime=" + ctime + "]";
	}
	
	
}
