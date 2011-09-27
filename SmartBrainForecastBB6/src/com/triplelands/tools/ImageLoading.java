package com.triplelands.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.UiApplication;

public class ImageLoading extends DataLoading {

	private ImageDownloaderUtilityHandler handler;
	private byte[] incomingData;
	
	public ImageLoading(String url, ImageDownloaderUtilityHandler handler) {
		super(url);
		this.handler = handler;
	}
	
	public void onReceiveResponseEvent(InputStream is) {
		synchronized (UiApplication.getEventLock()) {
			System.out.println("DAPAT RESPON DATA IMAGE");
			int ch = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				while ((ch = is.read(incomingData)) != -1){
					baos.write(incomingData, 0, ch);
				}
				
				EncodedImage image = EncodedImage.createEncodedImage(baos.toByteArray(), 0, baos.size());
				System.out.println("DAPAT IMAGE!");
				handler.onReceivedImage(image);
			} catch (IOException e) {
				System.out.println("ERROR RESPON DATA IMAGE: " + e.getMessage());
			}
			close();
		}
	}
	
	public void onStartEvent(long length, String type) {
		int lg = (int) length;
		incomingData = new byte[lg];
		super.onStartEvent(length, type);
	}

}
