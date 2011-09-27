package com.triplelands.utils;

import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

public class SoundUtils {
	
	public SoundUtils() {
	}
	
	public void playSound(Player player) {
//		UiApplication.getUiApplication().invokeLater(new Runnable() {
//			public void run() {
//				Class cl = null;
//				try {
//					cl = Class.forName("com.triplelands.main.MainApp");
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				}
//				cl = PushHandlerReceiver.GetInstance().getClass();
//				if(cl != null){
//					InputStream is = cl.getResourceAsStream(file);
					try {
						System.out.println("INIT PLAYER");
//						synchronized (Application.getEventLock()) {
//							System.out.println("PREPARE CREATE PLAYER");
//							Player player = Manager.createPlayer(is, "audio/x-wav");
//							System.out.println("PLAYER CREATED");
//							player.realize();
//							System.out.println("PLAYER REALIZED");
//	//					    VolumeControl volume = (VolumeControl)player.getControl("VolumeControl");
//	//					    volume.setLevel(vol);
//						    player.prefetch();
//						    System.out.println("PLAYER PREFETCHED");
					    
					    	player.start();
					    	System.out.println("PLAYER JUST STARTED");
//					    }
					    System.out.println("PLAYER STARTED");
					} catch (MediaException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
//				}
//			}
//		});
	}
}
