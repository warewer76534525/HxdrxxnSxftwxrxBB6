package com.triplelands.main;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

import net.rim.blackberry.api.messagelist.ApplicationMessageFolderRegistry;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.EventLogger;
import net.rim.device.api.ui.UiApplication;

import com.triplelands.ahversion.idatafolderconf.DataMessage;
import com.triplelands.ahversion.idatafolderconf.IDataFolderConfManager;
import com.triplelands.datastore.DataStorer;
import com.triplelands.push.PushLibFactory;
import com.triplelands.push.PushMessageListener;
import com.triplelands.view.LoginScreen;
import com.triplelands.view.RootScreen;

public class MainApp extends UiApplication {
	//private static UiApplication application;
	
	private static DataStorer storer;
	private static boolean gui;
	
	public static void main(String[] args) {
		storer = new DataStorer();
		//application = PushLibFactory.getUiApplication();
		
		// Register application for event logging.
		EventLogger.register(0x9c805919833654d6L, "SMARTBRAIN");
		// Set minimum logging level.
		EventLogger.setMinimumLevel(EventLogger.INFORMATION);
		EventLogger.logEvent(0x9c805919833654d6L, "MASUK APLIKASI".getBytes(), EventLogger.INFORMATION);
		if(args != null && args.length > 0) {
			if(args.length == 1 && args[0].equals("startup")){
				gui = false;
				if (isSdkIsHigherVersion()) {
					IDataFolderConfManager manager = null;
					try {
						EventLogger.logEvent(0x9c805919833654d6L, "MASUK SDK HIGHER VERSION".getBytes(), EventLogger.INFORMATION);
						manager = (IDataFolderConfManager)Class.forName("com.triplelands.ahversion.datafolderconf.DataFolderConfManager").newInstance();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					ApplicationMessageFolderRegistry reg = ApplicationMessageFolderRegistry.getInstance();
			        if(reg.getApplicationFolder(DataMessage.INBOX_FOLDER_ID) == null) {
			            manager.init("Hidreen Software","notification.png");
			        }
//			        manager.enterEventDispatcher();	
				}
				if(storer.isRegisteredForPush() && storer.isLogedIn()){
					try {
						Thread.sleep(3000);
						PushMessageListener messageListener = PushLibFactory.getPushMessageListener();
						messageListener.startListening();
						new MainApp().enterEventDispatcher();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else if (args.length == 1 && args[0].equals("gui")){
				gui = true;
				EventLogger.logEvent(0x9c805919833654d6L, "ARGS = GUI".getBytes(), EventLogger.INFORMATION);
				System.out.println("GUI STARTED");
				new MainApp().enterEventDispatcher();
			}
        } else {
        	gui = true;
        	System.out.println("ARGS = 0");
        	EventLogger.logEvent(0x9c805919833654d6L, "ARGS = 0".getBytes(), EventLogger.INFORMATION);
        	new MainApp().enterEventDispatcher();
        }
	}
	
	private static boolean isSdkIsHigherVersion() {
		String ver = DeviceInfo.getSoftwareVersion();
		return Integer.parseInt(ver.substring(0, ver.indexOf('.'))) >= 6;
	}

	public MainApp() {
		if(gui){
			if(storer.isLogedIn()){
				pushScreen(new RootScreen("Home"));
			} else {
				pushScreen(new LoginScreen("Please Login"));
			}
		}
	}
	
	
	public static class AudioThread extends Thread
    {
        public void play()
        {
			start();
        }
        
        public void run()
        {
            Player aPlayer = null;
            try
            {
            	System.out.println("PLAYING AUDIO");
                InputStream input = getClass().getResourceAsStream("/alert2.wav");
                aPlayer = Manager.createPlayer(input,"audio/x-wav");
                aPlayer.start();
            }
            catch(MediaException me)
            {
            	me.printStackTrace();
            }
            catch(IOException ie)
            {
            	ie.printStackTrace();
            }
        }
    }
	
	public static void PlaySound()
    {
        AudioThread at = new AudioThread();
        at.play();
    }
}