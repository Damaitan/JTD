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
	private int numberForRemovedFinish = 0;
	
	public TaskFolder() {
		super();
	}
	
	public TaskFolder(long i, String name) {
		super(i,name);
	}
	
	public int getAllTaskNumber() {
		return numberForRemovedFinish + this.tasks.size() + this.finishedTasks.size();
	}
	
	public int getCompletedTaskNumber() {
		return numberForRemovedFinish + this.finishedTasks.size();
	}
	
	public boolean addTask(Task task) {
		task.taskFolderId = this.getId();
		for(Task temp : tasks){
			if(temp.getId() == task.getId()){
				return false;
			}
		}
		return this.tasks.add(task);
	}
	private int findTaskIndex(ArrayList<Task> tasks, long id){
		for(int i = 0;i < tasks.size(); i++){
			if(tasks.get(i).getId() == id){
				return i;
			}
		}
		return -1;
	}
	
	public Task updateTask(Task task){
		int index = findTaskIndex(this.tasks, task.getId());
		Task oldTask = this.tasks.get(index);
		if(oldTask.equals(task)) return null;
		if(task.taskFolderId != this.getId()){
			this.tasks.remove(index);
		}else{
			this.tasks.set(index, task);
		}
		return oldTask;
	}
	
	public Task finishTask(Task task){
		removeTask(task, false);
		Task old = task.clone();
		task.status = Status.finished;
		this.finishedTasks.add(task);
		return old;
	}
	
	public Task activateTask(Task task){
		removeTask(task, false);
		Task old = task.clone();
		task.status = Status.ongoing;
		this.tasks.add(task);
		return old;
	}
	
	public Task removeTask(Task task){
		return removeTask(task, true);
	}
	
	public Task removeTask(Task task, boolean counting){
		if(task == null ) return null;
		Task old = null;
		if(task.status == Status.finished){
			int index = findTaskIndex(this.finishedTasks, task.getId());
			old = this.finishedTasks.get(index);
			this.finishedTasks.remove(index);
			if(counting){
				numberForRemovedFinish++;
			}
		}else{
			int index = findTaskIndex(this.tasks, task.getId());
			old = this.tasks.get(index);
			this.tasks.remove(index);
		}
		return old;
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
	
	public Task findTask(long id){
		int index = findTaskIndex(this.tasks,id);
		if (-1 != index) return this.tasks.get(index);
		index = findTaskIndex(this.finishedTasks,id);
		if (-1 != index) return this.finishedTasks.get(index);
		return null;
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
		numberForRemovedFinish = 0;
	}
	
	public void cleanFinished(){
		numberForRemovedFinish = numberForRemovedFinish + getFinishedTasks().size();
		getFinishedTasks().clear();
	}
	
	 

	
	

	

}
