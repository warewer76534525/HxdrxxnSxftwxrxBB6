package com.triplelands.utils;

import java.util.Vector;

import com.triplelands.model.Category;
import com.triplelands.model.Comment;
import com.triplelands.model.KeyValuePair;
import com.triplelands.model.News;
import com.triplelands.model.Signal;
import com.triplelands.utils.json.JSONArray;
import com.triplelands.utils.json.JSONException;
import com.triplelands.utils.json.JSONObject;

public class DataProcessor {
	
	public String getIdsForPushMessage(String json){
		try {
			StringBuffer sb = new StringBuffer();
			JSONObject obj = new JSONObject(json);
			JSONArray arr = new JSONArray(obj.optString("id"));
			for (int i = 0; i < arr.length(); i++) {
				sb.append(arr.optString(i) + ";");
			}
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}
	
	public String getCategoriesForPushReview(String json){
		StringBuffer sb = new StringBuffer();
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray arr = new JSONArray(obj.getString("cat"));
			for (int i = 0; i < arr.length(); i++) {
				sb.append(arr.optString(i));
				if(i == arr.length() - 1){
					sb.append(".");
				} else {
					sb.append(", ");
				}
			}
		} catch (Exception e) {
			return "";
		}
		return sb.toString();
	}
	
	public Vector getComments(String json){
		try {
			Vector comments = new Vector();
			JSONObject obj = new JSONObject(json);
			JSONArray arr = new JSONArray(obj.getString("comment"));
			for (int i = 0; i < arr.length(); i++) {
				JSONObject object = arr.getJSONObject(i);
				Comment comment = convertToComment(object);
				comments.addElement(comment);
			}
			return comments;
		} catch (JSONException e) {
			return null;
		}
	}
	
	public Signal getSignalDetail(String fullJson){
		try {
			JSONObject obj = new JSONObject(fullJson);
			JSONObject object = new JSONObject(obj.getString("signals"));
			Signal signal = convertToSignal(object);
			return signal;
		} catch (JSONException e) {
			return null;
		}
	}
	
	public Vector getCategories(String fullJson){
		try {
			Vector categories = new Vector();
			JSONObject obj = new JSONObject(fullJson);
			JSONArray arr = obj.optJSONArray("groups");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject object = arr.getJSONObject(i);
				Category category = convertToCategory(object);
				categories.addElement(category);
			}
			return categories;
		} catch (JSONException e) {
			return null;
		}
	}

	public String getMetaSignal(String fullJson){
		try {
			JSONObject obj = new JSONObject(fullJson);
			return obj.optString("metasignal");
		} catch (JSONException e) {
			return null;
		}
	}
	
	public int getType(String fullJson){
		try {
			JSONObject obj = new JSONObject(fullJson);
			return obj.getInt("typeid");
		} catch (JSONException e) {
			return 9;
		}
	}
	
//	public String getImageUrl(String fullJson){
//		try {
//			JSONObject obj = new JSONObject(fullJson);
//			return obj.getString("image");
//		} catch (JSONException e) {
//			return null;
//		}
//	}
	
	public String[] getImageUrls(String fullJson){
		try {
			JSONObject obj = new JSONObject(fullJson);
			JSONArray arrImg = obj.getJSONArray("image");
			String[] imgs = new String[arrImg.length()];
			for (int i = 0; i < arrImg.length(); i++) {
				imgs[i] = arrImg.optString(i);
			}
			return imgs;
		} catch (JSONException e) {
			return null;
		}
	}
	
	private Category convertToCategory(JSONObject object) {
		try {
			int id = object.getInt("id");
			String name = object.optString("name");
			
			Vector signals = new Vector();
			JSONArray arr = object.optJSONArray("signals");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				Signal signal = convertToSignal(obj);
				signals.addElement(signal);
			}
			return new Category(id, name, signals);
		} catch (Exception e) {
			return null;
		}
	}
	
	public Signal convertToSignal(JSONObject object){
		Signal signal;
		try {
			signal = new Signal(
					object.getInt("id"),
					object.optString("time"),
					object.optString("category"), 
					object.optString("method"), 
					object.optString("pattern"), 
					object.optString("symbol"), 
					object.optString("direction"), 
					object.getDouble("probability"), 
					object.getInt("comment_count"));
		} catch (JSONException e) {
			return null;
		}
		return signal;
	}
	
	public Comment convertToComment(JSONObject object){
		Comment comment;
		try {
			comment = new Comment(object.getInt("id"),
					object.optString("time"), 
					object.optString("name"),
					object.optString("content"));
		} catch (JSONException e) {
			return null;
		}
		return comment;
	}

	public Vector getNewsList(String json) {
		try {
			Vector newsList = new Vector();
			JSONObject obj = new JSONObject(json);
			JSONArray arr = new JSONArray(obj.getString("news"));
			for (int i = 0; i < arr.length(); i++) {
				JSONObject object = arr.getJSONObject(i);
				News news = convertToNews(object);
				newsList.addElement(news);
			}
			return newsList;
		} catch (JSONException e) {
			return null;
		}
	}
	
	public News getNews(String fullJson){
		try {
			JSONObject obj = new JSONObject(fullJson);
			JSONObject object = new JSONObject(obj.getString("news"));
			News news = convertToNews(object);
			return news;
		} catch (JSONException e) {
			return null;
		}
	}

	public News convertToNews(JSONObject object) {
		News news;
		try {
			news = new News(
					object.getInt("id"),
					object.optString("title"),
					object.optString("time"),
					object.optString("content"));
		} catch (JSONException e) {
			return null;
		}
		return news;
	}
	
	public String getResponseStatus(String json){
		JSONObject obj;
		try {
			obj = new JSONObject(json);
		} catch (JSONException e) {
			return null;
		}
		return obj.optString("status");
	}
	
	public String getResponseMessage(String json){
		JSONObject obj;
		try {
			obj = new JSONObject(json);
		} catch (JSONException e) {
			return null;
		}
		return obj.optString("message");
	}
	
	public Vector getKeyValueArray(String json){
		try {
			Vector data = new Vector();
			JSONObject object = new JSONObject(json);
			JSONArray arr = object.names();
			for (int i = 0; i < arr.length(); i++) {
				data.addElement(new KeyValuePair(arr.getString(i), object.optString(arr.getString(i))));
			}
			return data;
		} catch (Exception e) {
			return null;
		}
	}
	
	public String getEmail(String json){
		try {
			JSONObject obj = new JSONObject(json);
			return obj.getString("email");
		} catch (Exception e) {
			return null;
		}
	}
	
	public String getSessionId(String json){
		try {
			JSONObject obj = new JSONObject(json);
			return obj.getString("session_id");
		} catch (Exception e) {
			return null;
		}
	}
	
}
