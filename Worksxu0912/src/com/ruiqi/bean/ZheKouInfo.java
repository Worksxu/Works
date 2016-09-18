package com.ruiqi.bean;

import java.io.Serializable;
import java.util.List;


public class ZheKouInfo extends YouHuiZheKouInfo implements Serializable {
	
	private List<Data> data;

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}
	public class Data implements Serializable{
		String good_id;
		String good_name;
		String good_kind;
		String good_price;
		String good_type;
		String good_num;
		public String getGood_id() {
			return good_id;
		}
		public void setGood_id(String good_id) {
			this.good_id = good_id;
		}
		public String getGood_name() {
			return good_name;
		}
		public void setGood_name(String good_name) {
			this.good_name = good_name;
		}
		public String getGood_kind() {
			return good_kind;
		}
		public void setGood_kind(String good_kind) {
			this.good_kind = good_kind;
		}
		public String getGood_price() {
			return good_price;
		}
		public void setGood_price(String good_price) {
			this.good_price = good_price;
		}
		public String getGood_type() {
			return good_type;
		}
		public void setGood_type(String good_type) {
			this.good_type = good_type;
		}
		public String getGood_num() {
			return good_num;
		}
		public void setGood_num(String good_num) {
			this.good_num = good_num;
		}
	}

}
