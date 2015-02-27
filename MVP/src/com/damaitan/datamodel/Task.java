/**
 * 
 */
package com.damaitan.datamodel;
/**
 * @author admin
 *
 */
public class Task extends Model {
	
	public enum Status{
		ongoing,finished
	}
	public final static String TAGSPLITTER = " ";
	public final static String DATESPLITTER = "/";
	public boolean urgent = false;
	public String expired = "";
	public String note = "";
	public int priority = 0;
	public String tags = "";
	public long taskFolderId = 1;
	public Status status = Task.Status.ongoing;
	public boolean repeat = false;
	public int repeat_proid = -1;
	public long parentTaskId = Task.invalidId;
	public int finishedChilden = 0;

	public Task() {
		super(Model.invalidId,Model.invalidStr);
		status = Status.ongoing;
		priority = 0;
		parentTaskId = Model.invalidId;
	}
	
	public Task(long id,String name) {
		super(id,name);
		status = Status.ongoing;
		priority = 0;
		parentTaskId = Model.invalidId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone(),×¢ÒâcloneµÄÏÝÚå
	 */
	@Override
	public Task clone(){
		Task task = new Task();
		task.setId(this.getId());
		task.setName(this.getName());
		task.expired = this.expired;
		task.note = this.note;
		task.parentTaskId = this.parentTaskId;
		task.priority = this.priority;
		task.repeat = this.repeat;
		task.repeat_proid = this.repeat_proid;
		task.status = this.status;
		task.tags = this.tags;
		task.taskFolderId = this.taskFolderId;
		task.urgent = this.urgent;
		task.finishedChilden = this.finishedChilden;
		return task;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Task) {
			Task task = (Task) obj;
			return task.getName().equals(this.getName()) &&
			task.getId() == this.getId() &&
			task.expired.equals(this.expired) &&
			task.note.equals(this.note) &&
			task.parentTaskId == this.parentTaskId &&
			task.priority == this.priority &&
			task.repeat == this.repeat &&
			task.repeat_proid == this.repeat_proid &&
			task.status == this.status &&
			task.tags.equals(this.tags)&&
			task.taskFolderId == this.taskFolderId &&
			task.urgent == this.urgent;
		}
		return false;
	}
	
	
}
