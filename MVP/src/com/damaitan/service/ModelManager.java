/**
 * 
 */
package com.damaitan.service;

import java.util.ArrayList;

import com.damaitan.access.IDataAccess;
import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;

/**
 * @author admin
 *
 */
final public class ModelManager {
	private ArrayList<TaskFolder> taskFolders;
	private ArrayList<Task> allTasks;
	private ArrayList<String> tags;
	
	public ModelManager(IDataAccess handler){
		this.taskFolders = handler.construct();
		allTasks = new ArrayList<Task>();
		tags = new ArrayList<String>();
		
		for(TaskFolder item : this.taskFolders){
			allTasks.addAll(item.getTasks());
		}
		
		for(Task item : allTasks){
			tag(item);
		}
		
	}
	
	private void tag(Task task){
		for(String item : task.getTags()){
			if(!tags.contains(item)){
				tags.add(item);
			}
		}
	}
	
	public void createFolder(){
		
	}
	
	public void deleteFolder(boolean includeTask){
		
	}
	
	public void changeFolderName(String name){
		
	}
	
	public void createTask(){
		
	}
	
	public void deleteTask(){
	
	}
	
	public void changeTask(){
		
	}
}
