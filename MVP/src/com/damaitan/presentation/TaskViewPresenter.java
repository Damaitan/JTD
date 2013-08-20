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
	private TaskFolder folder;
	
	public void setFolder(int index){
		TaskFolderHandler handler = new TaskFolderHandler();
		folder =  handler.getIndex(index);
	}
	
	public String getViewName(){
		return folder.getName();
	}
	
	//public Task getTask
}
