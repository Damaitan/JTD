/**
 * 
 */
package com.damaitan.service;

import java.util.ArrayList;

import com.damaitan.access.IDataAccess;
import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.AccessException;
import com.damaitan.exception.ServiceException;

/**
 * @author admin
 *
 */
final class ModelManager{
	private ArrayList<TaskFolder> taskFolders;
	private ArrayList<Task> allTasks = new ArrayList<Task>(); //Tasks are got from all task folders.
	private ArrayList<String> tags = new ArrayList<String>(); // Tags are got from all taks tags
	private long taskId = 0;
	private long taskFolderId = 0;
	
	private static ModelManager uniqueInstance = null;
	
	private ModelManager(){
		
	}
	public static ModelManager getInstance() {
		 if (uniqueInstance == null) {
			 uniqueInstance = new ModelManager();
		 }
		 return uniqueInstance;
	}
	
	public ArrayList<TaskFolder> getCopiedFolder(){
		if(taskFolders == null){
			return null;
		}
		ArrayList<TaskFolder> clone = (ArrayList<TaskFolder>)taskFolders.clone();
		return clone;
	}
	 
	
	
	private void tag(Task task){
		if(task.getTags() == null)
			return;
		for(String item : task.getTags()){
			if(!tags.contains(item)){
				tags.add(item);
			}
		}
	}

	
	public void construct(IDataAccess handler) throws ServiceException {
		try {
			this.taskFolders = handler.construct();
		} catch (AccessException e) {
			throw new ServiceException("construct()",e);
		}
		for(TaskFolder item : this.taskFolders){
			if(item.getTasks() != null){
				allTasks.addAll(item.getTasks());
			}
			if(item.getId() > taskFolderId){
				taskFolderId = item.getId();
			}
		}
		for(Task item : allTasks){
			tag(item);
			if(item.getId() > taskId){
				taskId = item.getId();
			}
		}
	}

	/*
	private TaskFolder findFolderById(long id){
		if(this.taskFolders == null) return null;
		for(int i = 0; i < this.taskFolders.size();i++){
			if(this.taskFolders.get(i).getId() == id){
				return this.taskFolders.get(i);
			}
		}
		return null;
	}
	
	private int findFolderIndexById(long id){
		if(this.taskFolders == null) return -1;
		for(int i = 0; i < this.taskFolders.size();i++){
			if(this.taskFolders.get(i).getId() == id){
				return i;
			}
		}
		return -1;
	}

	@Override
	public TaskFolder createFolder(String name) throws ServiceException {
		taskFolderId++;
		TaskFolder folder = new TaskFolder(taskFolderId, name);
		taskFolders.add(folder);
		return folder;
	}

	@Override
	public TaskFolder modifyFolder(long id, String newName)
			throws ServiceException {
		TaskFolder folder = findFolderById(id);
		if(folder != null){
			folder.setName(newName);
		}
		return folder;
	}

	@Override
	public TaskFolder deleteFolder(long id, boolean includeTask)
			throws ServiceException {
		int index = findFolderIndexById(id);
		TaskFolder folder = this.taskFolders.get(index);
		if(index != -1){
			this.taskFolders.remove(index);
		}
		if(includeTask){
			//Not finished yet
		}else{
			
		}
		return folder;
	}*/
}
