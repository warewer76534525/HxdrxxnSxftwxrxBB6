package com.triplelands.datastore;

import java.util.Hashtable;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

public class DataStorer {

	private PersistentObject persistentObject;
	private Hashtable hashtable;

	public DataStorer() {
		persistentObject = PersistentStore.getPersistentObject(0xe413edef6f11b8c8L);		
		if(persistentObject.getContents() == null){
			hashtable = new Hashtable();
			persistentObject.setContents(hashtable);
		} else {
			hashtable = (Hashtable)persistentObject.getContents();
		}
	}
	
	public void addNewUnreadSignalId(String ids){
		String idlist = "";
		if(hashtable.containsKey("unread_id")){
			idlist = (String) hashtable.get("unread_id");
			hashtable.remove("unread_id");
		}
		hashtable.put("unread_id",ids + idlist);
		persistentObject.commit();
	}
	
	public String getUnreadSignalIds(){
		if(hashtable.containsKey("unread_id")){
			return (String)hashtable.get("unread_id");
		} else {
			return "";
		}
	}
	
	public void emptyUnreadSignals(){
		hashtable.remove("unread_id");
		persistentObject.commit();
	}
	
	public void setRegisteredForPush(boolean reg){
		if(hashtable.containsKey("push_registered")){
			hashtable.remove("push_registered");
		}
		hashtable.put("push_registered", "" + reg);
		persistentObject.commit();
	}
	
	public boolean isRegisteredForPush(){
		String reg = (String) hashtable.get("push_registered");
		if(reg == null) return false;
		return (reg.equals("true")) ? true : false;
	}
	
	public boolean isRegistered() {
		return hashtable.get("email") != null;
	}
	
	public void addData(String key, String value) {
		if(!hashtable.containsKey(key)){
			hashtable.put(key, value);
		} else {
			if(!hashtable.get(key).equals(value)){
				hashtable.remove(key);
				hashtable.put(key, value);
			}
		}
		persistentObject.commit();
	}
	
	public String getData(String key) {
		if(hashtable.containsKey(key)){
			return (String)hashtable.get(key);
		} else {
			return "";
		}
	}
	
	public boolean isLogedIn(){
		return (hashtable.get("session") != null);
	}
	
	public void logout(){
		hashtable.remove("session");
		persistentObject.commit();
	}
	
	public void clear(){
		hashtable.clear();
		hashtable = null;
		persistentObject.commit();
	}
}
