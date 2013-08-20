/**
 * 
 */
package com.damaitan.service;

import java.util.ArrayList;

import com.damaitan.datamodel.ModelStruct;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.ServiceException;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author admin
 *
 */
public class TaskFolderHandler {
	
	public TaskFolder create(String name) throws ServiceException{
		return null;
	}
	public void modify(long id, String newName) throws ServiceException{
		
	}
	
	public void delete(long id, boolean includeTask) throws ServiceException{

	}
	
	public ArrayList<TaskFolder> getCopied() throws ServiceException{ // it's just copy from service
		ModelManager dm = ModelManager.getInstance();
		ArrayList<TaskFolder> clone = dm.getCopiedFolder();
		if(clone == null)
			throw new ServiceException("TaskFolderHandler:getCopied - Get NULL of Folders",new NullPointerException());
		return clone;
	}
	
	
}
