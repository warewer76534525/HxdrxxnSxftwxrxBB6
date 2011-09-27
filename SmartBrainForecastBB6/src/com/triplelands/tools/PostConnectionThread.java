package com.triplelands.tools;


public class PostConnectionThread extends Thread {

	private InternetConnection internetConnection;
	private String url, param;

	public PostConnectionThread(String url, String param, InternetConnectionListener listener) {
		internetConnection = new InternetConnection(listener);
		this.url = url;
		this.param = param;
	}
	
	public void run() {
		internetConnection.uploadParameter(url, param);
		super.run();
	}
}
