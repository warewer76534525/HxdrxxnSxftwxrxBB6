package com.triplelands.view.component;

import com.triplelands.utils.ImageUtils;

import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

/**
 * @author jaka.lesmana
 *
 */
public class HeaderField extends Field {

	private int layoutHeight;
	private int layoutWidth;
	private EncodedImage imgHeader, imgLogo;

	public HeaderField() {
		super(USE_ALL_WIDTH);
		layoutWidth = Display.getWidth();
		layoutHeight = 25;
		imgHeader = ImageUtils.sizeImage(EncodedImage.getEncodedImageResource("header.jpg"), layoutWidth, layoutHeight);
		imgLogo = EncodedImage.getEncodedImageResource("logo.png");
	}
	
	protected void layout(int width, int height) {
		setExtent(layoutWidth, layoutHeight);
	}

	protected void paint(Graphics g) {
		g.drawImage(0, 0, layoutWidth, layoutHeight, imgHeader, 0, 0, 0);
		g.drawImage(10, (layoutHeight / 2) - (imgLogo.getHeight() / 2), imgLogo.getWidth(), imgLogo.getHeight(), imgLogo, 0, 0, 0);
		
		g.setColor(Color.WHITE);
		g.drawLine(0, layoutHeight - 1, layoutWidth, layoutHeight - 1);
	}
	
}
