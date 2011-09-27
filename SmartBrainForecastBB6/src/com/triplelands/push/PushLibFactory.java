//#preprocess
/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.triplelands.push;

public class PushLibFactory {

    private static Object lib;
    
    static {
        //#ifdef HANDHELD_VERSION_50
        lib = new PushLib50();
        //#endif
        //#ifndef HANDHELD_VERSION_50
        lib = new PushLib43();
        //#endif
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
