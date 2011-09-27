package com.triplelands.view.component;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.triplelands.model.KeyValuePair;
import com.triplelands.utils.StringUtils;

public class FormattedRichTextField extends VerticalFieldManager {
	
	private static String H1_HEADER_TYPE = "h1";
	private static String H2_HEADER_TYPE = "h2";
	private static String H1_START_TAG = "<h1>";
	private static String H1_END_TAG = "</h1>";
	private static String H2_START_TAG = "<h2>";
	private static String H2_END_TAG = "</h2>";
	private static String BOLD_START_TAG = "<b>";
	private static String BOLD_END_TAG = "</b>";
	private static String ITALIC_START_TAG = "<i>";
	private static String ITALIC_END_TAG = "</i>";
	private static String UNDERLINE_START_TAG = "<u>";
	private static String UNDERLINE_END_TAG = "</u>";
	private static String ARR_TAG[] = {BOLD_START_TAG, BOLD_END_TAG, ITALIC_START_TAG, ITALIC_END_TAG, UNDERLINE_START_TAG, UNDERLINE_END_TAG};
	private String formattedText;
	private Vector listOfAllContent;
	
	public FormattedRichTextField(String text) {
		formattedText = text + " ";
//		formattedText = "<h1>Info: Amygdala-D1, USDCHF, 2011-07-27</h1>\nPattern found on <b>USDCHF (US Dollar vs Swiss Franc)</b>based on Amygdala-D1 with pattern code #190, 21 samples in the past calculated from 2004.01.29 00:00. Possible direction is  UP  with historical probability 66.67. Find your own entry point based on support / resistance around 0.79989 - 0.79664 level, approximate target level will be around %exit%. USDCHF price range is around 989.98 points, traders can place StopLoss around 395.99 points and TakeProfit around 594 points.\n- Symbol: USDCHF (US Dollar vs Swiss Franc)\n- Pattern: #190\n- Samples: 21 from 2004.01.29 00:00\n- Direction:  UP \n- Entry: 0.79989 - 0.79664\n- Target: 0.80338 - 0.80756\n- Range: 989.98\n- StopLoss: 395.99 points\n- TakeProfit: 594 points\n\n<h2>Live Chart, updated every 30 minutes</h2> \n<b>Note:</b> \nAmygdala-D1 is pattern recognition method using Artificial Neural Network, it is based on CandleStick pattern. Past performance will not gaurantee the future performance, trade at your own risk\n";
		listOfAllContent = new Vector();
				
		parseHeaderAndContent(formattedText, H1_HEADER_TYPE, H1_START_TAG, H1_END_TAG);
		
		for (int i = 0; i < listOfAllContent.size(); i++) {
			KeyValuePair kvp = (KeyValuePair)listOfAllContent.elementAt(i);
			System.out.println("type: " + kvp.getKey());
			System.out.println("value: " + kvp.getValue());
			if(kvp.getKey().equals(H1_HEADER_TYPE)){
				addH1(kvp.getValue());
			}
			if(kvp.getKey().equals(H2_HEADER_TYPE)){
				addH2(kvp.getValue());
			}
			if(kvp.getKey().equals("content")){
				addContent(kvp.getValue());
			}
		}
	}

	private void parseHeaderAndContent(String text, String headerType, String startTag, String endTag){
		String[] arrTxt = StringUtils.split(" " + text, startTag);
		if(arrTxt.length == 0){
			//semuanya content
			if(headerType.equals(H1_HEADER_TYPE)){
				parseHeaderAndContent(text, H2_HEADER_TYPE, H2_START_TAG, H2_END_TAG);
			} else {
				listOfAllContent.addElement(new KeyValuePair("content", text));
			}
		} else {
			int length = arrTxt.length;
			for (int i = 0; i < length; i++) {
				if(i == 0){
					if(!arrTxt[i].equals(" ")){
						//diawali content
						if(headerType.equals(H1_HEADER_TYPE)){
							parseHeaderAndContent(arrTxt[0], H2_HEADER_TYPE, H2_START_TAG, H2_END_TAG);
						} else {
							listOfAllContent.addElement(new KeyValuePair("content", arrTxt[0]));
						}						
					}
				} else {
					String arrSplit[] = StringUtils.split(arrTxt[i], endTag);
					if(arrSplit.length > 1){
						String h1Txt = arrSplit[0];
						String contentTxt = arrSplit[1];
						listOfAllContent.addElement(new KeyValuePair(headerType, h1Txt));
						if(headerType.equals(H1_HEADER_TYPE)){
							parseHeaderAndContent(contentTxt, H2_HEADER_TYPE, H2_START_TAG, H2_END_TAG);
						} else {
							listOfAllContent.addElement(new KeyValuePair("content", contentTxt));
						}
					}
				}
			}
		}
	}
	
