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
	private String tags;
	private long taskFolderId;
	private Status status;
	
	

	public Task() {
		super(Model.invalidId,Model.invalidStr);
		status = Status.ongoing;
	}
	
	public Task(long id,String name) {
		super(id,name);
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
	public String getTags() {
		return tags;
	}
	/**
	 * @param tags the tags to set
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}
	/**
	 * @return the taskFolder
	 */
	public long getTaskFolderId() {
		return taskFolderId;
	}
	/**
	 * @param taskFolder the taskFolder to set
	 */
	public void setTaskFolderId(long id) {
		this.taskFolderId = id;
	}
	
	public void setStatus(Status status){
		this.status = status;
	}
	
	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone(),×¢ÒâcloneµÄÏÝÚå
	 */
	@Override
	public Task clone(){
		Task task = new Task();
		task.setCalendarType(this.getCalendarType());
		task.setExpired(this.getExpired());
		task.setId(this.getId());
		task.setName(this.getName());
		task.setNote(this.getNote());
		task.setPriority(this.getPriority());
		task.setTags(this.getTags());
		task.setTaskFolderId(this.getTaskFolderId());
		task.setUrgent(this.isUrgent());
		return task;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return super.equals(arg0);
	}

	/**
	 * @param status the status to set
	 */
	/*public void setStatus(Status status) {
		this.status = status;
	}*/
	

}
