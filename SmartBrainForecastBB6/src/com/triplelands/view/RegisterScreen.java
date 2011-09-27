package com.triplelands.view;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;

import com.triplelands.datastore.DataStorer;
import com.triplelands.model.KeyValuePair;
import com.triplelands.tools.AppDataPosting;
import com.triplelands.tools.PostingHandler;
import com.triplelands.utils.Constants;
import com.triplelands.utils.DataProcessor;
import com.triplelands.utils.StringUtils;
import com.triplelands.view.component.CustomButton;
import com.triplelands.view.component.CustomDropdownField;
import com.triplelands.view.component.CustomEditField;
import com.triplelands.view.component.EditFieldComponent;
import com.triplelands.view.component.SpaceField;

public class RegisterScreen extends BaseScreen implements PostingHandler {

	private Vector listField;
	private CustomDropdownField dropdown;

	public RegisterScreen(String title, String json) {
		super(title, false);
		
		addTitle(title);
		
		DataProcessor processor = new DataProcessor();
		Vector data = processor.getKeyValueArray(json);
		listField = buildFields(data);
		
		for (int i = 0; i < listField.size(); i++) {
			add((CustomEditField)listField.elementAt(i));
		}
		addTimeZone();
		CustomButton registerButton = new CustomButton("Register");
		registerButton.setChangeListener(this);
		add(registerButton);
	}

	private void addTitle(String title) {
		add(new LabelField(title, NON_FOCUSABLE) {
			protected void paint(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		});
		add(new SeparatorField());
		add(new SpaceField(20));
	}
	
	private Vector buildFields(Vector data){
		Vector listField = new Vector();
		for (int i = 0; i < data.size(); i++) {
			KeyValuePair map = (KeyValuePair)data.elementAt(i);
			boolean password = (map.getKey().toLowerCase().indexOf("password") != -1);
			CustomEditField editField;
			if(password){
				editField = new CustomEditField(map.getKey(), map.getValue(), EditFieldComponent.TYPE_PASSWORD);
			} else {
				editField = new CustomEditField(map.getKey(), map.getValue());
			}
			listField.addElement(editField);
		}
		
		data.removeAllElements();
		data = null;
		return listField;
	}
	
	private void addTimeZone(){
		Vector choices = new Vector();
		for (int i = 0; i < Constants.TIME_ZONE_KEYS.length; i++) {
			choices.addElement(new KeyValuePair(Constants.TIME_ZONE_KEYS[i], Constants.TIME_ZONES[i]));
			
		}
		dropdown = new CustomDropdownField("Time Zone", choices, 0);
		add(dropdown);
	}

	public void fieldChanged(Field field, int context) {
		if(field instanceof CustomButton){
			boolean valid = true;
			StringBuffer param = new StringBuffer();
			for (int i = 0; i < listField.size(); i++) {
				CustomEditField cef = (CustomEditField)listField.elementAt(i);
				if(cef.getText().toString().equals("")){
					valid = false;
				}
				if((cef.getName().toLowerCase().indexOf("password") != -1)){
					param.append(cef.getName() + "=" + StringUtils.getMD5("hisoft" + cef.getText()) + "&");
				} else {
					param.append(cef.getName() + "=" + cef.getText() + "&");
				}
				param.append("timezone=" + dropdown.getSelectedKey() + "&");
			}
			if(valid){
				param.append("__submit=register");
				UiApplication.getUiApplication().pushScreen(new AppDataPosting(Constants.URL_REGISTER, param.toString(), this));
			} else {
				Dialog.alert("Please fill in the blank field.");
			}
		}
	}

	public void onPostingSuccess(String response) {
		DataProcessor processor = new DataProcessor();
		DataStorer store = new DataStorer();
		store.addData("email", processor.getEmail(response));
		close();
	}

}
