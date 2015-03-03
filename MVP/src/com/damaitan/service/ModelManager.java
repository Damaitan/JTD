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
	private ArrayList<String> tags = new ArrayList<String>(); // Tags are got from all taks tags
	private long taskId = 0;
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
			if(item.trim().isEmpty())continue;
			if(!tags.contains(item.trim())){
				tags.add(item.trim());
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
		for(TaskFolder folder : modelStruct.getFolders()){
			for(Task item : folder.getTasks()){
				tag(item);
				if(item.getId() > taskId){
					taskId = item.getId();
				}
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
	
}
