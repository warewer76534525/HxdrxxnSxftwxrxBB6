package com.triplelands.tools;

import java.io.IOException;
import java.io.InputStream;

import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.triplelands.utils.DataProcessor;

public class CheckForUpdateLoading extends DataLoading {

	public CheckForUpdateLoading(String url) {
		super(url);
	}
	
	public void onReceiveResponseEvent(InputStream is) {
		synchronized (UiApplication.getEventLock()) {
			System.out.println("Received Response");
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
			final String response = sb.toString();
			UiApplication.getUiApplication().invokeLater(new Runnable() {public void run() {
				DataProcessor processor = new DataProcessor();
				String status = processor.getResponseStatus(response);
				String message = processor.getResponseMessage(response);
				if(status.equals("1")){
					int ask = Dialog.ask(Dialog.D_YES_NO, "There is new update version. Do you want to update now?");
					if(ask == Dialog.YES){
						Browser.getDefaultSession().displayPage(message);
					}
					close();
				} else {
					Dialog.alert(message);
					close();
				}
			}});
		}
	}

}
