package com.triplelands.view.component;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.triplelands.model.Comment;

public class CommentField extends VerticalFieldManager {
	private VerticalFieldManager manager;
	private RichTextField richTextField;
	private Font boldFont, plainFont;
	
	public CommentField(Comment comment) {
		boldFont = Font.getDefault().derive(Font.BOLD, 16);
		plainFont = Font.getDefault().derive(Font.PLAIN, 14);
		
		manager = new VerticalFieldManager();
		manager.setBorder(BorderFactory.createRoundedBorder(new XYEdges(10, 10, 10, 10),Color.BLACK, Border.STYLE_SOLID));
		LabelField lblCommentator = new LabelField(){
			protected void paint(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		lblCommentator.setFont(boldFont);
		lblCommentator.setText(comment.getCommentator());
		
		manager.add(lblCommentator);
		
		richTextField = new RichTextField(){
			protected void paint(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		richTextField.setFont(plainFont);
		richTextField.setText(comment.getComment());
		manager.add(richTextField);
		add(manager);
	}
}