	private void addH1(String h1) {
		RichTextField rtf = new RichTextField(h1, FOCUSABLE) {
			protected void paint(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		rtf.setFont(Font.getDefault().derive(Font.BOLD, 26));
		add(rtf);
	}
	
	private void addH2(String h2) {
		RichTextField rtf = new RichTextField(h2, FOCUSABLE) {
			protected void paint(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		rtf.setFont(Font.getDefault().derive(Font.BOLD, 23));
		add(rtf);
	}

	private void addContent(String content){
		Vector listTagFound = new Vector();
		
		boolean first = false;
		for (int i = 0; i < ARR_TAG.length; i++) {
			int currentIdx = 0;
			while (currentIdx != -1) {
				int idxTag = content.indexOf(ARR_TAG[i], currentIdx);
				if( idxTag != -1){
					if (idxTag != 0 && first == false){
						listTagFound.addElement(new KeyValuePair("0", "0"));
					}
					listTagFound.addElement(new KeyValuePair("" + idxTag, ARR_TAG[i]));
					currentIdx = idxTag + 1;
					first = true;
				} else {
					currentIdx = -1;
				}
			}
		}
		
		if(listTagFound.size() > 0){
			Vector sorted = sortAscByKey(listTagFound);
			
			int[] offset = new int[sorted.size() + 1];
			byte[] attribute = new byte[sorted.size()];
			Font[] fonts = new Font[8];
			fonts[0] = Font.getDefault(); 
			fonts[1] = Font.getDefault().derive(Font.BOLD); 
			fonts[2] = Font.getDefault().derive(Font.ITALIC);
			fonts[3] = Font.getDefault().derive(Font.UNDERLINED);	
			fonts[4] = Font.getDefault().derive(Font.BOLD | Font.ITALIC);	
			fonts[5] = Font.getDefault().derive(Font.BOLD | Font.UNDERLINED);	
			fonts[6] = Font.getDefault().derive(Font.ITALIC | Font.UNDERLINED);	
			fonts[7] = Font.getDefault().derive(Font.BOLD | Font.ITALIC | Font.UNDERLINED);
			
			int currentNumber = 0;
			int currentCharTakenOut = 0;
			for (int i = 0; i < sorted.size(); i++) {
				String index = ((KeyValuePair)sorted.elementAt(i)).getKey();
				String type = ((KeyValuePair)sorted.elementAt(i)).getValue();
				currentNumber += getType(type);
				offset[i] = Integer.parseInt(index) - currentCharTakenOut;
				attribute[i] = (byte) getFontIndex(currentNumber);
				currentCharTakenOut += (type.equals("0")) ? 0 : type.length();
			}
			//offset for text length
			offset[sorted.size()] = content.length() - currentCharTakenOut;
			
			RichTextField rtField = new RichTextField (clean(content), offset, attribute, fonts, RichTextField.USE_TEXT_WIDTH); 
			add(rtField); 
		} else {
			RichTextField rtf = new RichTextField(content, FOCUSABLE) {
				protected void paint(Graphics graphics) {
					graphics.setColor(Color.BLACK);
					super.paint(graphics);
				}
			};
			rtf.setFont(Font.getDefault().derive(Font.PLAIN));
			add(rtf);
		}
	}
	
	private int getType(String type){
		int typeNumber = 0;
		if(type.toLowerCase().equals("<b>")) typeNumber = 1;
		if(type.toLowerCase().equals("<i>")) typeNumber = 2;
		if(type.toLowerCase().equals("<u>")) typeNumber = 4;
		if(type.toLowerCase().equals("</b>")) typeNumber = -1;
		if(type.toLowerCase().equals("</i>")) typeNumber = -2;
		if(type.toLowerCase().equals("</u>")) typeNumber = -4;
		return typeNumber;
	}
	
	private int getFontIndex(int total){
		int index = 0;
		switch (total) {
			case 0:
				index = 0;
				break;
			case 1:
				index = 1;
				break;
			case 2:
				index = 2;
				break;
			case 3:
				index = 4;
				break;
			case 4:
				index = 3;
				break;
			case 5:
				index = 5;
				break;
			case 6:
				index = 6;
				break;
			case 7:
				index = 7;
				break;
		}
		return index;
	}
	
	private Vector sortAscByKey(Vector vec){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < vec.size(); i++) {
			if(i == vec.size() - 1){
				sb.append(((KeyValuePair)vec.elementAt(i)).getKey());
			} else {
				sb.append(((KeyValuePair)vec.elementAt(i)).getKey() + "-");
			}
		}
		String[] arrIdx = StringUtils.explode('-', sb.toString());
		Vector v = toVector(arrIdx);
		Vector result = new Vector();
		for (int i = 0; i < arrIdx.length; i++) {
			int min = min(v);
			for (int j = 0; j < vec.size(); j++) {
				String key = ((KeyValuePair)vec.elementAt(j)).getKey();
				if(Integer.parseInt(key) == min){
					result.addElement(vec.elementAt(j));
					v.removeElement("" + min);
				}
			}
			
		}		
		return result;
	}
	
	private Vector toVector(String[] arr){
		Vector v = new Vector();
		for (int i = 0; i < arr.length; i++) {
			v.addElement(arr[i]);
		}
		return v;
	}
	
	private int min(Vector vector){
		int min = Integer.parseInt((String) vector.elementAt(0));
		for (int i = 1; i < vector.size(); i++) {
			if(Integer.parseInt((String) vector.elementAt(i)) < min) min = Integer.parseInt((String) vector.elementAt(i));
		}
		return min;
	}
	
	private String clean(String str) {
		StringBuffer sb = new StringBuffer();
		String result = str;
		for (int i = 0; i < ARR_TAG.length; i++) {
			sb.append(StringUtils.replaceAll(result, ARR_TAG[i], ""));
			result = sb.toString();
			sb.delete(0, sb.length());
		}
		return result;
	}

}
