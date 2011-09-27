package com.triplelands.tools;

import java.io.IOException;
import java.io.InputStream;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.triplelands.utils.AppStatusManager;
import com.triplelands.utils.DataProcessor;

public class SignalNewsDataLoading extends DataLoading {

	private TabDataDownloadHandler handler;
	private int id;
	
	public SignalNewsDataLoading(int id, String url, TabDataDownloadHandler handler) {
		super(url);
		this.id = id;
		this.handler = handler;
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
			final String json = sb.toString();
			System.out.println("json: " + json);
			final DataProcessor processor = new DataProcessor();
			String status = processor.getResponseStatus(json);
			if(status != null && status.equals("0")){
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Dialog.alert(processor.getResponseMessage(json));
						AppStatusManager man = AppStatusManager.GetInstance();
						man.logoutAndCloseAllScreen();
					}
				});
			} else {
				if(id == AppStatusManager.SIGNAL_ID){
					handler.onDataCompleted(id, processor.getCategories(json));
				} else {
					handler.onDataCompleted(id, processor.getNewsList(json));
				}
			}
			
			close();
		}
//		close();
	}
}