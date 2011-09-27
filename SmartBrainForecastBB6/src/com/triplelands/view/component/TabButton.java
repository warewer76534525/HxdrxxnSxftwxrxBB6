package com.triplelands.view.component;

import com.triplelands.utils.AppStatusManager;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;

public class TabButton extends Field {

	private String label;
	private AppStatusManager tabManager;
	private int padding = 10;
	private int fieldWidth;
	private int fieldHeight;
	private int backgroundColor;
	private int id;
	private final int DARK_GRAY = 0x202020;
		
	public TabButton(int id, String label) {
		super(Field.FOCUSABLE);
		this.id = id;
		this.label = label;
		Font font = Font.getDefault();
		fieldHeight = font.getHeight() + (2 * padding);
		fieldWidth = font.getAdvance(label) + (2 * padding);
		backgroundColor = DARK_GRAY;
		tabManager = AppStatusManager.GetInstance();
	}
	
	protected void layout(int width, int height) {
		setExtent(fieldWidth, fieldHeight);
	}
	
	protected void onFocus(int direction) {
		backgroundColor = Color.GREEN;
		invalidate();
	}
	
	protected void onUnfocus() {
		backgroundColor = DARK_GRAY;
		invalidate();
	}
	
	protected void paint(Graphics graphics) {
		graphics.setColor(backgroundColor);
        graphics.fillRoundRect(padding / 2, padding / 2, fieldWidth - padding, fieldHeight - padding, 3, 3);  
        graphics.setColor(Color.WHITE);
        graphics.drawText(label, padding, padding);
	}
	
	protected void drawFocus(Graphics graphics, boolean on) {	
	}

	protected boolean keyChar(char character, int status, int time) {
		if (character == Keypad.KEY_ENTER) {
			changeNotify(id);
		}
		return true;
	}
	
	protected boolean navigationClick(int status, int time) {
		changeNotify(id);
		return true;
	}
	
	private void changeNotify(int id){
		tabManager.setActiveId(id);
		fieldChangeNotify(id);
	}
}
