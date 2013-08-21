/**
 * 
 */
package com.damaitan.presentation;

import com.damaitan.datamodel.TaskFolder;
import com.damaitan.service.TaskFolderHandler;

/**
 * @author admin
 *
 */
public class TaskViewPresenter {
	private TaskFolder copiedfolder;
	
	public void setFolder(int index){
		TaskFolderHandler handler = new TaskFolderHandler();
		copiedfolder =  handler.getIndex(index).clone();
	}
	
	public String getViewName(){
		return copiedfolder.getName();
	}
	
	public TaskFolder getFolder(){
		return copiedfolder;
	}
	
	
	
	//public Task getTask
}
