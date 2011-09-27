package com.triplelands.push;

import javax.microedition.io.Connection;

import net.rim.device.api.io.http.HttpServerConnection;
import net.rim.device.api.io.http.PushInputStream;
import net.rim.device.api.util.Arrays;

import com.triplelands.datastore.DataStorer;
import com.triplelands.main.MainApp;
import com.triplelands.utils.DataProcessor;
import com.triplelands.utils.NotificationUtils;

public class PushMessageReader {
    // HTTP header property that carries unique push message ID
    private static final String MESSAGE_ID_HEADER = "Push-Hisoft-ID";
    private static final String MESSAGE_TYPE_TEXT = "text";
    private static final int MESSAGE_ID_HISTORY_LENGTH = 10;
    private static String[] messageIdHistory = new String[ MESSAGE_ID_HISTORY_LENGTH ];
    private static byte historyIndex;

    private static byte[] buffer = new byte[ 15 * 1024 ];

    public static void process( PushInputStream pis, Connection conn ) {
        try {
            HttpServerConnection httpConn;
            if( conn instanceof HttpServerConnection ) {
                httpConn = (HttpServerConnection) conn;
            } else {
                throw new IllegalArgumentException( "Can not process non-http pushes, expected HttpServerConnection but have "
                        + conn.getClass().getName() );
            }

            String msgId = httpConn.getHeaderField( MESSAGE_ID_HEADER );
            String msgType = httpConn.getType();
            
            if( !alreadyReceived( msgId ) ) {
                byte[] binaryData;
                
                if(msgId == null) {
                    msgId = String.valueOf( System.currentTimeMillis() );
                }
                
                if( msgType.indexOf( MESSAGE_TYPE_TEXT ) >= 0 ) {
        				int size = pis.read( buffer );
                        binaryData = new byte[size];
                        System.arraycopy( buffer, 0, binaryData, 0, size );
                        
                        String message = new String(binaryData);
        				DataProcessor processor = new DataProcessor();
                        DataStorer storer = new DataStorer();
                        
                        //add unread signals id
                        storer.addNewUnreadSignalId(processor.getIdsForPushMessage(message));
                        
                        //here notify if there is new message
                        NotificationUtils.notify(1, processor.getCategoriesForPushReview(message));                        
                        
                        MainApp.PlaySound();
                }
            }
        } catch( Exception e ) {
        	e.printStackTrace();
        	System.out.println(e.getMessage());
        } finally {
            PushUtils.close( conn, pis, null );
        }
    }

    /**
     * Check whether the message with this ID has been already received.
     */
    private static boolean alreadyReceived( String id ) {
        if( id == null ) {
            return false;
        }
        
        if( Arrays.contains( messageIdHistory, id ) ) {
            return true;
        }
        
        // new ID, append to the history (oldest element will be eliminated)
        messageIdHistory[ historyIndex++ ] = id;
        if( historyIndex >= MESSAGE_ID_HISTORY_LENGTH ) {
            historyIndex = 0;
        }
        return false;
    }

}
