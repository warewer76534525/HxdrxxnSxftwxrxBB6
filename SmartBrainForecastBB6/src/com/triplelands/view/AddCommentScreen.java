package com.triplelands.view;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EmailAddressEditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.triplelands.tools.AppDataPosting;
import com.triplelands.tools.PostingHandler;
import com.triplelands.utils.Constants;
import com.triplelands.view.component.CustomButton;

public class AddCommentScreen extends PopupScreen implements PostingHandler {
	private BasicEditField nameField, commentField;
	private EmailAddressEditField emailField;

	public AddCommentScreen(final int id) {
		super(new VerticalFieldManager(), DEFAULT_MENU | DEFAULT_CLOSE);
		add(new LabelField("Add Comment", Field.FIELD_HCENTER));
		add(new SeparatorField());
		nameField = new BasicEditField("Name : ","", 20, Field.EDITABLE);
		add(nameField);
		emailField = new EmailAddressEditField("Email : ", "");
		add(emailField);
		commentField = new BasicEditField("Comment : ", "");
		add(commentField);
		
		CustomButton submit = new CustomButton("Submit");
		submit.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				processData(id);
			}
		});
		add(submit);
	}
	
	private void processData(int id){
		String name = nameField.getText().toString();
		String email = emailField.getText().toString();
		String comment = commentField.getText().toString();
		if(name.equals("") || email.equals("") || comment.equals("")){
			Dialog.alert("Please fill in the blank field.");
		} else {
			String param = "name=" + name + 
			"&email=" + email + 
			"&content=" + comment + 
			"&signal_id=" + id;
			
			UiApplication.getUiApplication().pushScreen(new AppDataPosting(Constants.URL_ADD_COMMENT, param, this));
		}
	}

	public void onPostingSuccess(String response) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				close();
			}
		});
	}
}
