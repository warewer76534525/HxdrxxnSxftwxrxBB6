package com.triplelands.view;

import net.rim.device.api.browser.field.ContentReadEvent;
import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.browser.field2.BrowserFieldConfig;
import net.rim.device.api.browser.field2.BrowserFieldListener;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.LabelField;

import org.w3c.dom.Document;

public class GraphViewerScreen extends BaseScreen {

	private BrowserField browserField;
	private LabelField lblProgress;

	public GraphViewerScreen(String title, String graphUrl, boolean tabMode) {
		super(title, tabMode);
		lblProgress = new LabelField("Loading graph...");
		BrowserFieldConfig myBrowserFieldConfig = new BrowserFieldConfig();
		myBrowserFieldConfig.setProperty(BrowserFieldConfig.NAVIGATION_MODE,
				BrowserFieldConfig.NAVIGATION_MODE_POINTER);
		browserField = new BrowserField(myBrowserFieldConfig);
		
		add(lblProgress);
		add(browserField);
		browserField.addListener(new MyBrowserFieldListener());
		browserField.requestContent(graphUrl);
	}

	private class MyBrowserFieldListener extends BrowserFieldListener {

		public void downloadProgress(BrowserField browserField,
				ContentReadEvent event) throws Exception {
			super.downloadProgress(browserField, event);
		}
		
		
		
		public void documentLoaded(BrowserField browserField, Document document) throws Exception {
			super.documentLoaded(browserField, document);
			try {
				int cw = browserField.getContentWidth();
				int sw = Display.getWidth();
				System.out.println("loaded content width  = " + cw);

				// scaling
//				if (cw > sw) {
//					float scalFactor = ((float) sw) / cw;
//					browserField.setZoomScale(scalFactor);
//					browserField.invalidate();
//					doPaint();
//				}
				delete(lblProgress);
				invalidate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void fieldChanged(Field field, int context) {
	}

}
