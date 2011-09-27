package com.triplelands.model;

public class Comment {
	private int id;
	private String time;
	private String commentator;
	private String comment;
	
	public Comment(int id, String time, String commentator, String comment) {
		this.id = id;
		this.time = time;
		this.commentator = commentator;
		this.comment = comment;
	}
	
	public int getId() {
		return id;
	}
	
	public String getTime() {
		return time;
	}
	
	public String getCommentator() {
		return commentator;
	}
	
	public String getComment() {
		return comment;
	}
}
