package com.triplelands.utils;

import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.EventLogger;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.WLANInfo;

public class HttpUtils {
	
	public static String getBestConnectionSuffix(){
		String suffix = null;
        if (DeviceInfo.isSimulator()) {
                suffix = ";deviceside=true";
        } else if (CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS)) {
                suffix = ";deviceside=false";
        } else if ((WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED)
                        && RadioInfo.areWAFsSupported(RadioInfo.WAF_WLAN)) {
                suffix = ";interface=wifi";
        } else if (CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_BIS_B)) { 
                suffix = ";deviceside=false;ConnectionType=mds-public";
        } else {
                ServiceBook sb = ServiceBook.getSB();
                ServiceRecord[] records = sb.getRecords();
                String myuid = null;
                for (int i = 0; i < records.length; i++) {
                        ServiceRecord myRecord = records[i];
                        String cid, uid;
  
                        if (myRecord.isValid() && !myRecord.isDisabled()) {
                                cid = myRecord.getCid().toLowerCase();
                                uid = myRecord.getUid().toLowerCase();
                                // BIS
                                // Wap2.0
                                if (cid.indexOf("wptcp") != -1 && uid.indexOf("wifi") == -1
                                                && uid.indexOf("mms") == -1) {
                                        myuid = myRecord.getUid();
                                        break;
                                }
                        }
                }
                if (myuid != null) {
                        // WAP2 Connection
                        suffix = ";deviceside=true;ConnectionUID=" + myuid;
                } else {
                        suffix = ";deviceside=true";
                }
        }
        EventLogger.logEvent(0x9c805919833654d6L, suffix.getBytes(), EventLogger.INFORMATION);
        return suffix;
    }
}
