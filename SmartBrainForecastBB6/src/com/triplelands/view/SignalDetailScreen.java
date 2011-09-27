package com.triplelands.view;

import java.util.Vector;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import com.triplelands.model.Signal;
import com.triplelands.tools.ImageLoadingHandler;
import com.triplelands.tools.ImagesDownloaderUtility;
import com.triplelands.utils.DataProcessor;
import com.triplelands.view.component.CustomButton;
import com.triplelands.view.component.FormattedRichTextField;
import com.triplelands.view.component.ImageField;

public class SignalDetailScreen extends BaseScreen implements ImageLoadingHandler {
	
	private String json;
	private DataProcessor processor;
	private int id;
	private Signal signal;
	private Vector imgs;
	
	public SignalDetailScreen(String json) {
		super("", false);
		this.json = json;
		
		processor = new DataProcessor();
		this.signal = processor.getSignalDetail(json); 
		id = signal.getId();
		System.out.println("id signal: " + id);
		
		String[] imagesUrl = processor.getImageUrls(json);
		if(imagesUrl != null && imagesUrl.length > 0){
			System.out.println("Will download images");
			ImagesDownloaderUtility downloader = new ImagesDownloaderUtility(imagesUrl, this);
			downloader.start();
		} else {
//			addTitle();
			addMetaSignal();
//			addListComments();
//			addCommentButton();
		}
	}

	public void fieldChanged(Field field, int context) {
		System.out.println("Field Changed");
		if(field instanceof CustomButton){
			UiApplication.getUiApplication().pushScreen(new AddCommentScreen(id));
		}
	}

	public void onReceivedImage(Vector images) {
		imgs = images;
//		addTitle();
		addMetaSignal();
		addImagesHorizontally(images);
//		addListComments();
//		addCommentButton();
	}
	
//	private void addTitle(){
//		String textLabel = "Here Goes the Title";
//		
//		add(new LabelField(textLabel, FOCUSABLE) {
//			protected void paint(Graphics graphics) {
//				graphics.setColor(Color.BLACK);
//				super.paint(graphics);
//			}
//		});
//		add(new SeparatorField());
//	}
	
	private void addMetaSignal(){
//		RichTextField rtf = new RichTextField(processor.getMetaSignal(json), FOCUSABLE) {
//			protected void paint(Graphics graphics) {
//				graphics.setColor(Color.BLACK);
//				super.paint(graphics);
//			}
//		};
//		rtf.setFont(Font.getDefault().derive(Font.PLAIN, 18));
		
		add(new FormattedRichTextField(processor.getMetaSignal(json)));
	}
	
//	private void addListComments(){
//		add(new CommentLayout(processor.getComments(json)));
//	}
//	
//	private void addCommentButton(){
//		CustomButton commentBtn = new CustomButton("Add Comment");
//		commentBtn.setChangeListener(this);
//		add(commentBtn);
//	}
	
	private void addImagesHorizontally(Vector images){
		HorizontalFieldManager hfm = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL);
		
		for (int i = 0; i < images.size(); i++) {
			hfm.add(new ImageField((EncodedImage)images.elementAt(i), ImageField.ALIGN_LEFT, i));
		}
		add(hfm);
	}
	
	
}