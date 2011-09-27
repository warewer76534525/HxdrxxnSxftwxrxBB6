package com.triplelands.view.component;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class CustomEditField extends VerticalFieldManager {
	
	private String name;
	private EditFieldComponent editField;
	
	public CustomEditField(String name, String label) {
		this(name, label, EditFieldComponent.TYPE_NORMAL);
	}
	
	public CustomEditField(String name, String label, int type) {
		this.name = name;
		
		LabelField lbl = new LabelField(label, Field.NON_FOCUSABLE);
		lbl.setPosition(10);
		add(lbl);
		editField = new EditFieldComponent(type);
		add(editField);
	}
	
	public String getText(){
		return editField.getText();
	}
	
	public void setText(String text){
		editField.setText(text);
	}
	
	public String getName(){
		return name;
	}
}
