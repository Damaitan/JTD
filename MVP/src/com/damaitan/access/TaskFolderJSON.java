/**
 * 
 */
package com.damaitan.access;

import org.json.JSONException;
import org.json.JSONObject;

import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.AccessException;

/**
 * @author admin
 *
 */
public class TaskFolderJSON {
	
	
	public static TaskFolder get(JSONObject json) throws AccessException{
		try {
			if(json.getInt(ModelJSON.Type) != 0){
				return null;
			}
			TaskFolder folder = new TaskFolder();
			folder.setName(json.getString(ModelJSON.Name));
			folder.setId(json.getLong(ModelJSON.Id));
			return folder;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			throw new AccessException("TaskFolderJSON : get() failed",e);
		}	
	}
	
	public static JSONObject getJSON(TaskFolder folder) throws AccessException{
		JSONObject obj = new JSONObject();
		try {
			obj.put(ModelJSON.Type, 0);
			obj.put(ModelJSON.Name, folder.getName());
			obj.put(ModelJSON.Id, folder.getId());
			return obj;
		} catch (JSONException e) {
			throw new AccessException("TaskFolderJSON : getJSON() failed",e);
		}
	}
}
