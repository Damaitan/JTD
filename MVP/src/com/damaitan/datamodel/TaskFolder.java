/**
 * 
 */
package com.damaitan.datamodel;

import java.util.ArrayList;

import com.damaitan.datamodel.Task.Status;

/**
 * @author admin
 *
 */
public class TaskFolder extends Model {
	//private int allTaskNumber;
	//private int completedTaskNumber;
	private ArrayList<Task> tasks;
	private ArrayList<Task> finishedTasks;
	
	
	public TaskFolder() {
		super();
		//allTaskNumber = 0;
		//completedTaskNumber = 0;
		tasks = new ArrayList<Task>();
		finishedTasks = new ArrayList<Task>();
	}
	
	public TaskFolder(int i, String name) {
		super(i,name);
	}

	/**
	 * @return the allTaskNumber
	 */
	public int getAllTaskNumber() {
		return tasks.size() + finishedTasks.size();
	}
	/**
	 * @param allTaskNumber the allTaskNumber to set
	 */
	/*public void setAllTaskNumber(int allTaskNumber) {
		this.allTaskNumber = allTaskNumber;
	}*/
	/**
	 * @return the completedTaskNumber
	 */
	public int getCompletedTaskNumber() {
		//return completedTaskNumber;
		return finishedTasks.size();
	}
	/**
	 * @param completedTaskNumber the completedTaskNumber to set
	 */
	/*public void setCompletedTaskNumber(int completedTaskNumber) {
		this.completedTaskNumber = completedTaskNumber;
	}*/
	/**
	 * @return the tasks
	 */
	/*public Task[] getTasks() {
		return tasks;
	}*/
	/**
	 * @param tasks the tasks to set
	 */
	/*public void setTasks(Task[] tasks) {
		this.tasks = tasks;
		this.allTaskNumber = tasks.length;
	}*/
	
	/**
	 * @param task
	 * 
	 */
	public void addTask(Task task){
		
		if(task.getStatus() == Status.finished){
			this.finishedTasks.add(task);
		}else{
			this.tasks.add(task);
		}
		
	}
	
	public void finishTask(Task task){
		this.tasks.remove(task);
		this.finishedTasks.add(task);
	}
	
	public void removeTask(Task task){
		if(task.getStatus() == Status.finished){
			this.finishedTasks.remove(task);
		}else{
			this.tasks.remove(task);
		}
	}
	
	public ArrayList<Task> getTasks(){
		return this.tasks;
	}

}
