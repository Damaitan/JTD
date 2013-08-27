/**
 * 
 */
package com.damaitan.presentation;

import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.service.TaskFolderHandler;

/**
 * @author admin
 *
 */
public class TaskViewPresenter {
	private TaskFolder copiedfolder;
	private TaskFolderHandler handler;
	
	public void setFolder(int index){
		handler = new TaskFolderHandler();
		copiedfolder =  handler.getIndex(index).clone();
	}
	
	public String getViewName(){
		return copiedfolder.getName();
	}
	
	public TaskFolder getFolder(){
		return copiedfolder;
	}
	

	public static String toJson(Object obj){
		return TaskFolderHandler.toJson(obj);
	}
	
	public static <T> T fromJson(String json,Class<T> classOfT){
		return TaskFolderHandler.fromJson(json, classOfT);
	}
	
	
}
