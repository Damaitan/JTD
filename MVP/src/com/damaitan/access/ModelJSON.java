/**
 * 
 */
package com.damaitan.access;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.AccessException;

/**
 * @author admin
 *
 */
public class ModelJSON {
	public final static String Type = "type";
	public final static String Name = "name";
	public final static String Id = "id";
	
	public static ArrayList<TaskFolder> create(String content) throws AccessException{
		JSONTokener jsonParser = new JSONTokener(content);
		JSONArray jsonArray;
		int type = -1;
		ArrayList<TaskFolder> folders = new ArrayList<TaskFolder>();
		try {
			jsonArray = (JSONArray ) jsonParser.nextValue();
			if(jsonArray != null){
				JSONObject json;
				for(int i = 0 ; i < jsonArray.length();i++){
					json = jsonArray.getJSONObject(i);
					type = json.getInt(Type);
					switch (type) {
					case 0:
						TaskFolder folder = TaskFolderJSON.get(json);
						folders.add(folder);
						break;
					default:
						break;
					}
				}
			}
		} catch (JSONException e) {
			throw new AccessException("JsonFiler:construct() failed in JSON.",e);
		}
		return folders;
	}
	
	public static String ToJsonString(ArrayList<TaskFolder> folders)throws AccessException{
		JSONArray jsonFolders = new JSONArray();
		for(TaskFolder folder : folders){
			jsonFolders.put(TaskFolderJSON.getJSON(folder));
		}
		return jsonFolders.toString();
	}
}
