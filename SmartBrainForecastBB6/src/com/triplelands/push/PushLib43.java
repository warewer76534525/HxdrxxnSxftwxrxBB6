/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.triplelands.push;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.ServerSocketConnection;

import net.rim.device.api.io.http.HttpServerConnection;
import net.rim.device.api.io.http.MDSPushInputStream;
import net.rim.device.api.io.http.PushInputStream;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiEngine;

import com.triplelands.utils.Constants;

/**
 * Library that is used on 4.3+ BlackBerry devices
 * 
 */
public class PushLib43 implements BpasProtocol, PushMessageListener {

    private MessageReadingThread thread;
    // ********* PushMessageListener implementation ********

    public boolean applicationCanQuit() {
        // the application may quit if we are not listening for push messages
        return thread == null;
    }
    
    public void startListening() {
        // start listening thread
    	System.out.println("start listening....");
        if( thread == null ) {
        	AppLog.getInstance().appendLog("Thread = null. Start listening thread");
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

    /**
     * Thread that processes incoming connections through {@link PushMessageReader}
     */
    private static class MessageReadingThread extends Thread {

        private boolean running;
        private ServerSocketConnection socket;
        private HttpServerConnection conn;
        private InputStream inputStream;
        private PushInputStream pushInputStream;
        
        public MessageReadingThread() {
            this.running = true;
        }

        public void run() {
        	AppLog.getInstance().appendLog("Running thread.");
            String url = "http://:" + Constants.PUSH_PORT + ";deviceside=false;ConnectionType=mds-public";

            try {
                socket = (ServerSocketConnection) Connector.open( url );
            } catch( final IOException ex ) {
                // can't open the port, probably taken by another application
                onListenError( ex );
            }

            while( running ) {
                try {
                    Object o = socket.acceptAndOpen();
                    conn = (HttpServerConnection) o;
                    inputStream = conn.openInputStream();
                    pushInputStream = new MDSPushInputStream( conn, inputStream );
                    PushMessageReader.process( pushInputStream, conn);
                } catch( Exception e ) {
                    if( running ) {
                        running = false;
                    }
                } finally {
                    PushUtils.close( conn, pushInputStream, null );
                }
            }
        }

        public void stopRunning() {
            running = false;
            PushUtils.close( socket, null, null );
        }

        private void onListenError( Exception ex ) {
        	ex.printStackTrace();
        }
    }

	public void register() throws Exception {
	}

	public void unregister() throws Exception {		
	}
}
