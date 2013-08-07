package com.damaitan.mobileUI;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.damaitan.access.IDataAccess;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.AccessException;

/**
 * 
 */

/**
 * @author admin
 *
 */
public class JsonFiler implements IDataAccess {
	private FileInputStream _stream;
	private final static String Type = "type";
	private final static String Name = "name";
	private final static String Id = "id";

	public JsonFiler(FileInputStream stream){
		_stream = stream;
	}
	/* (non-Javadoc)
	 * @see com.damaitan.access.IDataAccess#construct()
	 */
	@Override
	public ArrayList<TaskFolder> construct() throws AccessException {
		String content;
		try {
			byte[] bytes = new byte[1024];
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			while (this._stream.read(bytes) != -1) {
				arrayOutputStream.write(bytes, 0, bytes.length);
			}
			arrayOutputStream.close();
	        this._stream.close();
	        content = new String(arrayOutputStream.toByteArray()); 
		} catch (IOException e) {
			throw new AccessException("JsonFiler:construct() failed in File reading.",e);
		}
        
		JSONTokener jsonParser = new JSONTokener(content);
		JSONObject json;
		int type = -1;
		ArrayList<TaskFolder> folders = new ArrayList<TaskFolder>();
		try {
			json = (JSONObject) jsonParser.nextValue();
			while (json != null) {
				type = json.getInt(Type);
				switch (type) {
				case 0:
					TaskFolder folder = new TaskFolder();
					folder.setName(json.getString(Name));
					folder.setId(json.getLong(Id));
					folders.add(folder);
					break;
				default:
					break;
				}
			}
		} catch (JSONException e) {
			throw new AccessException("JsonFiler:construct() failed in JSON.",e);
		}
		return folders;
	}
	
	/* (non-Javadoc)
	 * @see com.damaitan.access.IDataAccess#getSystem()
	 */
	@Override
	public System getSystem() {
		// TODO Auto-generated method stub
		return null;
	}

}
