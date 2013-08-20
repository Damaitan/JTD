/**
 * 
 */
package com.damaitan.service;

import java.util.ArrayList;

import com.damaitan.datamodel.ModelStruct;
import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.AccessException;

/**
 * @author admin
 *
 */
public class AccessMock{

	enum MockType{
		normal,
		exception,
		Null
	}
	private MockType mockType = MockType.normal;
	public void setType(MockType type){
		this.mockType = type;
	}
	
	public ModelStruct construct() throws AccessException {
		if(mockType == MockType.normal){
			ModelStruct modelStruct = new ModelStruct();
			ArrayList<TaskFolder> folders = new ArrayList<TaskFolder>();
			for(int i = 0; i < 2 ; i++){
				TaskFolder folder = new TaskFolder(i,String.valueOf(i));
				folders.add(folder);
				for(int j = 0;j < 2;j++){
					Task task = new Task(i*2 + j, String.valueOf(i*2 + j));
					task.setTaskFolder(folder);
				}
			}
			modelStruct.setFolders(folders);
			return modelStruct;
		} else if(mockType == MockType.exception){
			throw new AccessException();
		} else{
			throw new AccessException();
		}
		
	}

	
}
