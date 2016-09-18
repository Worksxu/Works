package com.ruiqi.bean;

import java.io.Serializable;

public class Verson implements Serializable{
	/**
	 *  "version_address": "http://cztest.ruiqi100.com/statics/upload/worksxu.apk",
        "version_number": "v0.9.1",
        "version_code": "1",
        "version_content": "送气工app",
        "version_time": 0,
        "version_type": "shipperapp",
        "version_status": "1"
	 */
	private String version_address;
	private String version_number;
	private String version_code;
	private String version_content;
	private String version_time;
	private String version_type;
	private String version_status;
	public String getVersion_address() {
		return version_address;
	}
	public void setVersion_address(String version_address) {
		this.version_address = version_address;
	}
	public String getVersion_number() {
		return version_number;
	}
	public void setVersion_number(String version_number) {
		this.version_number = version_number;
	}
	public String getVersion_code() {
		return version_code;
	}
	public void setVersion_code(String version_code) {
		this.version_code = version_code;
	}
	public String getVersion_content() {
		return version_content;
	}
	public void setVersion_content(String version_content) {
		this.version_content = version_content;
	}
	public String getVersion_time() {
		return version_time;
	}
	public void setVersion_time(String version_time) {
		this.version_time = version_time;
	}
	public String getVersion_type() {
		return version_type;
	}
	public void setVersion_type(String version_type) {
		this.version_type = version_type;
	}
	public String getVersion_status() {
		return version_status;
	}
	public void setVersion_status(String version_status) {
		this.version_status = version_status;
	}
}
