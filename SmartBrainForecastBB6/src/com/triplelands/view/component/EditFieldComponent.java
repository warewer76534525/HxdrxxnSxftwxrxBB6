package com.triplelands.view.component;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.PasswordEditField;

public class EditFieldComponent extends Manager {
	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_PASSWORD = 1;
	
	/**
	 * Default margins
	 */
	private final static int DEFAULT_LEFT_MARGIN = 10;
	private final static int DEFAULT_RIGHT_MARGIN = 10;
	private final static int DEFAULT_TOP_MARGIN = 0;
	private final static int DEFAULT_BOTTOM_MARGIN = 5;

	/**
	 * Default paddings
	 */
	private final static int DEFAULT_LEFT_PADDING = 10;
	private final static int DEFAULT_RIGHT_PADDING = 10;
	private final static int DEFAULT_TOP_PADDING = 5;
	private final static int DEFAULT_BOTTOM_PADDING = 5;

	/**
	 * Margins around the text box
	 */
	private int topMargin = DEFAULT_TOP_MARGIN;
	private int bottomMargin = DEFAULT_BOTTOM_MARGIN;
	private int leftMargin = DEFAULT_LEFT_MARGIN;
	private int rightMargin = DEFAULT_RIGHT_MARGIN;

	/**
	 * Padding around the text box
	 */
	private int topPadding = DEFAULT_TOP_PADDING;
	private int bottomPadding = DEFAULT_BOTTOM_PADDING;
	private int leftPadding = DEFAULT_LEFT_PADDING;
	private int rightPadding = DEFAULT_RIGHT_PADDING;

	/**
	 * Amount of empty space horizontally around the text box
	 */
	private int totalHorizontalEmptySpace = leftMargin + leftPadding
			+ rightPadding + rightMargin;

	/**
	 * Amount of empty space vertically around the text box
	 */
	private int totalVerticalEmptySpace = topMargin + topPadding
			+ bottomPadding + bottomMargin;

	/**
	 * Minimum height of the text box required to display the text entered
	 */
	private int minHeight = getFont().getHeight() + totalVerticalEmptySpace;

	/**
	 * Width of the text box
	 */
	private int width = Display.getWidth();

	/**
	 * Height of the text box
	 */
	private int height = minHeight;

	/**
	 * Background image for the text box
	 */
	private EncodedImage backgroundImage;

	/**
	 * Bitmap version of the backgroundImage. Needed to reduce the calculation
	 * overhead incurred by scaling of the given image and derivation of Bitmap
	 * from the EncodedImage every time it is needed.
	 */
	private Bitmap bitmapBackgroundImage;

	private Field field;

	// private BasicEditField editField;

	// private String entireText;

	public EditFieldComponent(int type) {
		// Let the super class initialize the core
		super(0);
		/**
		 * The core element of this text box
		 */
		if(type == TYPE_PASSWORD){
			field = new PasswordEditField();
		} else {
			field = new EditField();
			
		}
		add(field);
	}

	public EditFieldComponent(EncodedImage backgroundImage, int type) {
		this(type);
		setBackgroundImage(backgroundImage);
	}

	public void setSize(int width, int height) {
		boolean isChanged = false;

		if (width > 0 // Ignore invalid width
				&& this.width != width) {
			this.width = width;
			isChanged = true;
		}

		// Ignore the specified height if it is less
		// than the minimum height required to display the text.
		if (height > minHeight && height != this.height) {
			this.height = height;
			isChanged = true;
		}

		// If width/height has been changed and background image
		// is available, adapt it to the new dimension
		if (isChanged == true && backgroundImage != null) {
			bitmapBackgroundImage = getScaledBitmapImage(backgroundImage,
					this.width - (leftMargin + rightMargin), this.height
							- (topMargin + bottomMargin));
		}
	}

	public void setWidth(int width) {

		if (width > 0 && width != this.width) {
			this.width = width;

			// If background image is available, adapt it to the new width
			if (backgroundImage != null) {
				bitmapBackgroundImage = getScaledBitmapImage(backgroundImage,
						this.width - (leftMargin + rightMargin), this.height
								- (topMargin + bottomMargin));
			}
		}
	}

	public void setHeight(int height) {
		// Ignore the specified height if it is
		// less than the minimum height required to display the text.
		if (height > minHeight) {
			this.height = height;

			// If background image is available, adapt it to the new width
			if (backgroundImage != null) {
				bitmapBackgroundImage = getScaledBitmapImage(backgroundImage,
						this.width - (leftMargin + rightMargin), this.height
								- (topMargin + bottomMargin));
			}
		}
	}

