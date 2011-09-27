/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.triplelands.push;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connection;

import net.rim.device.api.io.http.PushInputStream;
import net.rim.device.api.system.Application;

/**
 * Small utilities for common usage in the push sample application
 */
public class PushUtils {

    /**
     * Declines push message with given reason
     */
    public static void decline( PushInputStream pis, int reason ) {
        try {
            pis.decline( reason );
        } catch( IOException e ) {
        }
    }

    /**
     * Safely closes connection and streams
     */
    public static void close( Connection conn, InputStream is, OutputStream os ) {
        if( os != null ) {
            try {
                os.close();
            } catch( IOException e ) {
            }
        }
        if( is != null ) {
            try {
                is.close();
            } catch( IOException e ) {
            }
        }
        if( conn != null ) {
            try {
                conn.close();
            } catch( IOException e ) {
            }
        }
    }

    public static void runOnEventThread( Runnable r ) {
        if( Application.isEventDispatchThread() ) {
            r.run();
        } else {
            Application.getApplication().invokeLater( r );
        }
    }
}
