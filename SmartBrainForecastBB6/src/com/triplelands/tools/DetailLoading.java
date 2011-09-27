package com.triplelands.tools;

import java.io.IOException;
import java.io.InputStream;

import net.rim.device.api.ui.UiApplication;

import com.triplelands.utils.AppStatusManager;
import com.triplelands.view.NewsDetailScreen;
import com.triplelands.view.SignalDetailScreen;

public class DetailLoading extends DataLoading {

	private int id;
	
	public DetailLoading(int id, String url) {
		super(url);
		this.id = id;
	}

	public void onReceiveResponseEvent(InputStream is) {
		synchronized (UiApplication.getEventLock()) {
			System.out.println("RECEIVED RESPONSE");
			StringBuffer sb = new StringBuffer();
			byte[] data = new byte[1024];
			int len = 0;
			try {
				while(-1 != (len = is.read(data))){
					sb.append(new String(data, 0, len));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			String json = sb.toString();
			System.out.println("json: " + json);
			if(id == AppStatusManager.SIGNAL_ID){
				UiApplication.getUiApplication().pushScreen(new SignalDetailScreen(json));
			} else {
				UiApplication.getUiApplication().pushScreen(new NewsDetailScreen(json));
			}
			close();
		}
	}
}