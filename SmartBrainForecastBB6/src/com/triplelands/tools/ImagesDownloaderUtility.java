package com.triplelands.tools;

import java.util.Vector;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.UiApplication;

public class ImagesDownloaderUtility implements ImageDownloaderUtilityHandler {
	
	private String urls[];
	private ImageLoadingHandler handler;
	private Vector downloadedImages;
	private int downloaded;
	
	public ImagesDownloaderUtility(String urls[], ImageLoadingHandler handler) {
		this.urls = urls;
		this.handler = handler;
		downloadedImages = new Vector();
		downloaded = 0;	
	}
	
	public void start(){
		System.out.println("Starting download utility");
		startDownloads(urls[downloaded]);
	}

	private void startDownloads(final String url) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				UiApplication.getUiApplication().pushScreen(new ImageLoading(url, ImagesDownloaderUtility.this));
			}
		});
	}

	public void onReceivedImage(EncodedImage image) {
		downloaded += 1;
		downloadedImages.addElement(image);
		
		//finish download all images
		if(downloaded == urls.length)
			handler.onReceivedImage(downloadedImages);
		else
			startDownloads(urls[downloaded]);
	}
}
