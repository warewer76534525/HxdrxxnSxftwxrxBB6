package com.triplelands.view;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.triplelands.utils.AppStatusManager;
import com.triplelands.view.component.HeaderField;
import com.triplelands.view.component.TabButton;

public abstract class BaseScreen extends MainScreen implements FieldChangeListener {
	
	public BaseScreen(String title, boolean tabMode) {
		super();
//		AppStatusManager manager = AppStatusManager.GetInstance();
//		manager.addScreen(this);
		
		createBackground();
		if(tabMode){
			createHeaderWithNavigation();
		} else {
			setTitle(new HeaderField());
		}
		
		createMenu();
	}
	
	private void createHeaderWithNavigation(){
        HorizontalFieldManager hfm = new HorizontalFieldManager() {
        	protected boolean navigationMovement(int dx, int dy, int status,
        			int time) {
        		return super.navigationMovement(dx, dy, status, time);
        	}
        	
        	protected void onFocus(int direction) {
				if (direction == -1) {
					AppStatusManager man = AppStatusManager.GetInstance();
					if(man.getActiveId() == AppStatusManager.SIGNAL_ID){
						getField(0).setFocus();
					} else {
						getField(1).setFocus();
					}
				} else {
					super.onFocus(direction);
				}
        	}
        	
        	protected void fieldChangeNotify(int context) {
        		super.fieldChangeNotify(context);
        	}
        };
        
		TabButton tabSignal = new TabButton(AppStatusManager.SIGNAL_ID, "Signal");
		TabButton tabNews = new TabButton(AppStatusManager.NEWS_ID, "News");
		tabSignal.setChangeListener(this);
		tabNews.setChangeListener(this);
		hfm.add(tabSignal);
		hfm.add(tabNews);
		VerticalFieldManager vfm = new VerticalFieldManager();
		vfm.add(new HeaderField());
		vfm.add(hfm);
		setTitle(vfm);
	}

	protected void createMenu() {
//		addMenuItem(new MenuItem("Back", 0, 5){
//				public void run() {
//					close();
//				}
//		});
//		addMenuItem(new MenuItem("Exit", 0, 6){
//			public void run() {
//				System.exit(0);
//			}
//		});
	}
	
	protected boolean keyChar(char c, int status, int time) {
		if (c == Keypad.KEY_ESCAPE) {
			close();
		}
		return super.keyChar(c, status, time);
	}
	
	public boolean onClose() {
		return true;
	}
	
	protected boolean onSavePrompt() {
		return true;
	}

	public void createBackground() {
//		Background background = BackgroundFactory.createBitmapBackground(Bitmap.getBitmapResource("background.jpg"),Background.POSITION_X_CENTER , Background.POSITION_Y_CENTER , Background.REPEAT_SCALE_TO_FIT);
//		getMainManager().setBackground(background);
//		getMainManager().setBackground(BackgroundFactory.createLinearGradientBackground(0xd7e8f0, 0xd7e8f0, 0xffffff, 0xffffff));
	}

	public abstract void fieldChanged(Field field, int context);
}
