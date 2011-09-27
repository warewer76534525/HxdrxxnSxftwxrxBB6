package com.triplelands.tools;

import java.io.InputStream;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.triplelands.utils.DataProcessor;

public class AppDataPosting extends DataPosting {

	private PostingHandler handler;
	
	public AppDataPosting(String url, String param, PostingHandler handler) {
		super(url, param);
		this.handler = handler;
	}
	
	public void onReceiveResponseEvent(InputStream is) {
		System.out.println("Post response received");
		synchronized (UiApplication.getEventLock()) {
			StringBuffer sb = new StringBuffer();
			try {
				byte[] data = new byte[1024];
				int len = 0;
				while(-1 != (len = is.read(data))){
					sb.append(new String(data, 0, len));
				}
				final String json = sb.toString().trim();
				System.out.println("RESPONSE POST: " + json);
				final DataProcessor processor = new DataProcessor();
				final String responseStr = (processor.getResponseMessage(json));
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						if(processor.getResponseStatus(json).equals("1")){
							handler.onPostingSuccess(json);
						}
						Dialog.alert(responseStr);
						close();
					}
				});
			} catch (Exception e) {
				System.out.println("ERROR RECEIVING: " + e.getMessage());
			}
		}
	}
}
