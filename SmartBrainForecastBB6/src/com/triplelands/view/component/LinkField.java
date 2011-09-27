package com.triplelands.view.component;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;

public class LinkField extends Field {
	
	private static final int LINK_ID = 4;
	private int padding = 5;
	private int fontHeight;
	private int width, height;
	private Font font;
	private String text;
	
	public LinkField(String text) {
		super(Field.FOCUSABLE);
		this.text = text;
		font = Font.getDefault().derive(Font.UNDERLINED | Font.BOLD, 18);
		fontHeight = font.getHeight();
		this.width = font.getAdvance(text) + (2 * padding);
		this.height = fontHeight + (2 * padding);
	}
	
	protected void layout(int width, int height) {
		setExtent(this.width, this.height);
	}
	
	protected void onFocus(int direction) {
		super.onFocus(direction);
		invalidate();
	}
	
	protected void onUnfocus() {
		super.onUnfocus();
		invalidate();
	}

	protected void paint(Graphics g) {
		g.setFont(font);
		if(isFocus()){
			g.setColor(0x4888f0);
		} else {
			g.setColor(0xd44836);
		}
		g.drawText(text, padding * 2, (height / 2) - (fontHeight/2));
	}
	
	protected void drawFocus(Graphics graphics, boolean on) {	
	}

	protected boolean keyChar(char character, int status, int time) {
		if (character == Keypad.KEY_ENTER) {
			fieldChangeNotify(LINK_ID);
		}
		return true;
	}
	
	protected boolean navigationClick(int status, int time) {
		fieldChangeNotify(LINK_ID);
		return true;
	}

}
