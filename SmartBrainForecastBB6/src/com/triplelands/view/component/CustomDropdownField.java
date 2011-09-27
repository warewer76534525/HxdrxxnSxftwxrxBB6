package com.triplelands.view.component;

import java.util.Vector;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.triplelands.model.KeyValuePair;

public class CustomDropdownField extends VerticalFieldManager {

	private Vector choices;
	private ObjectChoiceField choiceField;
	
	public CustomDropdownField(String label, Vector keyValueChoices, int selectedIndex) {
		this.choices = keyValueChoices;
		initDropdown();
		LabelField lbl = new LabelField(label, Field.NON_FOCUSABLE);
		lbl.setPosition(10);
		add(lbl);
		add(choiceField);
	}
	
	public String getSelectedValue(){
		int selected = choiceField.getSelectedIndex();
		KeyValuePair nameValue = (KeyValuePair)choices.elementAt(selected);
		return nameValue.getValue();
	}
	
	public String getSelectedKey(){
		int selected = choiceField.getSelectedIndex();
		KeyValuePair nameValue = (KeyValuePair)choices.elementAt(selected);
		return nameValue.getKey();
	}
	
	private void initDropdown(){
		String[] listChoiceValue = new String[choices.size()];
		for (int i = 0; i < choices.size(); i++) {
			listChoiceValue[i] = ((KeyValuePair)choices.elementAt(i)).getValue();
		}
		choiceField = new ObjectChoiceField(null, listChoiceValue, 0);
	}
	
}
