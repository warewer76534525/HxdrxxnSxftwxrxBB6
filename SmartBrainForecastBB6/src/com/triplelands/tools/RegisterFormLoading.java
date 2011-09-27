package com.triplelands.tools;

import java.io.InputStream;

import net.rim.device.api.ui.UiApplication;

import com.triplelands.view.RegisterScreen;

public class RegisterFormLoading extends DataLoading {

	public RegisterFormLoading(String url) {
		super(url);
	}
	
	public void onReceiveResponseEvent(InputStream is) {
		synchronized (UiApplication.getEventLock()) {
			System.out.println("RECEIVED RESPONSE");
			StringBuffer sb = new StringBuffer();
			
			try {
				byte[] data = new byte[1024];
				int len = 0;
				while(-1 != (len = is.read(data))){
					sb.append(new String(data, 0, len));
				}
				String json = sb.toString();
				UiApplication.getUiApplication().pushScreen(new RegisterScreen("Register", json));
				close();
			} catch (Exception e) {
				System.out.println("ERROR RECEIVING: " + e.getMessage());
			}
		}
		super.onReceiveResponseEvent(is);
	}

}
