package com.triplelands.utils;

import net.rim.blackberry.api.homescreen.HomeScreen;
import net.rim.blackberry.api.messagelist.ApplicationIcon;
import net.rim.blackberry.api.messagelist.ApplicationIndicator;
import net.rim.blackberry.api.messagelist.ApplicationIndicatorRegistry;
import net.rim.blackberry.api.messagelist.ApplicationMessageFolder;
import net.rim.device.api.system.Alert;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.LED;

import com.triplelands.ahversion.idatafolderconf.DataMessage;
import com.triplelands.ahversion.idatafolderconf.DataMessageTempStore;

public class NotificationUtils {

//    private static final int VOLUME = 90;
	
	private static void updateIndicator(int value) {
		ApplicationIndicatorRegistry indicatorRegistry = ApplicationIndicatorRegistry.getInstance();
		ApplicationIndicator indicator = indicatorRegistry.getApplicationIndicator();
		if (indicator == null) {
			ApplicationIcon icon = new ApplicationIcon(EncodedImage.getEncodedImageResource("notification.png"));
			indicator = indicatorRegistry.register(icon, true, true);
		}

		indicator.setValue(value);
		if (value > 0) {
			indicator.setVisible(true);
			Alert.startVibrate(1000);
			LED.setConfiguration( 200, 2000, LED.BRIGHTNESS_100 );
            LED.setState( LED.STATE_BLINKING );
		} else {
			indicator.setVisible(false);
		}
	}
	
	public static void clearNotification(){
		try {
			LED.setState(LED.STATE_OFF);
			ApplicationIndicatorRegistry reg = ApplicationIndicatorRegistry.getInstance();
			reg.unregister();
        	DataMessageTempStore messageStore = DataMessageTempStore.getInstance();
        	messageStore.emptyMessages();
		} catch (Exception e) {
			System.out.println("Gagal unregister");
			System.out.println(e.getMessage());
		} catch (Throwable e) {
			System.out.println("Gagal unregister");
			System.out.println(e.getMessage());
		}
	}
	
	public static void notify(int id, String detail) {
		updateAppIcon("icon_asteriks.png");
		updateIndicator(id);
	        
        DataMessage message = new DataMessage();
        message.setSender("Hidreen Software");
        message.setSubject("New Signal: " + detail);
        message.setMessage("There is new signal data in: " + detail +  ". Click the notification title bar to view the signal.");
        message.setReceivedTime(System.currentTimeMillis());
        message.setPreviewPicture(null);

        //storing the message
        DataMessageTempStore messageStore = DataMessageTempStore.getInstance();
        synchronized(messageStore)
        {
            messageStore.addInboxMessage(message);
        }
        //here notify folder
        ApplicationMessageFolder folder = messageStore.getInboxFolder();
        folder.fireReset();
	}
	
	public static void updateAppIcon(String iconName){
		Bitmap bmp = Bitmap.getBitmapResource(iconName);
        HomeScreen.updateIcon(bmp);
	}
}
