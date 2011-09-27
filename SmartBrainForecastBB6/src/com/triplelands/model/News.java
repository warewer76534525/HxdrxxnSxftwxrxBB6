package com.triplelands.model;

public class News {
	private int id;
	private String title;
	private String time;
	private String content;
	
	public News(int id, String title, String time, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.time = time;
	}
	
	public News(int id, String title, String time){
		this.id = id;
		this.title = title;
		this.time = time;
		this.content = "";
	}
	
	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getTime() {
		return time;
	}
	
	public String getContent() {
		return content;
	}
	
}
