/**
 * 
 */
package com.damaitan.service;

import java.util.ArrayList;
import com.damaitan.datamodel.CommonString;
import com.damaitan.datamodel.ModelStruct;
import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.ServiceException;

public final class ModelManager{
	
	private ModelStruct modelStruct = new ModelStruct();
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

	public String initJsonString(){
		String content[] = CommonString.InitJsonString;
		ArrayList<TaskFolder> folders = new ArrayList<TaskFolder>();
		for(int i = 0; i< content.length; i++){
			TaskFolder folder = new TaskFolder();
			folder.setId(i);
			folder.setName(content[i]);
			folders.add(folder);
		}
		ModelStruct modelStruct = new ModelStruct();
		modelStruct.setFolders(folders);
		return new GsonHelper().jsonString(modelStruct);
	}
	
	public String JsonString(){
		return new GsonHelper().jsonString(modelStruct);
	}
	
	public ArrayList<TaskFolder> getFolders(){
		return modelStruct.getFolders();

	}
	
	public void tag(Task task){
		if(task.tags == null)
			return;
		for(String item : task.tags.split(Task.TAGSPLITTER)){
			if(item.trim().equalsIgnoreCase(""))continue;
			if(!tags.contains(item)){
				tags.add(item);
			}
		}
	}

	public ArrayList<String> getTags(){
		return this.tags;
	}
	
	public void construct(String json) throws ServiceException {
		this.modelStruct = new GsonHelper().fromJson(json, ModelStruct.class);
		for(String str : CommonString.InitTag){
			tags.add(str);
		}
		for(TaskFolder item : modelStruct.getFolders()){
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
	public TaskFolder getTaskFolder(int index){
		return modelStruct.getFolders().get(index);
	}
	public long getNewTaskId(boolean updateId){
		if(updateId){
			taskId++;
			return taskId;
		}
		return taskId+1;
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
