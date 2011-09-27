package com.triplelands.ahversion.idatafolderconf;

import net.rim.blackberry.api.messagelist.ApplicationMessage;
import net.rim.device.api.system.EncodedImage;

public class DataMessage implements ApplicationMessage {
	public static final long KEY = 0x39d90c5bc6899541L; // Base folder key 

    /* INBOX_FOLDER_ID */
	public static final long INBOX_FOLDER_ID = 0x2fb5115c0e4a6c33L;

    /* DELETED_FOLDER_ID */
	public static final long DELETED_FOLDER_ID = 0x78d50a91eff39e5bL;
	
	static final int FLAG_DELETED = 1 << 17;
	static final int BASE_STATUS = ApplicationMessage.Status.INCOMING;
	public static final int STATUS_NEW = BASE_STATUS | ApplicationMessage.Status.UNOPENED;
	public static final int STATUS_OPENED = BASE_STATUS | ApplicationMessage.Status.OPENED;
	public static final int STATUS_DELETED = BASE_STATUS | FLAG_DELETED;
	
	public static final int MESSAGE_TYPE = 1;

    private String _sender;
    private String _subject;
    private String _message;
    private long _receivedTime;
    private boolean _isNew;
    private boolean _deleted;
    private String _replyMessage;
//    private long _replyTime;
    private EncodedImage _previewPicture;


    /**
     * Creates a new DemoMesage object
     */
    public DataMessage()
    {
        _isNew = true;
    }


    /**
     * Constructs a DemoMessage object with specified properties
     * 
     * @param sender The name of the sender
     * @param subject The subject of the message
     * @param message The body of the message
     * @param receivedTime The time stamp for when the message was received
     */
    public DataMessage(String sender, String subject, String message, long receivedTime)
    {
        _sender = sender;
        _subject = subject;
        _message = message;
        _receivedTime = receivedTime;
        _isNew = true;
    }


    /**
     * Stores the reply message and sets the reply time
     * 
     * @param message The reply message
     */
    void reply(String message)
    {
        markRead();
        _replyMessage = message;
//        _replyTime = System.currentTimeMillis();
    }


    /**
     * Marks this message as deleted
     */
    void messageDeleted()
    {
        _isNew = false;
        _deleted = true;
    }


    /**
     * Marks this message as new
     */
    public void markAsNew()
    {
        _isNew = true;
        _replyMessage = null;
    }


    /**
     * Marks this message as read
     */
    public void markRead()
    {
        _isNew = false;
    }


    /**
     * Indicates whether this message is new or not
     * 
     * @return True if the message is new, false otherwise
     */
    boolean isNew()
    {
        return _isNew;
    }


    /**
     * Indicates whether this message has been replied to or not
     * 
     * @return True if the message has been replied to, false otherwise
     */
    boolean hasReplied()
    {
        return _replyMessage != null;
    }


    /**
     * Sets the name of the sender who sent this message
     * 
     * @param sender The name of the sender
     */
    public void setSender(String sender)
    {
        _sender = sender;
    }


    /**
     * Sets the subject of this message
     * 
     * @param subject The subject of this message
     */
    public void setSubject(String subject)
    {
        _subject = subject;
    }


    /**
     * Sets the time at which this message was received
     * 
     * @param receivedTime The time at which this message was received
     */
    public void setReceivedTime(long receivedTime)
    {
        _receivedTime = receivedTime;
    }


    /**
     * Sets the message body
     * 
     * @param message The message body
     */
    public void setMessage(String message)
    {
        _message = message;
    }


    /**
     * Retrieves the message body
     * 
     * @return The message body
     */
    String getMessage()
    {
        return _message;
    }


    /**
     * Sets the preview picture for this message
     * 
     * @param image The desired preview picture of this message
     */
    public void setPreviewPicture(EncodedImage image)
    {
        _previewPicture = image;
    }


    // Implementation of ApplicationMessage ------------------------------------
    /**
     * @see net.rim.blackberry.api.messagelist.ApplicationMessage#getContact()
     */
    public String getContact()
    {
        return _sender;
    }


    /**
     * @see net.rim.blackberry.api.messagelist.ApplicationMessage#getStatus()
     */
    public int getStatus()
    {
        // Form message list status based on current message state
        if(_isNew)
        {
            return STATUS_NEW;
        }
        if(_deleted)
        {
            return STATUS_DELETED;
        }
        return STATUS_OPENED;
    }


    /**
     * 
     * @see net.rim.blackberry.api.messagelist.ApplicationMessage#getSubject()
     */
    public String getSubject()
    {
        if(_replyMessage != null)
        {
            return "Re: " + _subject;
        }
        else
        {
            return _subject;
        }
    }


    /**
     * @see net.rim.blackberry.api.messagelist.ApplicationMessage#getTimestamp()
     */
    public long getTimestamp()
    {
        return _receivedTime;
    }


    /**
     * @see net.rim.blackberry.api.messagelist.ApplicationMessage#getType()
     */
    public int getType()
    {
        // All messages have the same type
        return MESSAGE_TYPE;
    }


    /**
     * @see net.rim.blackberry.api.messagelist.ApplicationMessage#getPreviewText()
     */
    public String getPreviewText()
    {
        if(_message == null)
        {
            return null;
        }

        StringBuffer buffer = new StringBuffer(_message);

        return buffer.length() > 100 ? buffer.toString().substring(0, 100) + " ..." : buffer.toString();
    }


    /**
     *@see net.rim.blackberry.api.messagelist.ApplicationMessage#getCookie()
     */
    public Object getCookie(int cookieId)
    {
        return null;
    }


    /**
     * 
     * @see net.rim.blackberry.api.messagelist.ApplicationMessage#getPreviewPicture()
     */
    public Object getPreviewPicture()
    {
        return _previewPicture;
    }
	
}
