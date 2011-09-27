//#preprocess
/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.triplelands.push;

//#ifdef HANDHELD_VERSION_50
import java.util.Stack;

import javax.microedition.io.StreamConnection;

import com.triplelands.utils.Constants;

import net.rim.blackberry.api.push.PushApplication;
import net.rim.blackberry.api.push.PushApplicationDescriptor;
import net.rim.blackberry.api.push.PushApplicationRegistry;
import net.rim.blackberry.api.push.PushApplicationStatus;
import net.rim.device.api.io.http.PushInputStream;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

/**
 * Library for 5.0+ devices.
 * <p>
 * This library uses new Push API and has the following advantages over 4.2 devices.
 * <ul>
 * <li>Application doesn't have to be running in the background waiting for push messages</li>
 * <li>Push API performs automatic registration with BPAS server</li>
 * <li>API automatically notifies application on SIM swap event</li>
 * <li>BPAS server is notified when user un-installs the application</li>
 * </ul>
 */
public class PushLib50 implements PushApplication, BpasProtocol, PushMessageListener {

    private MessageReadingThread thread;

    public PushLib50() {
	}
    
    /**
     * Implementation of PushApplication interface. This method is called when an incoming push message is received. The method is
     * called on the application event thread.
     */
    public void onMessage( PushInputStream inputStream, StreamConnection conn ) {
        // process messages asynchronously because this is event thread of an UI application
        MessageReadingThread reader = thread;
        if( reader != null ) {
            reader.enqueue( inputStream, conn );
        } else {
            // not listening, decline and close the connection
//            if( PushConfig.isApplicationAcknoledgementSupported() ) {
//                PushUtils.decline( inputStream, PushInputStream.DECLINE_REASON_USERPND );
//            }
            PushUtils.close( conn, inputStream, null );
        }
    	AppLog.getInstance().appendLog("Message pushed!");
    }

    /**
     * Implementation of PushApplication interface. This method is called when BPAS registration status changes.
     */
    public void onStatusChange( PushApplicationStatus pushAppStatus ) {
    }

    // ****************** BpasProtocol implementation ************************

    // keep transaction for registration
    private Object txLock = new Object();
    //private Transaction current;
    private byte currentRegistration;
    private byte currentReason;
    private String currentError;
    
    public void register() throws Exception {
        int port = Constants.PUSH_PORT;
        String appId = Constants.PUSH_APP_ID;
        String bpasUrl = "http://www.hidreensoftware.com/test.php";
        ApplicationDescriptor ad = ApplicationDescriptor.currentApplicationDescriptor();
        byte serverType = PushApplicationDescriptor.SERVER_TYPE_BPAS;

        PushApplicationDescriptor pad = new PushApplicationDescriptor( appId, port, bpasUrl, serverType, ad );

        // check whether already registered or registration pending
        PushApplicationStatus pushApplicationStatus = PushApplicationRegistry.getStatus( pad );
        byte pasStatus = pushApplicationStatus.getStatus();
        if( pasStatus == PushApplicationStatus.STATUS_ACTIVE ) {
            // we already registered, update the statuses
            //Logger.log( "Already registered with BPAS" );
            return;
            
        } else if( pasStatus == PushApplicationStatus.STATUS_PENDING ) {
            // we already scheduled registration, wait for its result
            //Logger.log( "Registration with BPAS already scheduled");
        } else {
            // not registered yet, register
            //Logger.log( "Scheduled registration with BPAS");
            PushApplicationRegistry.registerApplication( pad );
            //setCurrentTransaction( tx );
            //tx.waitForNotification();
        }
        readRegistrationResult();
    }

    public void unregister() {
        // Push API unregisters the application right away,
        // so we don't wait for notifications
        PushApplicationRegistry.unregisterApplication();
        //Logger.log( "Un-registred from BPAS" );
    }
    
    private void readRegistrationResult() throws Exception {
        synchronized( txLock ) {
            if( currentRegistration != -1 ) {
                switch( currentRegistration ) {
                    case PushApplicationStatus.STATUS_ACTIVE:
                        // successful registration
                    	UiApplication.getUiApplication().invokeLater(new Runnable() {
                			public void run() {
                				Dialog.alert("Register client for push sukses");
                			}
                		});
                        return;
                    case PushApplicationStatus.STATUS_FAILED:
                    case PushApplicationStatus.STATUS_NOT_REGISTERED:
                    case PushApplicationStatus.STATUS_PENDING:
                        if( currentError != null ) {
                            throw new Exception( "Registration with BPAS failed, caused by " + currentError );
                        } else {
                            String error;
                            switch( currentReason ) {
                                case PushApplicationStatus.REASON_INVALID_PARAMETERS:
                                    error = "Registration with BPAS failed due to invalid parameters";
                                    break;
                                case PushApplicationStatus.REASON_NETWORK_ERROR:
                                    error = "Registration with BPAS failed due to network problems";
                                    break;
                                case PushApplicationStatus.REASON_REJECTED_BY_SERVER:
                                    error = "Registration with BPAS was rejected by server";
                                    break;
                                default:
                                    error = "Registration with BPAS failed";
                                    break;
                            }
                            throw new Exception( error );
                        }
    
                }
                currentRegistration = -1;
                currentReason = -1;
                currentError = null;
            }
        }
    }

    // **************** PushMessageListener implementation ********************

    public void startListening() {
        // start listening thread
        if( thread == null ) {
            thread = new MessageReadingThread();
            thread.start();
        }
    }

    public void stopListening() {
        if( thread != null ) {
            thread.stopRunning();
            thread = null;
        }
    }

    public boolean applicationCanQuit() {
        // 5.0+ API will start our application when something interesting happens
        // this means that the listener doesn't have to be constantly running in the background
        return true;
    }

    /**
     * Thread that processes incoming connections through {@link PushMessageReader}
     */
    private static class MessageReadingThread extends Thread {

        private Stack queue;
        private boolean running;

        public MessageReadingThread() {
            this.queue = new Stack();
            this.running = true;
        }

        public void enqueue( PushInputStream inputStream, StreamConnection conn ) {
            synchronized( queue ) {
                queue.insertElementAt( inputStream, 0 );
                queue.insertElementAt( conn, 0 );
                queue.notifyAll();
            }
        }

        public void run() {
            //Logger.log( "Listening for push messages through PushAPI" );
            try {
                PushInputStream inputStream;
                StreamConnection conn;
                while( running ) {
                    inputStream = null;
                    conn = null;
                    synchronized( queue ) {
                        if( queue.isEmpty() ) {
                            queue.wait();
                        } else {
                            inputStream = (PushInputStream) queue.pop();
                            conn = (StreamConnection) queue.pop();
                        }
                    }
                    if( inputStream != null ) {
                        PushMessageReader.process( inputStream, conn );
                    }
                }
            } catch( InterruptedException e ) {
            }
            //Logger.log( "Stopped listening for push messages" );
        }

        public void stopRunning() {
            running = false;
            synchronized( queue ) {
                queue.notifyAll();
            }
        }

    }
}
/*
//#endif
//#ifndef HANDHELD_VERSION_50
/*
public class PushLib50 {
}
//#endif
*/