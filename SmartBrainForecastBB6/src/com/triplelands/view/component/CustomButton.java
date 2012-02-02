package com.triplelands.view.component;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;

public class CustomButton extends Field {

	private String label;
	private int padding = 20;
	private int fieldWidth;
	private int fieldHeight;
	private int backgroundColor;
	private int fontHeight;
	private boolean fillParentWidth;
	
	private int id = 1;
	
	public CustomButton(String label, int contextId) {
		super(Field.FOCUSABLE);
		initCustomButton(label, contextId, false);
	}
	
	public CustomButton(String label, int contextId, boolean fillParentWidth) {
		super(Field.FOCUSABLE);
		initCustomButton(label, contextId, fillParentWidth);
	}
	
	public void initCustomButton(String label, int contextId, boolean fillParentWidth){
		this.label = label;
		this.id = contextId;
		this.fillParentWidth = fillParentWidth;
		Font font = Font.getDefault();
		fontHeight = font.getHeight();
		fieldHeight = font.getHeight() + (padding);
		fieldWidth = font.getAdvance(label) + (2 * padding);
		backgroundColor = 0xd44836;
	}
	
	protected void layout(int width, int height) {
		if (fillParentWidth) {
			setExtent(Display.getWidth(), fieldHeight);
		} else {
			setExtent(fieldWidth, fieldHeight);
		}
	}
	
	protected void onFocus(int direction) {
		backgroundColor = 0x4888f0;
		invalidate();
	}
	
	protected void onUnfocus() {
		backgroundColor = 0xd44836;
		invalidate();
	}
	
	protected void paint(Graphics graphics) {
		if (fillParentWidth) {
			graphics.setColor(backgroundColor);
			graphics.fillRoundRect(padding / 2, padding / 2, Display.getWidth() - padding, fieldHeight, 3, 3);  
	        graphics.setColor(Color.WHITE);
	        graphics.drawText(label, (Display.getWidth() / 2) - (padding * 2), (fieldHeight / 2) - (fontHeight / 2) + (padding / 4));
		} else {
			graphics.setColor(backgroundColor);
			graphics.fillRoundRect(padding / 2, padding / 2, fieldWidth - padding, fieldHeight, 3, 3);  
	        graphics.setColor(Color.WHITE);
	        graphics.drawText(label, padding, (fieldHeight / 2) - (fontHeight / 2) + (padding / 4));
		}
	}
	
	protected void drawFocus(Graphics graphics, boolean on) {	
	}

	protected boolean keyChar(char character, int status, int time) {
		if (character == Keypad.KEY_ENTER) {
			fieldChangeNotify(id);
		}
		return true;
	}
	
	protected boolean navigationClick(int status, int time) {
		fieldChangeNotify(id);
		return true;
	}
}