	public void setBackgroundImage(EncodedImage backgroundImage) {
		this.backgroundImage = backgroundImage;

		// Consider the height of background image in
		// calculating the height of the text box.
		// setHeight() does not ensure that specified
		// height will be in effect, of course, for valid reasons.
		// So derivation of Bitmap image in the setHeight() method is not sure.
		setHeight(backgroundImage.getHeight() + topMargin + bottomMargin);
		if (bitmapBackgroundImage == null) {
			bitmapBackgroundImage = getScaledBitmapImage(backgroundImage,
					this.width - (leftMargin + rightMargin), this.height
							- (topMargin + bottomMargin));
		}
	}

	/**
	 * Generate and return a Bitmap Image scaled according to the specified
	 * width and height.
	 * 
	 * @param image
	 *            EncodedImage object
	 * @param width
	 *            Intended width of the returned Bitmap object
	 * @param height
	 *            Intended height of the returned Bitmap object
	 * @return Bitmap object
	 */
	private Bitmap getScaledBitmapImage(EncodedImage image, int width,
			int height) {
		// Handle null image
		if (image == null) {
			return null;
		}

		int currentWidthFixed32 = Fixed32.toFP(image.getWidth());
		int currentHeightFixed32 = Fixed32.toFP(image.getHeight());

		int requiredWidthFixed32 = Fixed32.toFP(width);
		int requiredHeightFixed32 = Fixed32.toFP(height);

		int scaleXFixed32 = Fixed32.div(currentWidthFixed32,
				requiredWidthFixed32);
		int scaleYFixed32 = Fixed32.div(currentHeightFixed32,
				requiredHeightFixed32);

		image = image.scaleImage32(scaleXFixed32, scaleYFixed32);

		return image.getBitmap();
	}

	protected void sublayout(int width, int height) {
		// We have one and only child - the edit field.
		// Place it at the appropriate place.
		Field field = getField(0);
		layoutChild(field, this.width - totalHorizontalEmptySpace, this.height
				- totalVerticalEmptySpace);
		setPositionChild(field, leftMargin + leftPadding, topMargin
				+ topPadding);

		setExtent(this.width, this.height);
	}

	public void setTopMargin(int topMargin) {
		this.topMargin = topMargin;
	}

	public void setBottomMargin(int bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	protected void paint(Graphics graphics)
    {
        // Draw background image if available, otherwise draw a rectangle
        if (bitmapBackgroundImage == null)
        {
        	graphics.setColor(Color.GRAY);
        	graphics.drawRoundRect(leftMargin, topMargin, width - (leftMargin+rightMargin), height - (topMargin+bottomMargin), 20, 20);
        	graphics.setColor(Color.BLACK);
        }
        else
        {
            graphics.drawBitmap(leftMargin, topMargin, 
                                width - (leftMargin+rightMargin), 
                                height - (topMargin+bottomMargin),  
                                bitmapBackgroundImage, 0, 0);
        }
        
        // Determine the rightward text that can fit into the visible edit field
        Field ef = getField();
        String entireText = "";
        if(ef instanceof PasswordEditField) entireText = ((PasswordEditField)field).getText();
        if(ef instanceof EditField) entireText = ((EditField)field).getText();
        
        boolean longText = false;
        String textToDraw = "";
        Font font = getFont();
        int availableWidth = width - totalHorizontalEmptySpace;
        if (font.getAdvance(entireText) <= availableWidth)
        {
            textToDraw = entireText;
        }
        else
        {
            int endIndex = entireText.length();
            for (int beginIndex = 1; beginIndex < endIndex; beginIndex++)
            {
                textToDraw = entireText.substring(beginIndex);
                if (font.getAdvance(textToDraw) <= availableWidth)
                {
                    longText = true;
                    break;
                }
            }
        }
        
        if (longText == true)
        {        
            // Force the edit field display only the truncated text
        	if(ef instanceof PasswordEditField) ((PasswordEditField)field).setText(textToDraw);
            if(ef instanceof EditField) ((EditField)field).setText(textToDraw);
            
            // Now let the components draw themselves
            super.paint(graphics);
            
            // Return the text field its original text
            if(ef instanceof PasswordEditField) ((PasswordEditField)field).setText(entireText);
            if(ef instanceof EditField) ((EditField)field).setText(entireText);
        }
        else
        {
            super.paint(graphics);
        }
    }

	public int getPreferredWidth() {
		return width;
	}

	public int getPreferredHeight() {
		return height;
	}

	protected boolean keyChar(char ch, int status, int time) {
		if (ch == Characters.ENTER) {
			return true;
		} else {
			return super.keyChar(ch, status, time);
		}
	}

	public String getText() {
		if(field instanceof PasswordEditField){
			return ((PasswordEditField) getField(0)).getText();
		} else {
			return ((EditField) getField(0)).getText();
		}
		
	}

	public void setText(final String text) {
		if(field instanceof PasswordEditField){
			((PasswordEditField) getField(0)).setText(text);
		} else {
			((EditField) getField(0)).setText(text);
		}
	}
	
	private Field getField(){
		if(field instanceof PasswordEditField){
			return ((PasswordEditField) getField(0));
		} else {
			return ((EditField) getField(0));
		}
	}
}