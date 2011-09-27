package com.triplelands.view.component;

import java.util.Vector;

import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.triplelands.model.Comment;

public class CommentLayout extends VerticalFieldManager {
	
	public CommentLayout(Vector commentList) { 
		if(commentList != null && commentList.size() > 0){
			add(new LabelField("Comments:"));
			for (int i = 0; i < commentList.size(); i++) {
				add(new CommentField((Comment)commentList.elementAt(i)));
			}
		} else {
			add(new LabelField("There is no comment."));
		}
	}
}
