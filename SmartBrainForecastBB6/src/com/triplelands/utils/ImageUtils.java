package com.triplelands.utils;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.math.Fixed32;

public class ImageUtils {

	public static EncodedImage sizeImage(EncodedImage image, int width, int height) {
		EncodedImage result = null;

		int currentWidthFixed32 = Fixed32.toFP(image.getWidth());
		int currentHeightFixed32 = Fixed32.toFP(image.getHeight());

		int requiredWidthFixed32 = Fixed32.toFP(width);
		int requiredHeightFixed32 = Fixed32.toFP(height);

		int scaleXFixed32 = Fixed32.div(currentWidthFixed32,requiredWidthFixed32);
		int scaleYFixed32 = Fixed32.div(currentHeightFixed32,requiredHeightFixed32);

		result = image.scaleImage32(scaleXFixed32, scaleYFixed32);
		return result;
	}
	
	public static EncodedImage sizeWidth(EncodedImage image, int newWidth){
		int newHeight = calculateNewHeight(image.getWidth(), image.getHeight(), newWidth);
		return sizeImage(image, newWidth, newHeight);
	}
	
	public static int calculateNewHeight(int width, int height, int newWidth){
		return (newWidth * height) / width;
	}
	
	public static int calculateNewWidth(int width, int height, int newHeight){
		return (width * newHeight) / height;
	}
}
