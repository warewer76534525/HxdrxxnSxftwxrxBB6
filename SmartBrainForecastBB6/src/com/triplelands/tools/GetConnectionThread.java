package com.triplelands.tools;


public class GetConnectionThread extends Thread {

	private InternetConnection internetConnection;
	private String url;

	public GetConnectionThread(String url, InternetConnectionListener listener) {
		internetConnection = new InternetConnection(listener);
		this.url = url;
	}
	
	public void run() {
		internetConnection.setAndAccessURL(url);
		super.run();
	}
}
