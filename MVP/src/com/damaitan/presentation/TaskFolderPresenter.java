/**
 * 
 */
package com.damaitan.presentation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.ServiceException;
import com.damaitan.service.ModelManager;
import com.damaitan.service.OnTaskResult;

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
	public final static String TASK_FOLDER_KEY = "taskedit_folder";
	
	private TaskListSorter m_sorter = null;
	
	public TaskFolderPresenter(boolean isSetSortListener){
		m_sorter = new TaskListSorter();
	}
	
	
	public  TaskFolder getFolderByIndex(int index)throws ServiceException{
		try{
			return ModelManager.getInstance().getTaskFolder(index);
		}catch(Exception e){
			throw new ServiceException("TaskFolderHandler:getFolderByIndex erros, index: " + index,e);
		}
	}
	
	public int getFolderCount(){
		return ModelManager.getInstance().getFolders().size();
	}
	
	public int saveTask(int folderindex, Task task,boolean isNew)throws ServiceException{
		TaskFolder folder = getFolderByIndex(folderindex);
		if(task.getName() == null || task.getName().isEmpty() || task.getName().equals(Task.invalidStr)){
			return 1;// Parameter is wrong
		}
		ModelManager.getInstance().tag(task);
		if (isNew) {
			task.setId(ModelManager.getInstance().getNewTaskId(true));
			folder.addTask(task);
			OnTaskResult.getInstance().inform(
					OnTaskResult.ITaskListener.Type.add, folder, null, task);
			return 0;
		}
		if (folderindex != task.taskFolderId) {
			Task clone = task.clone();
			clone.taskFolderId = folderindex;
			boolean result = delete(folderindex, clone, false);
			TaskFolder changedTofolder = getFolderByIndex((int)(task.taskFolderId));
			changedTofolder.addTask(task);
			OnTaskResult.getInstance().inform(
					OnTaskResult.ITaskListener.Type.add, changedTofolder, null, task);
			return result ? 0 : 1;
		} 
		Task oldTask = folder.updateTask(task);
		if (oldTask == null)
			return 2;

		OnTaskResult.getInstance().inform(
				OnTaskResult.ITaskListener.Type.update, folder, oldTask, task);
		return 0;
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
		Task old = folder.removeTask(task);
		OnTaskResult.getInstance().inform(OnTaskResult.ITaskListener.Type.delete, folder, old, null);
		if(folderIndex == 0) return true;
		
		if(task.status == Task.Status.finished){
			includeChilds = true;
		}
		if(includeChilds){
			deleteChildTasks(folderIndex - 1, id, task.status);
		}else{
			for(Task someone : folder.getTasks()){
				if(someone.parentTaskId == id){
					someone.parentTaskId = Task.invalidId;
				}
			}
		}
		
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
		Task task = null;
		if(status == Task.Status.finished){
			for(int i = 0; i < folder.getFinishedTasks().size();i++){
				task = folder.getFinishedTasks().get(i);
				if(task.parentTaskId == parentTaskId){
					deleteChildTasks(folderIndex - 1,task.getId(), status);
					folder.removeTask(task);
					i--;
				}
			}
		}else{
			for(int i = 0; i < folder.getTasks().size();i++){
				task = folder.getTasks().get(i);
				if(task.parentTaskId == parentTaskId){
					deleteChildTasks(folderIndex - 1,task.getId(), status);
					folder.removeTask(task);
					i--;
				}
			}
		}
		return true;
	}
	
	public boolean finishTask(int folderIndex,Task task){
		if(task == null ) return false;
		if(task.status == Task.Status.finished) return false;
		TaskFolder folder;
		try {
			folder = getFolderByIndex(folderIndex);
		} catch (ServiceException e) {
			e.printStackTrace();
			return false;
		}
		Task old = folder.finishTask(task);
		Task repeat = old.clone();
		OnTaskResult.getInstance().inform(OnTaskResult.ITaskListener.Type.finish, folder, old, task);
		if(repeat != null && repeat.repeat == true){
			if(!(repeat.expired.trim().isEmpty())){
				SimpleDateFormat df = TaskListSorter.dateFormat();
				try {
					Date date = df.parse(repeat.expired);
					Date exDate = new Date();
					long msNow = exDate.getTime();
					long msEx = date.getTime(); 
					long n = 0;
					if(msEx < msNow){
						n = (msNow - msEx)/(24*60*60*1000*repeat.repeat_proid);
					}
					long temp = (n+1)*repeat.repeat_proid*24*60*60*1000;
					msEx = msEx + temp;
					temp = 24*60*60*1000;
					if(msEx < msNow) msEx = msEx + temp;				
					exDate.setTime(msEx);
					repeat.expired = df.format(exDate);
					saveTask((int)(repeat.taskFolderId),repeat,true);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				} 
			}
		}
		return true;
	}
	
	public boolean activateTask(int folderIndex, Task task){
		if(task == null ) return false;
		if(task.status == Task.Status.ongoing) return false;
		TaskFolder folder;
		try {
			folder = getFolderByIndex(folderIndex);
		} catch (ServiceException e) {
			e.printStackTrace();
			return false;
		}
		Task old = folder.activateTask(task);
		OnTaskResult.getInstance().inform(OnTaskResult.ITaskListener.Type.activate, folder, old, task);
		return true;
	}
	
	
	public TaskListSorter getSorter(){
		return m_sorter;
	}
	
}
