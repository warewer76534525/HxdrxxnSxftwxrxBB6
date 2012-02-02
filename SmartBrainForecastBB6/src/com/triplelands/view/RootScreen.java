package com.triplelands.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import net.rim.device.api.system.Application;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.triplelands.datastore.DataStorer;
import com.triplelands.model.Category;
import com.triplelands.model.News;
import com.triplelands.model.Signal;
import com.triplelands.push.AppLog;
import com.triplelands.push.BpasProtocol;
import com.triplelands.push.PushLibFactory;
import com.triplelands.push.PushMessageListener;
import com.triplelands.tools.DetailLoading;
import com.triplelands.tools.InternetConnection;
import com.triplelands.tools.InternetConnectionListener;
import com.triplelands.tools.LogoutLoading;
import com.triplelands.tools.SignalNewsDataLoading;
import com.triplelands.tools.TabDataDownloadHandler;
import com.triplelands.utils.AppStatusManager;
import com.triplelands.utils.Constants;
import com.triplelands.utils.NotificationUtils;
import com.triplelands.utils.StringUtils;
import com.triplelands.view.component.CustomLabel;
import com.triplelands.view.component.NewsItemField;
import com.triplelands.view.component.SignalListItemField;
import com.triplelands.view.component.TabButton;

public class RootScreen extends BaseScreen implements TabDataDownloadHandler, InternetConnectionListener {

	private VerticalFieldManager vfManager;
	private Vector dataSignalCategories;
	private Vector dataNews;
	private AppStatusManager statusManager;
	private boolean exposed;
	private DataStorer storer;
		
	public RootScreen(String title) {
		super(title, true);
		
		storer = new DataStorer();
		statusManager = AppStatusManager.GetInstance();
		dataSignalCategories = new Vector();
		dataNews = new Vector();
		
		initMenu();
		
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				doExpose();
			}
		});
	}
	
	private void initSignalCategoriesList(Vector data){
		System.out.println("Init signal category list");
		deleteAll();
		vfManager = null;
		vfManager = new VerticalFieldManager();
		
		populateExpiredLabel();
		populateLicenseLabel();
		populateFlashNewsLabel();
		
		for (int i = 0; i < data.size(); i++) {
			Category cat = (Category)data.elementAt(i);
			Vector signals = cat.getSignals();
			CustomLabel lbl = new CustomLabel(cat.getName());
			vfManager.add(lbl);
			for (int j = 0; j < signals.size(); j++) {
				SignalListItemField item = new SignalListItemField((Signal)signals.elementAt(j));
				item.setChangeListener(this);
				vfManager.add(item);
			}
		}
		
		add(vfManager);
	}
	
	private void populateExpiredLabel(){
		LabelField lblExpired = new LabelField(" Account expired on: " + storer.getData("expired"));
		lblExpired.setFont(Font.getDefault().derive(Font.PLAIN, 16));
		vfManager.add(lblExpired);
	}
	
	private void populateLicenseLabel(){
		String license = storer.getData("license");
		LabelField lblLicense = new LabelField();
		if (license.equals("")) {
			lblLicense.setText(" License: Please logout and login again to generate your license.");
		} else{
			lblLicense.setText(" License: " + storer.getData("license"));
		}
		
		lblLicense.setFont(Font.getDefault().derive(Font.PLAIN, 16));
		vfManager.add(lblLicense);
	}
	
	private void populateFlashNewsLabel(){
		String flashNews = storer.getData("flashNews");
		LabelField lblFlashNews = new LabelField();
		lblFlashNews.setFont(Font.getDefault().derive(Font.PLAIN, 16));
		if (!flashNews.equals("")) {
			lblFlashNews.setText(storer.getData("flashNews"));
			vfManager.add(lblFlashNews);
		}
	}
	
	private void initNewsList(Vector data){
		deleteAll();
		vfManager = null;
		vfManager = new VerticalFieldManager();
		for (int i = 0; i < data.size(); i++) {
			NewsItemField item = new NewsItemField((News)data.elementAt(i));
			item.setChangeListener(this);
			vfManager.add(item);
		}
		add(vfManager);
	}

	public void fieldChanged(Field field, int context) {
		invalidate();
		if(field instanceof TabButton){
			if(context == AppStatusManager.SIGNAL_ID){
				initSignalCategoriesList(dataSignalCategories);
			}
			if(context == AppStatusManager.NEWS_ID){
				if(dataNews.size() > 0){
					initNewsList(dataNews);
				} else {
					UiApplication.getUiApplication().pushScreen(new SignalNewsDataLoading(AppStatusManager.NEWS_ID, Constants.URL_NEWS, RootScreen.this));
				}
			}
		}
		if(field instanceof SignalListItemField){
			UiApplication.getUiApplication().pushScreen(new DetailLoading(AppStatusManager.SIGNAL_ID, Constants.URL_SIGNAL_DETAIL + "/" + context));
		}
		if(field instanceof NewsItemField){
			UiApplication.getUiApplication().pushScreen(new DetailLoading(AppStatusManager.NEWS_ID, Constants.URL_NEWS_DETAIL + "/" + context));
		}
	}

	public void onDataCompleted(final int id, final Vector data) {
		System.out.println("Data completed");
		if(id == AppStatusManager.SIGNAL_ID){
			dataSignalCategories = data;
			initSignalCategoriesList(data);
		} else {
			dataNews = data;
			initNewsList(data);
		}
	}
	
	private void initMenu(){
		addMenuItem(new MenuItem("Refresh Data", 0, 5){
			public void run() {
				refreshData();
			}
		});
//		addMenuItem(new MenuItem("Check for update", 0, 6){
//			public void run() {
//				checkForUpdate();
//			}
//		});
		addMenuItem(new MenuItem("Logout", 0, 7){
			public void run() {
				int ask = Dialog.ask(Dialog.D_YES_NO, "Are you sure to logout? You will not be notified until you re-login.");
				if(ask == Dialog.YES){
					UiApplication.getUiApplication().pushScreen(new LogoutLoading(Constants.URL_LOGOUT));
				}
			}
		});
	}
	
