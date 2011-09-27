package com.triplelands.push;

public class AppLog {
	
	private static StringBuffer log;
	private static AppLog instance;
	
	private AppLog() {
		// TODO Auto-generated constructor stub
	}
	
	public static AppLog getInstance(){
		if(instance == null){
			instance = new AppLog();
			log = new StringBuffer();
		}
		return instance;
	}
	
	public void appendLog(String message) {
		log.append(message + "\n");
	}
	
	public String getLog(){
		return log.toString();
	}
}
