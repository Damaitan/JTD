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
	
	private ModelStruct m_modelStruct = new ModelStruct();
	private ArrayList<String> m_tags = new ArrayList<String>(); // Tags are got from all taks tags
	private long m_taskId = 0;
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
		return new GsonHelper().jsonString(m_modelStruct);
	}
	
	public ArrayList<TaskFolder> getFolders(){
		return m_modelStruct.getFolders();

	}
	
	public void tag(Task task){
		if(task.tags == null) return;
		if(task.tags.trim().isEmpty()) return;
		for(String item : task.tags.trim().split(Task.TAGSPLITTER)){
			if(item.trim().isEmpty())continue;
			if(!m_tags.contains(item.trim())){
				m_tags.add(item.trim());
			}
		}
	}

	public ArrayList<String> getTags(){
		return this.m_tags;
	}
	
	public void construct(String json) throws ServiceException {
		this.m_modelStruct = new GsonHelper().fromJson(json, ModelStruct.class);
		m_tags.clear();
		for(String str : CommonString.InitTag){
			m_tags.add(str);
		}
		for(TaskFolder folder : m_modelStruct.getFolders()){
			for(Task item : folder.getTasks()){
				tag(item);
				if(item.getId() > m_taskId){
					m_taskId = item.getId();
				}
			}
		}
		
	}
	public TaskFolder getTaskFolder(int index){
		return m_modelStruct.getFolders().get(index);
	}
	public long getNewTaskId(boolean updateId){
		if(updateId){
			m_taskId++;
			return m_taskId;
		}
		return m_taskId+1;
	}
	
}
