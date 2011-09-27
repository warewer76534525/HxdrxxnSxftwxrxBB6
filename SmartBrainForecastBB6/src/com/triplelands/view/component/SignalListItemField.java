package com.triplelands.view.component;

import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;

import com.triplelands.datastore.DataStorer;
import com.triplelands.model.Signal;
import com.triplelands.utils.StringUtils;

public class SignalListItemField extends Field {
	private Signal signal;
	private int fieldHeight;
	private int fieldWidth;
	private EncodedImage imageUp;
	private EncodedImage imageDown;
	private EncodedImage asteriks;
//	private EncodedImage selectedBg;
//	private EncodedImage imageComment;
	private Font boldFont;
	private Font plainFont;
	
	public SignalListItemField(Signal signal) { 
		this.signal = signal;
		fieldHeight = Font.getDefault().derive(Font.BOLD, 16).getHeight() * 2 + 15;
		fieldWidth = Display.getWidth();
		imageUp = EncodedImage.getEncodedImageResource("green.png");
		imageDown = EncodedImage.getEncodedImageResource("red.png");
		asteriks =  EncodedImage.getEncodedImageResource("asteriks_small.png");
//		imageComment = EncodedImage.getEncodedImageResource("comments.png");
//		selectedBg = ImageUtils.sizeImage(EncodedImage.getEncodedImageResource("selected_bg.jpg"), fieldWidth, fieldHeight);
		boldFont = Font.getDefault().derive(Font.BOLD, 20);
		plainFont = Font.getDefault().derive(Font.PLAIN, 14);
	}
	
	protected void layout(int width, int height) {
		setExtent(fieldWidth , fieldHeight);
	}

	protected void paint(Graphics g) {
		
		g.setColor(Color.BLACK);
		if (isFocus()) {
			//g.drawImage(0, 0, fieldWidth, fieldHeight, selectedBg, 0, 0, 0);
			g.setColor(Color.WHITE);
		}
		
		//if new, add asteriks image
		if (isNew()) {
			g.drawImage(3, 3, asteriks.getWidth(), asteriks.getHeight(), asteriks, 0, 0, 0);
		}
		
		int titleX = 15;
		int titleY = (fieldHeight / 2) - (boldFont.getHeight() / 2);
		
		//set symbol text
		g.setFont(boldFont);
		g.drawText(signal.getSymbol(), titleX, titleY);
		
		//comment
//		int imageCommentX = titleX + plainFont.getAdvance(signal.getTime()) + 10;
//		g.drawImage(imageCommentX, titleY + imageSubtitleY - 7,
//				imageComment.getWidth(), imageComment.getHeight(), imageComment, 0, 0, 0);
//		g.drawText("" + signal.getComment(), imageCommentX + imageComment.getWidth(), imageSubtitleY);		
		
		//draw up/down image
		if(signal.isUp()){
			g.drawImage(fieldWidth - imageUp.getWidth() - 5, (fieldHeight / 2) - (imageUp.getHeight() / 2), imageUp.getWidth(), imageUp.getHeight(), imageUp, 0, 0, 0);
		} else {
			g.drawImage(fieldWidth - imageDown.getWidth() - 5, (fieldHeight / 2) - (imageDown.getHeight() / 2), imageDown.getWidth(), imageDown.getHeight(), imageDown, 0, 0, 0);
		}
		
		//set rate and percentage
		g.setColor(Color.WHITE);
		g.setFont(Font.getDefault().derive(Font.BOLD, 14));
		int probX = fieldWidth - imageUp.getWidth() + 10;
		g.drawText((signal.isUp()) ? "UP" : "DOWN", probX, 3 + (imageUp.getHeight()/2) - (plainFont.getHeight()));
		g.drawText(signal.getProbability() + "%", probX, (imageUp.getHeight()/2) + (plainFont.getHeight()/2) - 3);
		
		//draw divider line
		g.setColor(0x808080);
		g.drawLine(0, 0, fieldWidth , 0);
//		g.drawLine(0, fieldHeight - 1, fieldWidth , fieldHeight - 1);
	}
	
	private boolean isNew(){
		DataStorer storer = new DataStorer();
		String ids = storer.getUnreadSignalIds();
		String[] arrId = StringUtils.explode(';', ids);
		for (int i = 0; i < arrId.length; i++) {
			if(arrId[i].equals("" + signal.getId())) return true;
		}
		return false;
	}
	
	protected boolean keyChar(char character, int status, int time) {
		if (character == Keypad.KEY_ENTER) {
			fieldChangeNotify(signal.getId());
		}
		return true;
	}
	
	protected boolean navigationClick(int status, int time) {
		fieldChangeNotify(signal.getId());
		return true;
	}
	
	protected void onFocus(int direction) {
		super.onFocus(direction);
		invalidate();
	}
	
	protected void onUnfocus() {
		super.onUnfocus();
		invalidate();
	}
	
	public boolean isFocusable() {
		return true;
	}
	
	protected void drawFocus(Graphics graphics, boolean on) {
		super.drawFocus(graphics, on);
	}
}