//	private void checkForUpdate() {
//		String urlUpdate = Constants.URL_CHECK_UPDATE + "bb/" + StringUtils.getOSVersion() + "/" + ApplicationDescriptor.currentApplicationDescriptor().getVersion();
//		UiApplication.getUiApplication().pushScreen(new CheckForUpdateLoading(urlUpdate));
//	}
	
	private void refreshData(){
		int id = statusManager.getActiveId();
		if(id == AppStatusManager.SIGNAL_ID)
			UiApplication.getUiApplication().pushScreen(new SignalNewsDataLoading(AppStatusManager.SIGNAL_ID, Constants.URL_SIGNALS, RootScreen.this));
		else
			UiApplication.getUiApplication().pushScreen(new SignalNewsDataLoading(AppStatusManager.NEWS_ID, Constants.URL_NEWS, RootScreen.this));
	}
	
	//======================== Push Implementation (Register and Unregister) ===============
	
	protected boolean keyChar(char c, int status, int time) {
		if (c == Keypad.KEY_ESCAPE) {
			onClose();
		}
		return true;
	}
	
	protected void onExposed() {
		doExpose();
		super.onExposed();
	}
	
	private void doExpose(){
		NotificationUtils.updateAppIcon("icon.png");
		if(!exposed) {
			NotificationUtils.clearNotification();
			UiApplication.getUiApplication().pushScreen(new SignalNewsDataLoading(AppStatusManager.SIGNAL_ID, Constants.URL_SIGNALS, RootScreen.this));
			if(!storer.isRegisteredForPush()){
				registerToPushServer();
			}
			exposed = true;
		}
	}
	
	public boolean onClose() {
		exposed = false;
		storer.emptyUnreadSignals();
        Application.getApplication().requestBackground();
        return false;
	}
	
	private void registerToPushServer() {
		System.out.println("REGISTERING PUSH: ");
		Thread t = new Thread(new Runnable() {
			public void run() {
				InternetConnection con = new InternetConnection(RootScreen.this);
				con.setAndAccessURL(Constants.URL_PUSH_REGISTER + "&osversion=" + StringUtils.getOSVersion());
			}
		});
		t.start();
	}

	public void onReceiveResponseEvent(InputStream is) {
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
		final String key = StringUtils.split(response, "=")[0];
		final String value = StringUtils.split(response, "=")[1];
		
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				System.out.println("RESPONSE REGISTERING PUSH: " + response);
				if(key.equals("param")){
					Thread t = new Thread(new Runnable() {
						public void run() {
							InternetConnection con = new InternetConnection(RootScreen.this);
							con.setAndAccessURL(Constants.URL_PUSH_REGISTER  + "&osversion=" + StringUtils.getOSVersion() + "&param=" + value);
						}
					});
					t.start();
				} else {
					if(value.equals("200") || value.equals("10003")){
						BpasProtocol bpasProtocol = PushLibFactory.getBpasProtocol();
				        try {
							bpasProtocol.register();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						AppLog.getInstance().appendLog("Registered, preparing for listening");
						PushMessageListener messageListener = PushLibFactory.getPushMessageListener();
						messageListener.startListening();
						storer.setRegisteredForPush(true);
						System.out.println("LISTENING RECEIVER SET");
					}
				}
			}
		});
	}

	public void onErrorOccurEvent(Exception e) {		
	}

	public void onCancelEvent() {		
	}

	public void onStartEvent(long length, String type) {		
	}

	public void onNotFound() {		
	}
}
