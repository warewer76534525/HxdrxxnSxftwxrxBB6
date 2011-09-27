package com.triplelands.utils;

import com.triplelands.datastore.DataStorer;
import com.triplelands.push.PushLibFactory;


public class AppStatusManager {
	public  static final int SIGNAL_ID = 0;
	public  static final int NEWS_ID = 1;
	private static int status = SIGNAL_ID;
	private static AppStatusManager instance;
//	private static Vector screens;
	
	private AppStatusManager(){
	}
	
	public static AppStatusManager GetInstance(){
		if(instance == null){
			instance = new AppStatusManager();
//			screens = new Vector();
		}
		return instance;
	}
	
	public int getActiveId(){
		return status;
	}
	
	public void setActiveId(int stat){
		status = stat;
	}
	
	public void clearResources(){
		instance = null;
	}
	
//	public void addScreen(Screen screen){
//		screens.addElement(screen);
//	}
	
	public void logoutAndCloseAllScreen(){
		DataStorer storer = new DataStorer();
		storer.logout();
		clearResources();
		PushLibFactory.getPushMessageListener().stopListening();
		System.exit(0);
//		for (int i = 0; i < screens.size(); i++) {
//			Screen scr = (Screen)screens.elementAt(i);
//			if(scr != null){
//				try {
//					scr.close();
//				} catch (Exception e) {
//				} finally {					
//				}
//			}
//		}
	}
}
