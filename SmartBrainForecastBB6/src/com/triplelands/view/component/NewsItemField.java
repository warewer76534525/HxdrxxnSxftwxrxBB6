package com.triplelands.view.component;

import java.util.Timer;
import java.util.TimerTask;

import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;

import com.triplelands.model.News;

public class NewsItemField extends Field {
	private News news;
	private int fieldHeight;
	private int fieldWidth;
	private EncodedImage imageNews;
	private Font boldFont;
	private Font plainFont;
	
	private Timer scrollTimer;
	private TimerTask scrollTimerTask;
	private int pos;
	
	public NewsItemField(News news) { 
		this.news = news;
		fieldHeight = Font.getDefault().derive(Font.BOLD, 16).getHeight() * 2 + 15;
		fieldWidth = Display.getWidth();
		imageNews= EncodedImage.getEncodedImageResource("news.png");
//		selectedBg = ImageUtils.sizeImage(EncodedImage.getEncodedImageResource("selected_bg.jpg"), fieldWidth, fieldHeight);
		boldFont = Font.getDefault().derive(Font.BOLD, 16);
		plainFont = Font.getDefault().derive(Font.PLAIN, 14);	
//		titleLength = boldFont.getAdvance(news.getTitle());
//		total = titleLength + imageNews.getWidth() + 10;
	}
	
	protected void layout(int width, int height) {
		setExtent(fieldWidth , fieldHeight);
	}

	protected void paint(Graphics g) {
		//draw background when focus
		if (isFocus()) {
			g.setColor(0x99a7b2);
//			g.fillRect(0, 0, fieldWidth, fieldHeight);
			//g.drawImage(0, 0, fieldWidth, fieldHeight, selectedBg, 0, 0, 0);
			g.setColor(Color.WHITE);
		} else {
			g.setColor(Color.BLACK);
		}
		
		//image news
		g.drawImage(5, (fieldHeight/2) - (imageNews.getHeight()/2), imageNews.getWidth(), imageNews.getHeight(), imageNews, 0, 0, 0);
		
		//set title text
		g.setFont(boldFont);
		g.drawText(news.getTitle(), imageNews.getWidth() + 10 + pos, 5, DrawStyle.ELLIPSIS, fieldWidth - (imageNews.getWidth() + 10));
		
		//set time text
		g.setFont(plainFont);
		g.drawText(news.getTime(), imageNews.getWidth() + 10, 5 + plainFont.getHeight() + 5);
		
		//draw divider line
		g.setColor(0x808080);
		g.drawLine(0, 0, fieldWidth , 0);
		g.drawLine(0, fieldHeight, fieldWidth , fieldHeight);
	}
	
	protected boolean keyChar(char character, int status, int time) {
		if (character == Keypad.KEY_ENTER) {
			fieldChangeNotify(news.getId());
		}
		return true;
	}
	
	protected boolean navigationClick(int status, int time) {
		fieldChangeNotify(news.getId());
		return true;
	}
	
	protected void onFocus(int direction) {
		super.onFocus(direction);
//		startScroll();
		invalidate();
	}
	
	protected void onUnfocus() {
		super.onUnfocus();
		//------
		if (scrollTimer != null) {
			scrollTimerTask.cancel();
			scrollTimer.cancel();
			scrollTimer = null;
			scrollTimerTask = null;
		}
		pos = 0;
		//------
		invalidate();
	}
	
	public boolean isFocusable() {
		return true;
	}
	
	protected void drawFocus(Graphics graphics, boolean on) {
		super.drawFocus(graphics, on);
	}	

//	private void startScroll() {
//		if (scrollTimer == null) {
//			scrollTimer = new Timer();
//			scrollTimerTask = new TimerTask() {
//				public void run() {
//					if(total > fieldWidth){
//						if(fieldWidth - total >= pos){
//							hold += 1;
//							if(hold == 20){
//								pos = 0;
//								hold = 0;
//							}
//						} else {
//							pos = pos - 1;
//							invalidate();
//						}
//					}
//				}
//			};
//			scrollTimer.scheduleAtFixedRate(scrollTimerTask, 100, 100);
//		}
//	}
}
