/**
 * 
 */
package com.damaitan.presentation;

import java.util.ArrayList;

import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.ServiceException;
import com.damaitan.service.ModelManager;

public class TaskFolderPresenter {
	public final static String KEY_TASK = "TASK";
	public final static String NAME_KEY = "taskedit_name";
	public final static String URGENT_KEY = "taskedit_urgent";
	public final static String TAG_KEY = "taskedit_tag";
	public final static String PRIORITY_KEY = "taskedit_priority";
	public final static String NOTE_KEY = "taskedit_note";
	public final static String PARENET_KEY = "taskedit_parent";
	public final static String REPEAT_KEY = "taskedit_repeat";
	public final static String EXPIRE_KEY = "taskedit_expire";
	public final static String REPEAT_PEROID_KEY = "taskedit_repeat_peroid";
	
	public TaskFolderPresenter(OnTaskResult.ITaskListener listener){
		if(listener != null){
			OnTaskResult.getInstance().join(listener);
		}
	}
	
	public  TaskFolder getFolderByIndex(int index)throws ServiceException{
		try{
			return ModelManager.getInstance().getTaskFolder(index);
		}catch(Exception e){
			throw new ServiceException("TaskFolderHandler:getFolderByIndex erros, index: " + index,e);
		}
	}
	
	public int saveTask(int folderindex, Task task,boolean isNew)throws ServiceException{
		TaskFolder folder = getFolderByIndex(folderindex);
		int resultCode = 0;
		if(task.getName() == null || task.getName().equals("null")){
			resultCode = 1;// Parameter is wrong
			return resultCode;
		}
		if(isNew){
			task.setId(ModelManager.getInstance().getNewTaskId(true));
			folder.addTask(task);
			ModelManager.getInstance().tag(task);
			OnTaskResult.getInstance().inform(OnTaskResult.ITaskListener.Type.add, folder, task);
		}else{
			folder.updateTask(task);
			if(folderindex != task.taskFolderId){
				TaskFolder changeTo = getFolderByIndex( (int)task.taskFolderId);
				changeTo.addTask(task);
			}
			OnTaskResult.getInstance().inform(OnTaskResult.ITaskListener.Type.add, folder, task);
		}
		return resultCode;
	}
	
	public ArrayList<String> getTags(){
		return ModelManager.getInstance().getTags();
	}	
	
	public boolean delete(int folderIndex, Task task, boolean includeChilds){
		TaskFolder folder;
		try {
			folder = getFolderByIndex(folderIndex);
		} catch (ServiceException e) {
			e.printStackTrace();
			return false;
		}
		long id = task.getId();
		folder.removeTask(task);
		if(task.status == Task.Status.finished){
			includeChilds = true;
		}
		if(includeChilds){
			if(folderIndex == 0) return true;
			deleteChildTasks(folderIndex - 1, id, task.status);
		}else{
			for(Task someone : folder.getTasks()){
				if(someone.parentTaskId == id){
					someone.parentTaskId = Task.invalidId;
				}
			}
		}
		OnTaskResult.getInstance().inform(OnTaskResult.ITaskListener.Type.delete, folder, task);
		return true;
	}
	
	private boolean deleteChildTasks(int folderIndex, long parentTaskId, Task.Status status){
		if(folderIndex == 0) return true;
		TaskFolder folder;
		try {
			folder = getFolderByIndex(folderIndex);
		} catch (ServiceException e) {
			e.printStackTrace();
			return false;
		}
		if(status == Task.Status.finished){
			for(Task task : folder.getFinishedTasks()){
				if(task.parentTaskId == parentTaskId){
					deleteChildTasks(folderIndex - 1,task.getId(), status);
					folder.removeTask(task);
				}
			}
		}else{
			for(Task task : folder.getTasks()){
				if(task.parentTaskId == parentTaskId){
					deleteChildTasks(folderIndex - 1,task.getId(), status);
					folder.removeTask(task);
				}
			}
		}
		return true;
	}
	
	public boolean finishTask(int folderIndex,Task task){
		if(task == null ) return false;
		if(task.status != Task.Status.finished) return false;
		TaskFolder folder;
		try {
			folder = getFolderByIndex(folderIndex);
		} catch (ServiceException e) {
			e.printStackTrace();
			return false;
		}
		folder.finishTask(task);
		OnTaskResult.getInstance().inform(OnTaskResult.ITaskListener.Type.finish, folder, task);
		return true;
	}
	
	public boolean activateTask(int folderIndex, Task task){
		if(task == null ) return false;
		if(task.status != Task.Status.ongoing) return false;
		TaskFolder folder;
		try {
			folder = getFolderByIndex(folderIndex);
		} catch (ServiceException e) {
			e.printStackTrace();
			return false;
		}
		folder.activateTask(task);
		OnTaskResult.getInstance().inform(OnTaskResult.ITaskListener.Type.activate, folder, task);
		return true;
	}
	
}
