package com.triplelands.view;

import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import com.triplelands.datastore.DataStorer;
import com.triplelands.tools.AppDataPosting;
import com.triplelands.tools.PostingHandler;
import com.triplelands.tools.RegisterFormLoading;
import com.triplelands.utils.Constants;
import com.triplelands.utils.DataProcessor;
import com.triplelands.utils.StringUtils;
import com.triplelands.view.component.CustomButton;
import com.triplelands.view.component.CustomEditField;
import com.triplelands.view.component.EditFieldComponent;
import com.triplelands.view.component.SpaceField;

public class LoginScreen extends BaseScreen implements PostingHandler {

	private CustomEditField username, password;
	private CustomButton reg, login;
	private DataStorer store;
	public LoginScreen(String title) {
		super(title, false);
		
		store = new DataStorer();
		
		addTitle(title);
		username = new CustomEditField("username", "Email");
		username.setText(store.getData("email"));
		password = new CustomEditField("password", "Password", EditFieldComponent.TYPE_PASSWORD);
		
		add(username);
		add(password);
		
		HorizontalFieldManager hfm = new HorizontalFieldManager();
		
		login = new CustomButton("Login", 0);
		login.setChangeListener(this);
		reg = new CustomButton("Register", 1);
		reg.setChangeListener(this);
		
		hfm.add(login);
		hfm.add(reg);
		add(hfm);
		add(new SpaceField(15));
		add(new SeparatorField());
		//add(new LinkField("Forgot Password"));
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

	public void fieldChanged(Field field, int context) {
		if(field == reg){
			UiApplication.getUiApplication().pushScreen(new RegisterFormLoading(Constants.URL_REGISTER));
		}
		if(field == login){
			if(username.getText().toString().equals("") || password.getText().toString().equals("")){
				Dialog.alert("Please fill your login information.");
			} else {
				String os;
				//#ifdef HANDHELD_VERSION_42
			        os =  "4.2.0";
				//#else
				    os =  DeviceInfo.getSoftwareVersion();
				//#endif
				String param = 
					"email=" + username.getText().toString() + 
					"&password=" + StringUtils.getMD5("hisoft" + password.getText().toString()) + 
					"&type=bb" +
					"&address=" + Integer.toHexString( DeviceInfo.getDeviceId() ) +
					"&osversion=" + os +
					"&model=" + DeviceInfo.getDeviceName() +
					"&__submit=login";
				UiApplication.getUiApplication().pushScreen(new AppDataPosting(Constants.URL_LOGIN, param, this));
			}
		}
	}

	public void onPostingSuccess(String response) {
		//Success Login
		System.out.println("Respon: " + response);
		DataProcessor processor = new DataProcessor();
		store.addData("email", processor.getData(response, "email"));
		store.addData("session", processor.getData(response, "session_id"));
		store.addData("license", processor.getData(response, "license"));
		store.addData("name", processor.getData(response, "name"));
		store.addData("country", processor.getData(response, "country"));
		store.addData("city", processor.getData(response, "city"));
		store.addData("phone", processor.getData(response, "phone"));
		UiApplication.getUiApplication().pushScreen(new RootScreen("Home"));
		this.close();
	}

}
