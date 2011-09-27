package com.triplelands.ahversion.datafolderconf;

import net.rim.blackberry.api.menuitem.ApplicationMenuItem;
import net.rim.blackberry.api.messagelist.ApplicationFolderIntegrationConfig;
import net.rim.blackberry.api.messagelist.ApplicationIcon;
import net.rim.blackberry.api.messagelist.ApplicationMessage;
import net.rim.blackberry.api.messagelist.ApplicationMessageFolder;
import net.rim.blackberry.api.messagelist.ApplicationMessageFolderListener;
import net.rim.blackberry.api.messagelist.ApplicationMessageFolderRegistry;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.UiApplication;

import com.triplelands.ahversion.idatafolderconf.DataMessage;
import com.triplelands.ahversion.idatafolderconf.DataMessageTempStore;
import com.triplelands.ahversion.idatafolderconf.IDataFolderConfManager;
import com.triplelands.view.RootScreen;

public class DataFolderConfManager implements ApplicationMessageFolderListener, IDataFolderConfManager {
	
	public void actionPerformed(int action, ApplicationMessage[] messages, ApplicationMessageFolder folder) {	
	}
//	
//	/* (non-Javadoc)
//	 * @see com.triplelands.daemon.INotificationFolderConf#init()
//	 */
	public void init(String displayName, String iconName){
		ApplicationMessageFolderRegistry reg = ApplicationMessageFolderRegistry.getInstance();
		ApplicationDescriptor daemonDescr = ApplicationDescriptor.currentApplicationDescriptor();
		ApplicationDescriptor mainDescr = new ApplicationDescriptor(daemonDescr, displayName, new String[] {});
		ApplicationDescriptor uiCallbackDescr = new ApplicationDescriptor(daemonDescr, displayName, new String[] {"gui"});
		
		// Get existing messages from storage and register them in folders        
        ApplicationFolderIntegrationConfig inboxIntegration = new ApplicationFolderIntegrationConfig(false, true, mainDescr);
        ApplicationFolderIntegrationConfig deletedIntegration = new ApplicationFolderIntegrationConfig(false);
        
        DataMessageTempStore messages = DataMessageTempStore.getInstance();
		ApplicationMessageFolder inbox = reg.registerFolder(DataMessage.INBOX_FOLDER_ID, "New Signal Data", messages.getInboxMessages(), inboxIntegration);
		ApplicationMessageFolder deleted = reg.registerFolder(DataMessage.DELETED_FOLDER_ID, "Deleted Signal Data", messages.getDeletedMessages(), deletedIntegration);
		
		// Register as a listener for callback notifications
        inbox.addListener(this, ApplicationMessageFolderListener.MESSAGE_DELETED | ApplicationMessageFolderListener.MESSAGE_MARKED_OPENED
            | ApplicationMessageFolderListener.MESSAGE_MARKED_UNOPENED | ApplicationMessageFolderListener.MESSAGES_MARKED_OLD, daemonDescr);
            deleted.addListener(this, ApplicationMessageFolderListener.MESSAGE_DELETED, daemonDescr);

        messages.setFolders(inbox, deleted);
        
        // We've registered two folders, specify root folder name for the
        // [View Folder] screen.
        reg.setRootFolderName(displayName);

        // 2. Register message icons -------------------------------------------

        ApplicationIcon newIcon = new ApplicationIcon(EncodedImage.getEncodedImageResource(iconName));
        ApplicationIcon readIcon = new ApplicationIcon(EncodedImage.getEncodedImageResource(iconName));
        ApplicationIcon deletedIcon = new ApplicationIcon(EncodedImage.getEncodedImageResource(iconName));

        reg.registerMessageIcon(DataMessage.MESSAGE_TYPE, DataMessage.STATUS_NEW, newIcon);
        reg.registerMessageIcon(DataMessage.MESSAGE_TYPE, DataMessage.STATUS_OPENED, readIcon);
        reg.registerMessageIcon(DataMessage.MESSAGE_TYPE, DataMessage.STATUS_DELETED, deletedIcon);
        
        ApplicationMenuItem newMenuItem = new OpenContextMenu(0);
        ApplicationMenuItem openMenuItem = new OpenContextMenu(1);
        ApplicationMenuItem deleteMenuItem = new OpenContextMenu(2);
        ApplicationMenuItem[] newGuiMenuItems = new ApplicationMenuItem[] {newMenuItem};
        ApplicationMenuItem[] openGuiMenuItems = new ApplicationMenuItem[] {openMenuItem};
        ApplicationMenuItem[] deleteGuiMenuItems = new ApplicationMenuItem[] {deleteMenuItem};
        reg.registerMessageMenuItems(DataMessage.MESSAGE_TYPE, DataMessage.STATUS_NEW, newGuiMenuItems, uiCallbackDescr);
        reg.registerMessageMenuItems(DataMessage.MESSAGE_TYPE, DataMessage.STATUS_OPENED, openGuiMenuItems, uiCallbackDescr);
        reg.registerMessageMenuItems(DataMessage.MESSAGE_TYPE, DataMessage.STATUS_DELETED, deleteGuiMenuItems, uiCallbackDescr);
        reg.setBulkMarkOperationsSupport(DataMessage.MESSAGE_TYPE, DataMessage.STATUS_NEW, true, false);
        reg.setBulkMarkOperationsSupport(DataMessage.MESSAGE_TYPE, DataMessage.STATUS_OPENED, false, true);
	}
	
	public static void emptyMessageInDataStore(){
		DataMessageTempStore messageStore = DataMessageTempStore.getInstance();
    	messageStore.emptyMessages();
	}
	
	
	/**
     * Open Context menu item. Marks read and opens the selected message for
     * viewing.
     */
    static class OpenContextMenu extends ApplicationMenuItem
    {
        /**
         * Creates a new ApplicationMenuItem instance with provided menu position
         * 
         * @param order Display order of this item, lower numbers correspond to higher placement in the menu
         */
        public OpenContextMenu(int order)
        {
            super(order);
        }

        /**
         * @see ApplicationMenuItem#run(Object)
         */
        public Object run(Object context)
        {
            // Show application
            UiApplication uiApplication = UiApplication.getUiApplication();
            uiApplication.pushScreen(new RootScreen("Main Screen"));
            uiApplication.requestForeground();
            return context;
        }


        /**
         * @see java.lang.Object#toString()
         */
        public String toString()
        {
            return "Open Hidreen Software";
        }
    }
	
}
