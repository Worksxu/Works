package com.ruiqi.bean;
/**
 * 个人信息
 * @author Administrator
 *
 */
public class Pensoral {
	private String title; //信息名称
	
	private String content; //信息内容
	private int i;// 信息图片

	@Override
	public String toString() {
		return "Pensoral [title=" + title + ", content=" + content + "]";
	}

	public String getTitle() {
		return title;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Pensoral() {
		super();
	}

	public Pensoral(String title, String content) {
		super();
		this.title = title;
		this.content = content;
	}
	public Pensoral(String title, int i) {
		super();
		this.title = title;
		this.i = i;
	}
	
}
