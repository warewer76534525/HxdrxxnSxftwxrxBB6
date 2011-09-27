package com.triplelands.view.component;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

public class SpaceField extends Field {

	private int height;
	
	public SpaceField(int height) {
		super(Field.NON_FOCUSABLE);
		this.height = height;
	}
	
	protected void layout(int width, int height) {
		setExtent(Display.getWidth(), this.height);
	}

	protected void paint(Graphics graphics) {

	}

}
