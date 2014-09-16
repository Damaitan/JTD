/**
 * 
 */
package com.damaitan.service;

import java.util.ArrayList;

import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.ServiceException;


/**
 * @author admin
 *
 */
public class TaskFolderHandler {
	
	//@SuppressWarnings("unchecked")
	/*public static ArrayList<TaskFolder> getCopied() throws ServiceException{ // it's just copy from service
		ModelManager dm = ModelManager.getInstance();
		
		ArrayList<TaskFolder> folders = dm.getFolders();
		if(folders == null)
			throw new ServiceException("TaskFolderHandler:getCopied - Get NULL of Folders",new NullPointerException());
		return (ArrayList<TaskFolder>)(folders.clone());
	}*/
	
	public static ArrayList<TaskFolder> getFolders() throws ServiceException{ // it's just copy from service
		ModelManager dm = ModelManager.getInstance();
		
		ArrayList<TaskFolder> folders = dm.getFolders();
		if(folders == null)
			throw new ServiceException("TaskFolderHandler:getFolders - Get NULL of Folders",new NullPointerException());
		return folders;
	}
	
	public static TaskFolder getFolderByIndex(int index)throws ServiceException{
		try{
			return ModelManager.getInstance().getTaskFolder(index);
		}catch(Exception e){
			throw new ServiceException("TaskFolderHandler:getFolderByIndex erros, index: " + index,e);
		}
	}
	
	public static int saveTask(int folderindex, Task task,boolean isNew)throws ServiceException{
		TaskFolder folder = getFolderByIndex(folderindex);
		int resultCode = 0;
		if(task.getName() == null || task.getName().equals("null")){
			resultCode = 1;// Parameter is wrong
			return resultCode;
		}
		
		if(isNew){
			//Cross Validation first
			task.setId(ModelManager.getInstance().getNewTaskId(true));
			folder.addTask(task);
			ModelManager.getInstance().tag(task);
		}
		
		return resultCode;
	}
	
	public static ArrayList<String> getTags(){
		return ModelManager.getInstance().getTags();
	}
	
	
	
	
	
}
