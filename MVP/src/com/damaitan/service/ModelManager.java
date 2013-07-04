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
final public class ModelManager implements IModelHandler{
	private ArrayList<TaskFolder> taskFolders;
	private ArrayList<Task> allTasks; //Tasks are got from all task folders.
	private ArrayList<String> tags;
	
	public ModelManager(){
		
	}
	
	private void tag(Task task){
		for(String item : task.getTags()){
			if(!tags.contains(item)){
				tags.add(item);
			}
		}
	}

	@Override
	public void construct(IDataAccess handler) throws ServiceException {
		try {
			this.taskFolders = handler.construct();
		} catch (AccessException e) {
			e.printStackTrace();
			throw new ServiceException("construct()",e);
		}
		allTasks = new ArrayList<Task>();
		tags = new ArrayList<String>();
		
		for(TaskFolder item : this.taskFolders){
			if(item.getTasks() != null){
				allTasks.addAll(item.getTasks());
			}
		}
		for(Task item : allTasks){
			tag(item);
		}
		
	}

	@Override
	public TaskFolder createFolder(String name) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskFolder modifyFolder(int id, String newName)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskFolder deleteFolder(int id, boolean includeTask)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}
}
