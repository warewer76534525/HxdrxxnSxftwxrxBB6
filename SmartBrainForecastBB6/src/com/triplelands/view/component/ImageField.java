package com.triplelands.view.component;

import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.extension.container.ZoomScreen;

import com.triplelands.utils.ImageUtils;

public class ImageField extends Field {

	public static final int ALIGN_LEFT = 0;
	public static final int ALIGN_CENTER = 1;
	public static final int ALIGN_RIGHT = 2;
	private static final int MAX_AREA_WIDTH = Display.getWidth() - 10;
	
	private EncodedImage image, tempRealImg;
	private int align;
	private int areaWidth;
	private int areaHeight;
	
	private int index;
	
	public ImageField(String strImage, int align) {
		EncodedImage img = EncodedImage.getEncodedImageResource(strImage);
		initField(img, align);
		index = 0;
	}
	
	public ImageField(EncodedImage image, int align, int index){
		this.index = index;
		initField(image, align);
	}
	
	private void initField(EncodedImage image, int align){
		index = 0;
		tempRealImg = image;
		this.image = image;
		areaWidth = image.getWidth();
		areaHeight = image.getHeight();
		if(this.image.getWidth() >= Display.getWidth()){
			this.image = ImageUtils.sizeWidth(image, MAX_AREA_WIDTH);
			areaWidth = MAX_AREA_WIDTH;
			areaHeight = ImageUtils.calculateNewHeight(image.getWidth(), image.getHeight(), areaWidth);
		}
		this.align = align;
	}
	
	protected void layout(int width, int height) {
		setExtent(areaWidth + 10, areaHeight + 10);
	}
	
	public boolean isFocusable() {
		return true;
	}
	
	protected void paint(Graphics g) {
		switch (align) {
			case ALIGN_LEFT:
				g.drawImage(5, 5, areaWidth, areaHeight, image, 0, 0, 0);
				break;
			case ALIGN_CENTER:
				g.drawImage((Display.getWidth()/2) - (areaWidth / 2), 0, areaWidth, areaHeight, image, 0, 0, 0);
				break;
			case ALIGN_RIGHT:
				g.drawImage(Display.getWidth() - areaWidth, 0, areaWidth, areaHeight, image, 0, 0, 0);
				break;
		}
	}
	
	protected boolean navigationClick(int status, int time) {
		fieldChangeNotify(index);
		ZoomScreen zs = new ZoomScreen(tempRealImg);
		UiApplication.getUiApplication().pushScreen(zs);
		return true;
	}
}
