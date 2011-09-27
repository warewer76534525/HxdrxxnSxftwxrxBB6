package com.triplelands.view.component;

import com.triplelands.utils.ImageUtils;

import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;

/**
 * @author jaka.lesmana
 *
 */
public class CustomLabel extends Field {

	private int layoutHeight;
	private int layoutWidth;
	private EncodedImage bgLabel, bgLabelFocus;
	private String text;
	private Font font;

	public CustomLabel(String text) {
		super(USE_ALL_WIDTH);
		this.text = text;
		font = Font.getDefault().derive(Font.BOLD, 15);
		layoutWidth = Display.getWidth();
		layoutHeight = 25;
		bgLabel = ImageUtils.sizeImage(EncodedImage.getEncodedImageResource("bglabel.jpg"), layoutWidth, layoutHeight);
		bgLabelFocus = ImageUtils.sizeImage(EncodedImage.getEncodedImageResource("bglabelfocus.jpg"), layoutWidth, layoutHeight);
	}
	
	protected void layout(int width, int height) {
		setExtent(layoutWidth, layoutHeight);
	}

	protected void paint(Graphics g) {
		if(isFocus()) g.drawImage(0, 0, layoutWidth, layoutHeight, bgLabelFocus, 0, 0, 0);
		else g.drawImage(0, 0, layoutWidth, layoutHeight, bgLabel, 0, 0, 0);
		
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawText(text, 5, (layoutHeight / 2) - (font.getHeight() / 2));
	}
	
	public boolean isFocusable() {
		return true;
	}
	
	protected void drawFocus(Graphics graphics, boolean on) {
	}
	
}
