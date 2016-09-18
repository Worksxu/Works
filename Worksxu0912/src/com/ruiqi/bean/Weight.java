package com.ruiqi.bean;

import java.io.Serializable;

/**
 * 重瓶的 芯片号，类型。以及状态
 * @author Administrator
 *
 */
public class Weight implements Serializable{
	private String xinpian;
	private String yj;
	public String getYj() {
		return yj;
	}

	public void setYj(String yj) {
		this.yj = yj;
	}

	private String type;
	
	private String status;
	private String type_name;
	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getXuliehao() {
		return xuliehao;
	}

	public void setXuliehao(String xuliehao) {
		this.xuliehao = xuliehao;
	}

	private String xuliehao;

	@Override
	public String toString() {
		return "Weight [xinpian=" + xinpian + ", type=" + type + ", status="
				+ status + "]";
	}

	public String getXinpian() {
		return xinpian;
	}

	public void setXinpian(String xinpian) {
		this.xinpian = xinpian;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Weight(String xinpian, String type, String status,String yj) {
		super();
		this.xinpian = xinpian;
		this.type = type;
		this.status = status;
		this.yj = yj;// 代表瓶的id
	}
	public Weight(String xinpian, String type, String status) {
		super();
		this.xinpian = xinpian;
		this.type = type;
		this.status = status;
	}

	public Weight() {
		super();
	}
}
