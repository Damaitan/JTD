/**
 * 
 */
package com.damaitan.datamodel;


/* Backlog
 *   task repeat
 *   reminder
 *   reminder repeat
 *   TaskList
 */


/**
 * @author admin
 *
 */
public class Task extends Model {
	
	public enum Status{
		ongoing,finished
	}
	
	private boolean urgent;
	private String expired;
	private int calendarType;
	private String note;
	private int priority;
	private String[] tags;
	private TaskFolder taskFolder;
	private Status status;
	
	

	public Task() {
		super();
		status = Status.ongoing;
	}
	
	/**
	 * @return the urgent
	 */
	public boolean isUrgent() {
		return urgent;
	}
	/**
	 * @param urgent the urgent to set
	 */
	public void setUrgent(boolean urgent) {
		this.urgent = urgent;
	} 
	
	/**
	 * @return the expired
	 */
	public String getExpired() {
		return expired;
	}
	/**
	 * @param expired the expired to set
	 */
	public void setExpired(String expired) {
		this.expired = expired;
	}


	
	/**
	 * @return the calendarType
	 */
	public int getCalendarType() {
		return calendarType;
	}
	/**
	 * @param calendarType the calendarType to set
	 */
	public void setCalendarType(int calendarType) {
		this.calendarType = calendarType;
	}


	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the tags
	 */
	public String[] getTags() {
		return tags;
	}
	/**
	 * @param tags the tags to set
	 */
	public void setTags(String[] tags) {
		this.tags = tags;
	}
	/**
	 * @return the taskFolder
	 */
	public TaskFolder getTaskFolder() {
		return taskFolder;
	}
	/**
	 * @param taskFolder the taskFolder to set
	 */
	public void setTaskFolder(TaskFolder taskFolder) {
		if(this.taskFolder != null && this.taskFolder.getId() != taskFolder.getId()){
			//Whether needs validation
			this.taskFolder.removeTask(this);
		}
		this.taskFolder = taskFolder;
		taskFolder.addTask(this);
	}
	
	public void finish(){
		this.taskFolder.finishTask(this);
		this.status = Status.finished;
	}
	
	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	/*public void setStatus(Status status) {
		this.status = status;
	}*/

}
