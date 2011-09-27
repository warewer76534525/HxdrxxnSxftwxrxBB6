package com.triplelands.tools;

import java.io.IOException;
import java.io.InputStream;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.triplelands.datastore.DataStorer;
import com.triplelands.push.BpasProtocol;
import com.triplelands.push.PushLibFactory;
import com.triplelands.push.PushMessageListener;
import com.triplelands.utils.Constants;
import com.triplelands.utils.DataProcessor;
import com.triplelands.utils.StringUtils;

public class LogoutLoading extends DataLoading {
	private boolean unRegTx;
	private DataStorer storer;
	
	public LogoutLoading(String url) {
		super(url);
		storer = new DataStorer();
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
			final String response = sb.toString();
			
			if(unRegTx){
				final String key = StringUtils.split(response, "=")[0];
				final String value = StringUtils.split(response, "=")[1];
				
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						System.out.println("response: " + response);
						if(key.equals("param")){
							Thread t = new Thread(new Runnable() {
								public void run() {
									InternetConnection con = new InternetConnection(LogoutLoading.this);
									con.setAndAccessURL(Constants.URL_PUSH_UNREGISTER  + "&osversion=" + StringUtils.getOSVersion() + "&param=" + value);
								}
							});
							t.start();
						} else {
							if(value.equals("200") || value.equals("10004")){
								BpasProtocol bpasProtocol = PushLibFactory.getBpasProtocol();
						        try {
									bpasProtocol.unregister();
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								PushMessageListener messageListener = PushLibFactory.getPushMessageListener();
								messageListener.stopListening();
								storer.setRegisteredForPush(false);
								System.exit(0);
							} else {
								System.out.println("LOGOUT FAILED:" + value);
								Dialog.alert("Logout failed. Try again later.");
								close();
							}
						}
					}
				});
			} else {
				final DataProcessor processor = new DataProcessor();
				if(processor.getResponseStatus(response).equals("1")){
					storer.logout();
					if(storer.isRegisteredForPush()){
						//here unregistering push
						Thread t = new Thread(new Runnable() {
							public void run() {
								InternetConnection con = new InternetConnection(LogoutLoading.this);
								con.setAndAccessURL(Constants.URL_PUSH_UNREGISTER  + "&osversion=" + StringUtils.getOSVersion());	
							}
						});
						t.start();
						unRegTx = true;
					} else {
						System.exit(0);
					}
				} else {
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Dialog.alert(processor.getResponseMessage(response));
							close();
						}
					});
				}
			}		
		}
	}
}