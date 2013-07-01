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
	private boolean urgent;
	private String expired;
	private int calendarType;
	private String note;
	private int priority;
	private String[] tags;
	private TaskFolder taskFolder;
	
	
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
		this.taskFolder = taskFolder;
	}


}
