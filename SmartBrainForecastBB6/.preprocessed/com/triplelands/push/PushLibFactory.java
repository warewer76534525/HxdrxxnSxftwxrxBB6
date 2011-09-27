
/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.triplelands.push;

public class PushLibFactory {

    private static Object lib;
    
    static {




        lib = new PushLib43();

    }

    public static BpasProtocol getBpasProtocol() {
        return (BpasProtocol) lib;
    }
    
//    public static UiApplication getUiApplication() {
//        return (UiApplication) lib;
//    }

    public static PushMessageListener getPushMessageListener() {
        return (PushMessageListener) lib;
    }
}
