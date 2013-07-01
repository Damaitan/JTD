/**
 * 
 */
package com.damaitan.datamodel;

/**
 * @author admin
 *
 */
public class TaskFolder extends Model {
	private int allTaskNumber;
	private int completedTaskNumber;
	private Task[] tasks;
	/**
	 * @return the allTaskNumber
	 */
	public int getAllTaskNumber() {
		return allTaskNumber;
	}
	/**
	 * @param allTaskNumber the allTaskNumber to set
	 */
	public void setAllTaskNumber(int allTaskNumber) {
		this.allTaskNumber = allTaskNumber;
	}
	/**
	 * @return the completedTaskNumber
	 */
	public int getCompletedTaskNumber() {
		return completedTaskNumber;
	}
	/**
	 * @param completedTaskNumber the completedTaskNumber to set
	 */
	public void setCompletedTaskNumber(int completedTaskNumber) {
		this.completedTaskNumber = completedTaskNumber;
	}
	/**
	 * @return the tasks
	 */
	public Task[] getTasks() {
		return tasks;
	}
	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(Task[] tasks) {
		this.tasks = tasks;
	}
}
