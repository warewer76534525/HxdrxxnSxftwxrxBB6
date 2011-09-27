package com.triplelands.view;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;

import com.triplelands.model.News;
import com.triplelands.utils.DataProcessor;
import com.triplelands.view.component.CustomButton;

public class NewsDetailScreen extends BaseScreen {
	
	private DataProcessor processor;
	private int id;
	private News news;

	public NewsDetailScreen(String json) {
		super("", false);
		processor = new DataProcessor();
		news = processor.getNews(json);
		id = news.getId();
		
		addTitle();
		addContent();
	}

	public void fieldChanged(Field field, int context) {
		if(field instanceof CustomButton){
			UiApplication.getUiApplication().pushScreen(new AddCommentScreen(id));
		}
	}
	
	private void addTitle(){		
		add(new LabelField(news.getTitle(), FOCUSABLE) {
			protected void paint(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		});
		add(new SeparatorField());
	}
	
	private void addContent(){
		RichTextField rtf = new RichTextField(news.getTime() + "\n\n" + news.getContent(), FOCUSABLE) {
			protected void paint(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		rtf.setFont(Font.getDefault().derive(Font.PLAIN, 18));
		add(rtf);
	}
}