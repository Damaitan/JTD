/**
 * 
 */
package com.damaitan.datamodel;

import java.util.ArrayList;
import java.util.List;

import com.damaitan.datamodel.Task.Status;

/**
 * @author admin
 *
 */
public class TaskFolder extends Model {
	public final static long HideFolderId = 0;
	private ArrayList<Task> tasks = new ArrayList<Task>();
	private ArrayList<Task> finishedTasks = new ArrayList<Task>();
	private int m_numberForRemovedFinish = 0;
	
	public TaskFolder() {
		super();
	}
	
	public TaskFolder(long i, String name) {
		super(i,name);
	}
	
	public int getAllTaskNumber() {
		return m_numberForRemovedFinish + this.tasks.size() + this.finishedTasks.size();
	}
	
	public int getCompletedTaskNumber() {
		return m_numberForRemovedFinish + this.finishedTasks.size();
	}
	
	public void addTask(Task task) {
		task.taskFolderId = this.getId();
		this.tasks.add(task);
	}
	private int findTaskIndex(ArrayList<Task> tasks, long id){
		for(int i = 0;i < tasks.size(); i++){
			if(tasks.get(i).getId() == id){
				return i;
			}
		}
		return -1;
	}
	
	public void updateTask(Task task){
		int index = findTaskIndex(this.tasks, task.getId());
		if(task.parentTaskId != this.getId()){
			this.tasks.remove(index);
		}else{
			this.tasks.add(index,task);
		}
	}
	
	public void finishTask(Task task){
		removeTask(task, false);
		task.status = Status.finished;
		this.finishedTasks.add(task);
	}
	
	public void activateTask(Task task){
		removeTask(task, false);
		task.status = Status.ongoing;
		this.tasks.add(task);
	}
	
	public boolean removeTask(Task task){
		return removeTask(task, true);
	}
	
	public boolean removeTask(Task task, boolean counting){
		if(task == null ) return false;
		if(task.status == Status.finished){
			int index = findTaskIndex(this.finishedTasks, task.getId());
			this.finishedTasks.remove(index);
			if(counting){
				m_numberForRemovedFinish++;
			}
		}else{
			int index = findTaskIndex(this.tasks, task.getId());
			this.tasks.remove(index);
		}
		return true;
	}
	
	
	public List<Task> getTasks(){
		return this.tasks;
	}
	
	public List<Task> getFinishedTasks(){
		return this.finishedTasks;
	}

	public String getSimpleInfo(){
		return this.getName() + " - " + this.getCompletedTaskNumber() + "/" + this.getAllTaskNumber(); 
	}

	@Override
	public TaskFolder clone() {
		TaskFolder folder = new TaskFolder();
		folder.setId(this.getId());
		folder.setName(this.getName());
		for(Task task : this.tasks){
			folder.addTask(task.clone());
		}
		return folder;
	}
	
	public void resetRemovedFinish(){
		m_numberForRemovedFinish = 0;
	}
	
	 

	
	

	

}
