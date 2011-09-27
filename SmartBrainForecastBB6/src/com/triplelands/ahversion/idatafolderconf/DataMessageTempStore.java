/**
 * 
 * Copyright © 1998-2010 Research In Motion Ltd.
 * 
 * Note: For the sake of simplicity, this sample application may not leverage
 * resource bundles and resource strings.  However, it is STRONGLY recommended
 * that application developers make use of the localization features available
 * within the BlackBerry development platform to ensure a seamless application
 * experience across a variety of languages and geographies.  For more information
 * on localizing your application, please refer to the BlackBerry Java Development
 * Environment Development Guide associated with this release.
 */

package com.triplelands.ahversion.idatafolderconf;

import java.util.Vector;

import net.rim.blackberry.api.messagelist.ApplicationMessageFolder;
import net.rim.device.api.collection.ReadableList;
import net.rim.device.api.system.RuntimeStore;


/**
 * This class is used to facilitate the storage of messages. For the sake of
 * simplicitly, we are saving messages in the device runtime store. In a real
 * world situation, messages would be saved in device persistent store and/or
 * on a mail server.
 */
public final class DataMessageTempStore
{
    // com.rim.samples.device.messagelistdemo.MessageListDemoStore
    private static final long MSG_KEY = 0xcf2b552e0e98a715L; 

    private static DataMessageTempStore _instance;

    private ReadableListImpl _inboxMessages;
    private ReadableListImpl _deletedMessages;
    private ApplicationMessageFolder _mainFolder;
    private ApplicationMessageFolder _deletedFolder;


    /**
     * Creates a new MessageListDemoStore object
     */
    private DataMessageTempStore()
    {
        _inboxMessages = new ReadableListImpl();
        _deletedMessages = new ReadableListImpl();
    }


    /**
     * Gets the singleton instance of the MessageListDemoStore
     * 
     * @return The singleton instance of the MessagelistDemoStore
     */
    public static synchronized DataMessageTempStore getInstance()
    {
        // Keep messages as singleton in the RuntimeStore
        if(_instance == null)
        {
            RuntimeStore rs = RuntimeStore.getRuntimeStore();

            synchronized(rs)
            {
                _instance = (DataMessageTempStore) rs.get(MSG_KEY);

                if(_instance == null)
                {
                    _instance = new DataMessageTempStore();
                    rs.put(MSG_KEY, _instance);
                }
            }
        }

        return _instance;
    }


    /**
     * Sets the main and deleted folders
     * 
     * @param mainFolder The main folder to use
     * @param deletedFolder The deleted folder to use
     */
    public void setFolders(ApplicationMessageFolder mainFolder, ApplicationMessageFolder deletedFolder)
    {
    	System.out.println("SET FOLDERS");
        _mainFolder = mainFolder;
        _deletedFolder = deletedFolder;
    }


    /**
     * Retrieves the inbox folder
     * 
     * @return The inbox folder
     */
    public ApplicationMessageFolder getInboxFolder()
    {
        return _mainFolder;
    }


    /**
     * Retrieves the deleted folder
     * 
     * @return The deleted folder
     */
    public ApplicationMessageFolder getDeletedFolder()
    {
        return _deletedFolder;
    }


    /**
     * Moves a message into the deleted folder
     * 
     * @param message The message to move to the deleted folder
     */
    public void deleteInboxMessage(DataMessage message)
    {
        message.messageDeleted();
        _inboxMessages.removeMessage(message);
        _deletedMessages.addMessage(message);
    }
    
    public void emptyMessages(){
    	_inboxMessages.emptyAllMessages();
    	_deletedMessages.emptyAllMessages();
    	_mainFolder.fireReset();
    	_deletedFolder.fireReset();
    }


    /**
     * Commits a message to the persistent store
     * 
     * @param message The message to commit
     */
    void commitMessage(DataMessage message)
    {
        // This empty method exists to reinforce the idea that in a real world
        // situation messages would be saved in device persistent store and/or
        // on a mail server.
    }


    /**
     * Adds a message to the inbox
     * 
     * @param message The message to add to the inbox
     */
    public void addInboxMessage(DataMessage message)
    {
        _inboxMessages.addMessage(message);
    }


    /**
     * Completely deletes the message from the message store
     * 
     * @param message The message to delete from the message store
     */
    public void deleteMessageCompletely(DataMessage message)
    {
        _deletedMessages.removeMessage(message);
    }


    /**
     * Retrieves the inbox messages as a readable list
     * 
     * @return The readable list of all the inbox messages
     */
    public ReadableListImpl getInboxMessages()
    {
        return _inboxMessages;
    }


    /**
     * Gets the deleted messages as a readable list
     * 
     * @return The readable list of all the deleted messages
     */
    public ReadableListImpl getDeletedMessages()
    {
        return _deletedMessages;
    }
    

    /**
     * This is an implementation of the ReadableList interface which stores the
     * list of messages using a Vector.
     */
    static class ReadableListImpl implements ReadableList
    {
        private Vector messages;

        /**
         * Creates a empty instance of ReadableListImpl
         */
        ReadableListImpl()
        {
            messages = new Vector();
        }


        /**
         * @see net.rim.device.api.collection.ReadableList#getAt(int)
         */
        public Object getAt(int index)
        {
            return messages.elementAt(index);
        }


        /**
         * @see net.rim.device.api.collection.ReadableList#getAt(int, int, Object, int)
         */
        public int getAt(int index, int count, Object[] elements, int destIndex)
        {
            return 0;
        }


        /**
         * @see net.rim.device.api.collection.ReadableList#getIndex(Object)
         */
        public int getIndex(Object element)
        {
            return messages.indexOf(element);
        }


        /**
         * @see net.rim.device.api.collection.ReadableList#size()
         */
        public int size()
        {
            return messages.size();
        }


        /**
         * Add a message to this list
         * 
         * @param message The message to add to this list
         */
        void addMessage(DataMessage message)
        {
            messages.addElement(message);
        }
        

        /**
         * Removes a message from this list
         * 
         * @param message The message to remove from this list
         */
        void removeMessage(DataMessage message)
        {
            messages.removeElement(message);
        }
        
        void emptyAllMessages(){
        	messages.removeAllElements();
        }
    }
}
